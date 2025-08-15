package likelion13th.hackathon3rd.controller;

import likelion13th.hackathon3rd.dto.LocationListResponse;
import likelion13th.hackathon3rd.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    // 장소 리스트 조회
    // @param sort 정렬 기준 (ad: 광고 우선)
    // @return 장소 리스트
    @GetMapping("/list")
    public ResponseEntity<List<LocationListResponse>> getLocationList(@RequestParam(defaultValue = "ad") String sort) {
        
        List<LocationListResponse> locationList = locationService.getAllLocations(sort);
        
        return ResponseEntity.ok(locationList);
    }
} 