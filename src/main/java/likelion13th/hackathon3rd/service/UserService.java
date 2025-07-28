package likelion13th.hackathon3rd.service;

import likelion13th.hackathon3rd.domain.User;
import likelion13th.hackathon3rd.dto.UserProfileResponse;
import likelion13th.hackathon3rd.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserProfileResponse getUserProfile(Integer userid) {
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));
        return UserProfileResponse.from(user);
    }
}
