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
        List<String> out = new ArrayList<>();
        if (json == null || json.isBlank()) return out;

        try {
            // 1) JSON 배열
            return objectMapper.readValue(json, new com.fasterxml.jackson.core.type.TypeReference<List<String>>() {});
        } catch (Exception ignore) { }

        try {
            // 2) 단일 JSON 문자열
            String one = objectMapper.readValue(json, String.class);
            if (one != null && !one.isBlank()) {
                // 2-1) 이 문자열이 JSON 배열 형태라면 다시 파싱
                if (one.trim().startsWith("[") && one.trim().endsWith("]")) {
                    try {
                        return objectMapper.readValue(one, new com.fasterxml.jackson.core.type.TypeReference<List<String>>() {});
                    } catch (Exception innerIgnore) { }
                }
                out.add(one.trim());
            }
            return out;
        } catch (Exception ignore) { }

        // 3) 콤마 문자열
        for (String s : json.split(",")) {
            String t = s == null ? "" : s.trim();
            if (!t.isBlank()) out.add(t);
        }
        return out;
    }

    private String normalize(String k) {
        if (k == null) return "";
        String s = k.trim();
        // 양끝 불필요한 기호 제거
        s = s.replaceAll("^[#@\\-\\s]+|[\\s\\-,:;!?.]+$", "");
        if (s.length() > 40) s = s.substring(0, 40);
        return s;
    }
    private String writeKeywords(List<String> list) {
        try {
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            // 문제가 생기면 비워서라도 저장 실패 방지
            return "[]";
        }
    }

}
