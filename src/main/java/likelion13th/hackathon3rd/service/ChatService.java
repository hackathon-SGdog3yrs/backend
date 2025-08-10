package likelion13th.hackathon3rd.service;

import jakarta.transaction.Transactional;
import likelion13th.hackathon3rd.client.OpenAiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import likelion13th.hackathon3rd.service.UserService;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserService userService;
    private final OpenAiClient openAiClient;

    @Transactional
    public String handleUserMessage(Integer userId, String userMessage) {
        String safe = userMessage == null ? "" : userMessage.trim();
        if (safe.isEmpty()) return "메시지가 비어 있어 키워드를 추출하지 않았습니다.";

        String keyword;
        try {
            keyword = openAiClient.extractOneKeyword(safe);
        } catch (Exception e) {
            keyword = safe.split("\\s+")[0].replaceAll("^[#@\\-\\s]+|[\\s\\-,:;!?.]+$", "");
        }

        userService.appendKeyword(userId, keyword);
        return "키워드 추출 완료";
    }


    // 아주 간단한 모킹 추출기 (나중에 OpenAI로 교체)
    private String extractOneKeywordMock(String text) {
        if (text == null || text.isBlank()) return "";
        String first = text.trim().split("\\s+")[0];
        // 기호 정리(appendKeyword에서도 normalize 하지만 중복 안전)
        return first.replaceAll("^[#@\\-\\s]+|[\\s\\-,:;!?.]+$", "");
    }

    
}