package likelion13th.hackathon3rd.service;

import likelion13th.hackathon3rd.domain.Location;
import likelion13th.hackathon3rd.dto.LocationListResponse;
import likelion13th.hackathon3rd.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocationService {

    private final LocationRepository locationRepository;
    private final ObjectMapper objectMapper;

    // 모든 장소 조회 (광고 우선 정렬)
    // @param sort 정렬 기준 (ad: 광고 우선)
    public List<LocationListResponse> getAllLocations(String sort) {
        List<Location> locations;
        
        if ("ad".equals(sort)) {
            // 광고하는 장소를 우선으로 정렬
            locations = locationRepository.findAllOrderByAdvertisementDesc();
        } else {
            // 기본 정렬 (ID 순)
            locations = locationRepository.findAll();
        }

        return locations.stream()
                .map(this::convertToLocationListResponse)
                .collect(Collectors.toList());
    }

    // Location 엔티티를 LocationListResponse DTO로 변환
    private LocationListResponse convertToLocationListResponse(Location location) {
        List<String> pictures = parseTagsFromJson(location.getPicture());

        return LocationListResponse.builder()
                .id(location.getId())
                .name(location.getName())
                .picture(pictures)
                .address(location.getAddress())
                .advertisement(location.getAdvertisement())
                .build();
    }

    // JSON 형태의 문자열을 List<String>으로 변환
    private List<String> parseTagsFromJson(String jsonString) {
        try {
            if (jsonString == null || jsonString.trim().isEmpty() || "[]".equals(jsonString.trim())) {
                return List.of();
            }
            return objectMapper.readValue(jsonString, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            // JSON 파싱 실패 시 빈 리스트 반환
            return List.of();
        }
    }
} 