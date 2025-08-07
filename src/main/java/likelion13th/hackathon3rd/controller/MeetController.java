package likelion13th.hackathon3rd.controller;

import likelion13th.hackathon3rd.dto.MeetCreateRequest;
import likelion13th.hackathon3rd.dto.MeetCreateResponse;
import likelion13th.hackathon3rd.dto.MeetDetailResponse;
import likelion13th.hackathon3rd.dto.MeetListResponse;
import likelion13th.hackathon3rd.service.MeetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meet")
@RequiredArgsConstructor
public class MeetController {

    private final MeetService meetService;

    // 전체 모임 리스트 조회
    // 진행 중인 모임들을 현재 참여자 수 많은 순으로 정렬하여 반환
    // @param sort 정렬 기준 (현재는 currentDesc만 지원)
    // @return 모임 리스트
    @GetMapping("/list")
    public ResponseEntity<List<MeetListResponse>> getMeetList() {
        
        // 현재는 currentDesc 정렬만 지원
        List<MeetListResponse> meetList = meetService.getAllAvailableMeets();
        
        return ResponseEntity.ok(meetList);
    }

    // 모임 상세 정보 조회
    // @param id 모임 ID
    // @return 모임 상세 정보
    @GetMapping("/{id}")
    public ResponseEntity<MeetDetailResponse> getMeetDetail(@PathVariable Integer id) {
        
        MeetDetailResponse meetDetail = meetService.getMeetDetail(id);
        
        return ResponseEntity.ok(meetDetail);
    }

    // 새로운 모임 생성
    // @param request 모임 생성 요청 정보
    // @return 생성 결과
    @PostMapping("/create")
    public ResponseEntity<MeetCreateResponse> createMeet(@RequestBody MeetCreateRequest request) {
        
        MeetCreateResponse response = meetService.createMeet(request);
        
        return ResponseEntity.status(201).body(response);
    }
}