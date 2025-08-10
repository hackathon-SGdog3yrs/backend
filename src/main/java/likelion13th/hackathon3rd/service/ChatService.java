package likelion13th.hackathon3rd.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import likelion13th.hackathon3rd.service.UserService;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserService userService;

    // 1) 사용자의 메시지를 입력받아 (임시) 답변 + 키워드 추출 → 키워드 저장 → 답변 반환
    @Transactional
    public String handleUserMessage(Integer userId, String userMessage) {
        // (임시) 답변: 에코
        String answer = "말씀하신 내용: " + userMessage;

        // (임시) 키워드 1개 추출: 첫 단어(공백 기준) 정리
        String keyword = extractOneKeywordMock(userMessage);

        // 저장
        userService.appendKeyword(userId, keyword);

        return answer;
    }

    // 아주 간단한 모킹 추출기 (나중에 OpenAI로 교체)
    private String extractOneKeywordMock(String text) {
        if (text == null || text.isBlank()) return "";
        String first = text.trim().split("\\s+")[0];
        // 기호 정리(appendKeyword에서도 normalize 하지만 중복 안전)
        return first.replaceAll("^[#@\\-\\s]+|[\\s\\-,:;!?.]+$", "");
    }
}