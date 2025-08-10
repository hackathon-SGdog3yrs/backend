package likelion13th.hackathon3rd.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component
public class OpenAiClient {

    @Value("${openai.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 필요하면 외부에서 바꿀 수 있도록 상수로 둠
    private static final String MODEL = "gpt-4o-mini";

    private static final String SYSTEM_PROMPT = """
        You are a meetup-concierge assistant.
        Goal:
        1) Read the user's message and infer what kind of meetup they are looking for (topic, vibe).
        2) Produce ONE concise central keyword (noun or noun-phrase in the user's language) that best represents the user's current intent.
        3) Give a helpful, short reply to keep the conversation going.

        Rules:
        - Output JSON with fields: {"reply": "...", "keyword": "..."} only. No extra text.
        - The keyword MUST exclude particles, stopwords, and PII. Prefer concrete topics (e.g., "등산", "보드게임", "카페탐방").
        - If the message is ambiguous, pick the most likely topic and ask 1 clarifying question in "reply".
        """;

    public static final class ChatResult {
        private final String reply;
        private final String keyword;
        public ChatResult(String reply, String keyword) {
            this.reply = reply;
            this.keyword = keyword;
        }
        public String reply() { return reply; }
        public String keyword() { return keyword; }
    }

    private WebClient getClient() {
        return WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /** 한 번의 호출로 reply + keyword 동시 생성 */
    public ChatResult generateReplyAndKeyword(String userUtterance) {
        var messages = List.of(
                Map.of("role", "system", "content", SYSTEM_PROMPT),
                Map.of("role", "user",   "content", userUtterance)
        );

        var body = Map.of(
                "model", MODEL,
                "messages", messages,
                // 모델이 반드시 {"reply":"..","keyword":".."} JSON만 내도록 강제
                "response_format", Map.of(
                        "type", "json_schema",
                        "json_schema", Map.of(
                                "name", "reply_and_keyword",
                                "schema", Map.of(
                                        "type", "object",
                                        "properties", Map.of(
                                                "reply",   Map.of("type","string","minLength",1,"maxLength",300),
                                                "keyword", Map.of("type","string","minLength",1,"maxLength",40)
                                        ),
                                        "required", List.of("reply","keyword"),
                                        "additionalProperties", false
                                )
                        )
                )
        );

        try {
            Map<?, ?> res = getClient().post()
                    .uri("/chat/completions")
                    .bodyValue(body)
                    // 상태코드를 직접 보고 429면 Retry-After 고려해 재시도 유도
                    .exchangeToMono(response -> {
                        int code = response.statusCode().value();
                        if (code == 429) {
                            String ra = response.headers().asHttpHeaders().getFirst("Retry-After");
                            Duration wait = parseRetryAfter(ra);
                            return Mono.delay(wait).then(response.createException()).flatMap(Mono::error);
                        } else if (response.statusCode().isError()) {
                            return response.createException().flatMap(Mono::error);
                        }
                        return response.bodyToMono(Map.class);
                    })
                    .retryWhen(
                            Retry.backoff(3, Duration.ofMillis(500))
                                    .jitter(0.5)
                                    .filter(ex -> ex instanceof WebClientResponseException we
                                            && we.getStatusCode().value() == 429)
                    )
                    .timeout(Duration.ofSeconds(15))
                    .block();

            var choices = (List<Map<String, Object>>) res.get("choices");
            var msg = (Map<String, Object>) choices.get(0).get("message");
            String json = (String) msg.get("content"); // {"reply":"...","keyword":"..."}

            JsonNode node = objectMapper.readTree(json);
            String reply   = node.path("reply").asText("");
            String keyword = node.path("keyword").asText("");

            if (reply.isBlank() && keyword.isBlank()) {
                // 혹시 JSON이 비어오면 폴백 세팅
                return new ChatResult("원하시는 모임을 한 줄로 말해 주세요. 예: '주말 보드게임'", firstMeaningfulToken(userUtterance));
            }
            return new ChatResult(reply, keyword);

        } catch (Exception e) {
            // 최종 폴백
            return new ChatResult("좋아요! 간단히 한 줄로 말씀해 주세요. 예: '카페 탐방'", firstMeaningfulToken(userUtterance));
        }
    }

    // Retry-After 헤더 파싱(초 숫자만 처리)
    private static Duration parseRetryAfter(String ra) {
        if (ra == null || ra.isBlank()) return Duration.ofSeconds(1);
        try {
            long seconds = Long.parseLong(ra.trim());
            return Duration.ofSeconds(Math.max(1, seconds));
        } catch (NumberFormatException e) {
            return Duration.ofSeconds(1);
        }
    }

    // 폴백: 문장에서 의미 있는 토큰 대충 한 개 (정교한 건 KeywordUtils 사용 권장)
    private static String firstMeaningfulToken(String text) {
        if (text == null) return "";
        String cleaned = text.replaceAll("[^ㄱ-ㅎ가-힣A-Za-z0-9]+", " ").trim();
        if (cleaned.isEmpty()) return "";
        String first = cleaned.split("\\s+")[0];
        return first.replaceAll("(은|는|이|가|을|를|에|의|와|과)$", "");
    }
}