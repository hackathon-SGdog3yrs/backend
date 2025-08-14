package likelion13th.hackathon3rd.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import likelion13th.hackathon3rd.dto.ChatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenAIChatService {

    private final OpenAiService openAiService;
    private final UserService userService; // ✅ 키워드 저장용 서비스 주입

    public String getChatResponse(ChatRequest requestDto) {
        String userMessage = requestDto.getMessage();
        int userId = requestDto.getUserId();

        if (userMessage == null || userMessage.isBlank()) {
            return "메시지가 비어 있습니다. 무엇이든 입력해 주세요.";
        }

        String instructions = String.join("\n",
                "당신은 '실버 세대'를 위한 지역 모임 추천 챗봇입니다.",
                "항상 한국어로, 쉬운 단어를 사용해 예의바르게 대화하세요.",
                "답변이 한국어 맞춤법에 맞는지 항상 확인하세요",
                "대답은 복잡하지 않게, 세 문장 이하로만 작성합니다.",
                "사용자가 말한 내용에서 핵심 취향을 키워드 하나로 뽑아 내부적으로 저장하세요. (이 키워드는 사용자에게 보여주지 않습니다.)",
                "사용자가 원하면, 저장된 키워드를 바탕으로 근처에서 열리는 관련 모임 리스트를 보여주세요.",
                "항상 친근하고 따뜻한 말투를 유지하세요."
        );
        ChatMessage system = new ChatMessage("system", instructions);
        ChatMessage user = new ChatMessage("user", userMessage);

        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .model("gpt-4o-mini")
                .messages(List.of(system, user))
                .temperature(0.7)
                .maxTokens(512)
                .build();

        try {
            // 1) GPT 호출 → 답변 만들기
            ChatCompletionResult result = openAiService.createChatCompletion(completionRequest);
            String reply = result.getChoices().get(0).getMessage().getContent();

            // 2) ✅ 키워드 추출 & 저장 (return 직전에 넣기)
            // userId가 0일 수 있으니(기본값) 방어적으로 체크
            if (userId > 0) {
                try {
                    String keyword = extractKeyword(userMessage);
                    if (keyword != null && !keyword.isBlank()) {
                        userService.appendKeyword(userId, keyword.trim());
                    }
                } catch (Exception sub) {
                    // 조용히 로깅만 (필요하면 로거 사용)
                    sub.printStackTrace();
                }
            }

            // 3) 사용자에게는 답변만 반환
            return reply;

        } catch (Exception e) {
            e.printStackTrace();
            return "OpenAI 호출 중 오류가 발생했어요: " + (e.getMessage() != null ? e.getMessage() : "원인 미상");
        }
    }

    // 🔽 키워드 한 단어만 추출
    private String extractKeyword(String userMessage) {
        ChatMessage sys = new ChatMessage("system",
                "아래 문장에서 사용자의 취향을 대표하는 한국어 명사 키워드 1개만 출력하세요. " +
                        "형용사, 문장, 해시태그, 따옴표, 설명 금지. " +
                        "올바른 예시: 등산, 재즈, 커피, 전시, 봉사");
        ChatMessage usr = new ChatMessage("user", userMessage);

        ChatCompletionRequest req = ChatCompletionRequest.builder()
                .model("gpt-4o-mini")
                .messages(List.of(sys, usr))
                .temperature(0.0)
                .maxTokens(10)
                .build();

        // 한 번 실패하면 딱 1회 재시도
        for (int attempt = 0; attempt < 2; attempt++) {
            ChatCompletionResult res = openAiService.createChatCompletion(req);
            String raw = res.getChoices().get(0).getMessage().getContent();

            String kw = raw == null ? "" : raw.trim();
            kw = kw.replaceAll("[\"'`#]", "").trim();
            if (kw.contains(" ")) kw = kw.substring(0, kw.indexOf(' '));
            if (kw.length() > 20) kw = kw.substring(0, 20);

            // 너무 짧거나 의미 없음 방지 (선택)
            if (kw.length() >= 1) return kw;
        }
        return "";
    }
}