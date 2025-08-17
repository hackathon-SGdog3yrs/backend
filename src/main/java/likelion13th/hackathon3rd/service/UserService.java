package likelion13th.hackathon3rd.service;

import likelion13th.hackathon3rd.domain.User;
import likelion13th.hackathon3rd.dto.UserProfileResponse;
import likelion13th.hackathon3rd.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserProfileResponse getUserProfile(Integer userid) {
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));
        return UserProfileResponse.from(user);
    }

    // 사용자 키워드 조회
    public List<String> getUserKeywords(int userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getKeyword() == null) return List.of();

        try {
            return objectMapper.readValue(user.getKeyword(), new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return List.of();
        }
    }

    // 키워드 추가 (챗봇용)
    @Transactional
    public void appendKeyword(int userId, String newKeyword) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return;

        List<String> keywords = getUserKeywords(userId);
        if (keywords == null) keywords = new ArrayList<>();

        // 중복 방지
        if (!keywords.contains(newKeyword)) {
            keywords.add(newKeyword);
            try {
                user.setKeyword(objectMapper.writeValueAsString(keywords));
                userRepository.save(user);
            } catch (Exception e) {
                // 조용히 실패
            }
        }
    }
}
