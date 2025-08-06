package likelion13th.hackathon3rd.controller;

import likelion13th.hackathon3rd.dto.UserProfileResponse;
import likelion13th.hackathon3rd.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public UserProfileResponse getMyPage(@PathVariable Integer id) {
        return userService.getUserProfile(id);
    }
}
