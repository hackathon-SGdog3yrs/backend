package likelion13th.hackathon3rd.controller;

import likelion13th.hackathon3rd.dto.MeetJoinedItemResponse;
import likelion13th.hackathon3rd.dto.UserProfileResponse;
import likelion13th.hackathon3rd.dto.ApiResponse;
import likelion13th.hackathon3rd.dto.MeetCreatedItemResponse;
import likelion13th.hackathon3rd.service.MeetQueryService;
import likelion13th.hackathon3rd.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final MeetQueryService meetQueryService; // ⬅️ 추가

    // 내 정보 조회
    @GetMapping("/{id}")
    public UserProfileResponse getMyPage(@PathVariable Integer id) {
        return userService.getUserProfile(id);
    }

    // 내가 생성한 모임 리스트, 모임 시간순으로 정렬
    @GetMapping("/{id}/created-list")
    public ApiResponse<List<MeetCreatedItemResponse>> getCreatedList(
            @PathVariable("id") Integer userId,
            @RequestParam(value = "sortBy", defaultValue = "dateTime") String sortBy,
            @RequestParam(value = "order", defaultValue = "desc") String order
    ) {
        List<MeetCreatedItemResponse> data = meetQueryService.getCreatedList(userId, sortBy, order);

        if (data.isEmpty()) {
            return ApiResponse.ok("아직 생성한 모임이 없습니다", data);
        }
        return ApiResponse.ok("생성한 모임 리스트 조회 성공", data);
    }

    //내가 참여한 모임 리스트, 시간순으로 정렬
    @GetMapping("/{id}/joined-list")
    public ApiResponse<List<MeetJoinedItemResponse>> getJoinedList(
            @PathVariable("id") Integer userId,
            @RequestParam(value = "sortBy", defaultValue = "dateTime") String sortBy,
            @RequestParam(value = "order", defaultValue = "desc") String order
    ) {
        List<MeetJoinedItemResponse> data = meetQueryService.getJoinedList(userId, sortBy, order);
        if (data.isEmpty()) return ApiResponse.ok("아직 참여한 모임이 없습니다", data);
        return ApiResponse.ok("참가한 모임 리스트 조회 성공", data);
    }
}
