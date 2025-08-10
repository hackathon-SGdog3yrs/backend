package likelion13th.hackathon3rd.service;

import jakarta.transaction.Transactional;
import likelion13th.hackathon3rd.client.OpenAiClient;
import likelion13th.hackathon3rd.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final OpenAiClient openAiClient;
    private final UserService userService;

    @Transactional
    public String handleUserMessage(Integer userId, String userMessage) {
        String safe = userMessage == null ? "" : userMessage.trim();
        if (safe.isEmpty()) return "메시지가 비어 있어 키워드를 추출하지 않았습니다.";

        try {
            OpenAiClient.ChatResult r = openAiClient.generateReplyAndKeyword(safe);

            // 필요하면 KeywordUtils로 정리
            // String k = KeywordUtils.sanitizeModelKeyword(r.keyword(), safe);
            // userService.appendKeyword(userId, k);

            userService.appendKeyword(userId, r.keyword().trim());
            return r.reply();
        } catch (Exception e) {
            // 폴백
            userService.appendKeyword(userId, safe.split("\\s+")[0]);
            return "좋아요! 간단히 한 줄로 말씀해 주세요. 예: '카페 탐방'";
        }
    }
}