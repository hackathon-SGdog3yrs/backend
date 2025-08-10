package likelion13th.hackathon3rd.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OpenAiClient {

    @Value("${openai.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private WebClient getClient() {
        return WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String extractOneKeyword(String userUtterance) {
        var messages = List.of(
                Map.of("role","system","content",
                        "You are a keyword extractor. Return EXACTLY ONE concise keyword that best represents the user's latest message. " +
                                "No explanations. Respond as JSON: {\"keyword\":\"...\"}."),
                Map.of("role","user","content", userUtterance)
        );

        var body = Map.of(
                "model", "gpt-4o-mini",
                "messages", messages,
                "response_format", Map.of(
                        "type", "json_schema",
                        "json_schema", Map.of(
                                "name", "keyword_schema",
                                "schema", Map.of(
                                        "type","object",
                                        "properties", Map.of(
                                                "keyword", Map.of("type","string","minLength",1,"maxLength",40)
                                        ),
                                        "required", List.of("keyword"),
                                        "additionalProperties", false
                                )
                        )
                )
        );

        try {
            Map<?,?> res = getClient().post()
                    .uri("/chat/completions")
                    .bodyValue(body)
                    // 여기서 status를 보고 직접 분기한다
                    .exchangeToMono(response -> {
                        int code = response.statusCode().value();
                        if (code == 429) {
                            // Retry-After 헤더가 있으면 그만큼 기다렸다가 예외를 흘려보내 재시도 트리거
                            String ra = response.headers().asHttpHeaders().getFirst("Retry-After");
                            Duration wait = parseRetryAfter(ra);
                            return Mono.delay(wait).then(response.createException()).flatMap(Mono::error);
                        } else if (response.statusCode().isError()) {
                            return response.createException().flatMap(Mono::error);
                        }
                        return response.bodyToMono(Map.class);
                    })
                    // 429로 들어온 예외에 대해 백오프 재시도
                    .retryWhen(
                            reactor.util.retry.Retry
                                    .backoff(3, java.time.Duration.ofMillis(500))
                                    .jitter(0.5)
                                    .filter(ex -> (ex instanceof org.springframework.web.reactive.function.client.WebClientResponseException w)
                                            && w.getStatusCode().value() == 429)
                    )
                    .timeout(java.time.Duration.ofSeconds(15))
                    .block();

            // choices[0].message.content 가 {"keyword":"..."} 문자열
            var choices = (List<Map<String, Object>>) res.get("choices");
            var msg = (Map<String, Object>) choices.get(0).get("message");
            String content = (String) msg.get("content");

            com.fasterxml.jackson.databind.JsonNode node = objectMapper.readTree(content);
            return node.get("keyword").asText();

        } catch (Exception e) {
            // 최종 폴백: 첫 단어
            String first = (userUtterance == null ? "" : userUtterance.trim()).split("\\s+")[0];
            return first.replaceAll("^[#@\\-\\s]+|[\\s\\-,:;!?.]+$", "");
        }
    }

    // Retry-After 문자열을 Duration으로 변환 (초 숫자만 처리; 날짜 형식은 기본 1초로)
    private static java.time.Duration parseRetryAfter(String ra) {
        if (ra == null || ra.isBlank()) return java.time.Duration.ofSeconds(1);
        try {
            long seconds = Long.parseLong(ra.trim());
            return java.time.Duration.ofSeconds(Math.max(1, seconds));
        } catch (NumberFormatException e) {
            return java.time.Duration.ofSeconds(1);
        }
    }
}