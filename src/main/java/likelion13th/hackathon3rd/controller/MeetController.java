package likelion13th.hackathon3rd.controller;

import likelion13th.hackathon3rd.dto.ApiResponse;
import likelion13th.hackathon3rd.dto.MeetCreatedItemResponse;
import likelion13th.hackathon3rd.service.MeetQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meet")
public class MeetController {

    private final MeetQueryService meetQueryService;

    @GetMapping("/created-list")
    public ApiResponse<List<MeetCreatedItemResponse>> getCreatedList(
            @RequestParam("id_u") Integer userId,
            @RequestParam(value = "sortBy", defaultValue = "dateTime") String sortBy,
            @RequestParam(value = "order", defaultValue = "desc") String order
    ) {
        List<MeetCreatedItemResponse> data = meetQueryService.getCreatedList(userId, sortBy, order);

        if (data.isEmpty()) {
            return ApiResponse.ok("아직 생성한 모임이 없습니다", data);
        }
        return ApiResponse.ok("생성한 모임 리스트 조회 성공", data);
    }
}