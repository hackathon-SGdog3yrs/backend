package likelion13th.hackathon3rd.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import likelion13th.hackathon3rd.domain.User;
import likelion13th.hackathon3rd.dto.UserProfileResponse;
import likelion13th.hackathon3rd.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
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
    public void appendKeyword(Integer userId, String keyword) {
        if (keyword == null || keyword.isBlank()) return;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user not found: " + userId));

        // 🔁 기존 값이 ["산책","건강"] 이든, "[\"산책\",\"건강\"]" 이든, "산책,건강" 이든 전부 안전하게 파싱
        List<String> list = readKeywordsLenient(user.getKeyword());

        // 최근 것이 앞에 오도록 + 중복 제거
        LinkedHashSet<String> set = new LinkedHashSet<>();
        set.add(keyword.trim());
        set.addAll(list);
        List<String> updated = new ArrayList<>(set);

        // 최대 20개 제한
        if (updated.size() > 20) updated = updated.subList(0, 20);

        try {
            String newJson = objectMapper.writeValueAsString(updated); // 항상 정상 JSON 배열로 저장
            user.updateKeywordJson(newJson);
        } catch (Exception e) {
            throw new RuntimeException("failed to save keyword json", e);
        }
    }

    public List<String> getUserKeywords(int userId) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user not found: " + userId));
        // 조회도 관대한 파서로 통일 → 항상 깔끔한 리스트 반환
        return readKeywordsLenient(u.getKeyword());
    }

    // ---------------------- 유틸리티: 관대한 읽기 + 평탄화 ----------------------

    /**
     * 관대한 키워드 파서.
     * - 정상 배열: ["산책","건강"]
     * - 배열이 문자열로 저장: "[\"산책\",\"건강\"]"
     * - CSV: 산책,건강
     * - 단일 문자열: 산책
     * 을 모두 List<String>으로 복원하고,
     * 리스트 내부에 "[\"...\",\"...\"]" 같은 '배열 문자열'이 섞여 있으면 평탄화한다.
     */
    private List<String> readKeywordsLenient(String raw) {
        List<String> out = new ArrayList<>();
        if (raw == null || raw.isBlank()) return out;

        String s = raw.trim();

        // 1) 정상 JSON 배열
        if (s.startsWith("[")) {
            try {
                List<String> base = objectMapper.readValue(s, new TypeReference<List<String>>() {});
                return flattenNestedStrings(base);
            } catch (Exception ignore) { /* 다음 케이스 시도 */ }
        }

        // 2) 따옴표로 감싼 JSON 배열 문자열: "[\"산책\",\"건강\"]"
        if (s.startsWith("\"") && s.endsWith("\"")) {
            try {
                String inner = objectMapper.readValue(s, String.class); // 따옴표 벗기기
                if (inner != null) {
                    String t = inner.trim();
                    if (t.startsWith("[")) {
                        List<String> arr = objectMapper.readValue(t, new TypeReference<List<String>>() {});
                        return flattenNestedStrings(arr);
                    } else {
                        // 그냥 문자열 한 개로 저장돼 있던 경우
                        return splitCsvOrSingle(t);
                    }
                }
            } catch (Exception ignore) { /* 다음 케이스 시도 */ }
        }

        // 3) CSV로 저장돼 있던 케이스
        if (s.contains(",")) {
            return splitCsvOrSingle(s);
        }

        // 4) 단일 토큰
        out.add(s);
        return out;
    }

    /** 리스트 안에 "[\"산책\",\"건강\"]" 같은 '배열 문자열' 원소가 섞여 있으면 풀어서 평탄화 */
    private List<String> flattenNestedStrings(List<String> in) {
        List<String> out = new ArrayList<>();
        if (in == null) return out;

        for (String e : in) {
            if (e == null) continue;
            String t = e.trim();
            if (t.isEmpty()) continue;

            // 원소 자체가 JSON 배열 문자열인 경우 풀어서 추가
            if (t.startsWith("[") && t.endsWith("]")) {
                try {
                    List<String> inner = objectMapper.readValue(t, new TypeReference<List<String>>() {});
                    for (String x : inner) {
                        if (x != null && !x.isBlank()) out.add(x.trim());
                    }
                    continue;
                } catch (Exception ignore) { /* 그냥 원소로 취급 */ }
            }
            out.add(t);
        }
        return out;
    }

    /** 콤마가 있으면 CSV split, 없으면 단일 문자열 */
    private List<String> splitCsvOrSingle(String s) {
        List<String> out = new ArrayList<>();
        if (s.contains(",")) {
            for (String p : s.split(",")) {
                String t = p.trim();
                if (!t.isEmpty()) out.add(t);
            }
        } else {
            String t = s.trim();
            if (!t.isEmpty()) out.add(t);
        }
        return out;
    }
}