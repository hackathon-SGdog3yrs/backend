package likelion13th.hackathon3rd.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import likelion13th.hackathon3rd.domain.User;
import likelion13th.hackathon3rd.dto.UserProfileResponse;
import likelion13th.hackathon3rd.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserProfileResponse getUserProfile(Integer userid) {
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));
        return UserProfileResponse.from(user);
    }

    @Transactional
    public void appendKeyword(Integer userId, String rawKeyword) {
        String k = normalize(rawKeyword);
        if (k.isBlank()) return;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        List<String> keywords = readKeywords(user.getKeyword());
        boolean exists = keywords.stream().anyMatch(s -> s.equalsIgnoreCase(k));
        if (!exists) {
            keywords.add(k);
            String json = writeKeywords(keywords);
            user.updateKeywordJson(json);
        }
    }

    private List<String> readKeywords(String json) {
        try {
            if (json == null || json.isBlank()) return new ArrayList<>();
            return objectMapper.readValue(json, new com.fasterxml.jackson.core.type.TypeReference<List<String>>() {});
        } catch (Exception e) {
            // 파싱 실패 시 안전하게 새로 시작
            return new ArrayList<>();
        }
    }

    private String writeKeywords(List<String> list) {
        try {
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            // 문제가 생기면 비워서라도 저장 실패 방지
            return "[]";
        }
    }

    private String normalize(String k) {
        if (k == null) return "";
        String s = k.trim();
        // 양끝 불필요한 기호 제거
        s = s.replaceAll("^[#@\\-\\s]+|[\\s\\-,:;!?.]+$", "");
        if (s.length() > 40) s = s.substring(0, 40);
        return s;
    }


}
