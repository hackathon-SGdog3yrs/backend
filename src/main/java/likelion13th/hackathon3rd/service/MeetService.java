package likelion13th.hackathon3rd.service;

import likelion13th.hackathon3rd.domain.Location;
import likelion13th.hackathon3rd.domain.Meet;
import likelion13th.hackathon3rd.domain.User;
import likelion13th.hackathon3rd.dto.MeetCreateRequest;
import likelion13th.hackathon3rd.dto.MeetCreateResponse;
import likelion13th.hackathon3rd.dto.MeetDetailResponse;
import likelion13th.hackathon3rd.dto.MeetJoinRequest;
import likelion13th.hackathon3rd.dto.MeetJoinResponse;
import likelion13th.hackathon3rd.dto.MeetLeaveRequest;
import likelion13th.hackathon3rd.dto.MeetLeaveResponse;
import likelion13th.hackathon3rd.dto.MeetListResponse;
import likelion13th.hackathon3rd.exception.InvalidRequestException;
import likelion13th.hackathon3rd.exception.MeetNotFoundException;
import likelion13th.hackathon3rd.repository.LocationRepository;
import likelion13th.hackathon3rd.repository.MeetRepository;
import likelion13th.hackathon3rd.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetService {

    private final MeetRepository meetRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final ObjectMapper objectMapper;

    // 전체 모임 리스트 조회 (진행 중인 모임만, 현재 참여자 수 내림차순)
    public List<MeetListResponse> getAllAvailableMeets() {
        List<Meet> meets = meetRepository.findAvailableMeetsOrderByCurrentDesc();
        
        return meets.stream()
                .map(this::convertToMeetListResponse)
                .collect(Collectors.toList());
    }

        // 모임 상세 정보 조회
    // @param meetId 모임 ID
    public MeetDetailResponse getMeetDetail(Integer meetId) {
        // 입력값 검증
        if (meetId == null || meetId <= 0) {
            throw new InvalidRequestException("가입되지 않은 계정입니다.");
        }

        Meet meet = meetRepository.findByIdWithDetails(meetId)
                .orElseThrow(() -> new MeetNotFoundException("서버에 문제가 발생했습니다. 잠시후 다시 시도해주세요."));

        // 사용자의 모임 참여 여부 확인 (임시로 false 반환)
        // TODO: 실제 사용자 인증과 참여 관계 구현 필요
        boolean isJoined = false;

        return convertToMeetDetailResponse(meet, isJoined);
    }

    // 새로운 모임 생성
    // @param request 모임 생성 요청 정보
    @Transactional
    public MeetCreateResponse createMeet(MeetCreateRequest request) {
        // 입력값 검증
        validateCreateRequest(request);
        
        // 사용자 조회 (ID로)
        User hostUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new InvalidRequestException("가입되지 않은 계정입니다."));
        
        // 장소 조회 (ID로)
        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new InvalidRequestException("존재하지 않는 장소입니다."));
        
        // 태그를 JSON 문자열로 변환
        String tagJson = convertTagsToJson(request.getTag());
        
        // Meet 엔티티 생성
        Meet meet = Meet.builder()
                .name(request.getName())
                .intro(request.getIntro())
                .dateTime(request.getDatetime())
                .detail(request.getDetail())
                .maximum(request.getMaximum())
                .current(1) // 생성자가 첫 번째 참여자
                .tag(tagJson)
                .hostUser(hostUser)
                .meetLocation(location)
                .build();
        
        // 모임 저장
        Meet savedMeet = meetRepository.save(meet);
        
        return MeetCreateResponse.success(savedMeet.getId());
    }

    // Meet 엔티티를 MeetListResponse DTO로 변환
    private MeetListResponse convertToMeetListResponse(Meet meet) {
        List<String> tags = parseTagsFromJson(meet.getTag());
        List<String> locationPictures = parseTagsFromJson(meet.getMeetLocation().getPicture());
        
        return MeetListResponse.builder()
                .id(meet.getId())
                .name(meet.getName())
                .datetime(meet.getDateTime())
                .current(meet.getCurrent())
                .maximum(meet.getMaximum())
                .hostName(meet.getHostUser().getName())
                .hostAge(meet.getHostUser().getAge())
                .hostGender(meet.getHostUser().getGender().toString())
                .locationName(meet.getMeetLocation().getName())
                .locationPicture(locationPictures)
                .tag(tags)
                .intro(meet.getIntro())
                .detail(meet.getDetail())
                .build();
    }

    // Meet 엔티티를 MeetDetailResponse DTO로 변환
    private MeetDetailResponse convertToMeetDetailResponse(Meet meet, boolean isJoined) {
        List<String> tags = parseTagsFromJson(meet.getTag());
        List<String> locationPictures = parseTagsFromJson(meet.getMeetLocation().getPicture());
        
        return MeetDetailResponse.builder()
                .id(meet.getId())
                .name(meet.getName())
                .datetime(meet.getDateTime())
                .current(meet.getCurrent())
                .maximum(meet.getMaximum())
                .hostName(meet.getHostUser().getName())
                .hostAge(meet.getHostUser().getAge())
                .hostGender(meet.getHostUser().getGender().toString())
                .locationName(meet.getMeetLocation().getName())
                .locationPicture(locationPictures)
                .tag(tags)
                .intro(meet.getIntro())
                .detail(meet.getDetail())
                .isjoined(isJoined)
                .build();
    }

    // 사용자가 해당 모임에 참여했는지 확인
    private boolean checkUserJoinedMeet(Integer userId, Integer meetId) {
        try {
            User user = userRepository.findById(userId).orElse(null);
            Meet meet = meetRepository.findById(meetId).orElse(null);
            
            if (user == null || meet == null) {
                return false;
            }
            
            return user.hasJoinedMeet(meet);
        } catch (Exception e) {
            return false;
        }
    }

    // 모임 생성 요청 검증
    private void validateCreateRequest(MeetCreateRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new InvalidRequestException("모임 이름은 필수입니다.");
        }
        if (request.getDatetime() == null) {
            throw new InvalidRequestException("모임 시간은 필수입니다.");
        }
        if (request.getMaximum() == null || request.getMaximum() <= 0) {
            throw new InvalidRequestException("최대 인원은 1명 이상이어야 합니다.");
        }
        if (request.getDetail() == null || request.getDetail().trim().isEmpty()) {
            throw new InvalidRequestException("모임 설명은 필수입니다.");
        }
        if (request.getLocationId() == null) {
            throw new InvalidRequestException("장소 ID는 필수입니다.");
        }
        if (request.getUserId() == null) {
            throw new InvalidRequestException("사용자 ID는 필수입니다.");
        }
        if (request.getIntro() == null || request.getIntro().trim().isEmpty()) {
            throw new InvalidRequestException("모임 한줄소개는 필수입니다.");
        }
    }

    // List<String> 태그를 JSON 문자열로 변환
    private String convertTagsToJson(List<String> tags) {
        try {
            if (tags == null || tags.isEmpty()) {
                return "[]";
            }
            return objectMapper.writeValueAsString(tags);
        } catch (Exception e) {
            // JSON 변환 실패 시 빈 배열 반환
            return "[]";
        }
    }

        // 모임 검색
    public List<MeetListResponse> searchMeets(String query, String field) {
        List<Meet> searchResults = new ArrayList<>();
        
        switch (field.toLowerCase()) {
            case "name":
                searchResults = meetRepository.findByNameContainingIgnoreCase(query);
                break;
            case "detail":
                searchResults = meetRepository.findByDetailContainingIgnoreCase(query);
                break;
            case "tag":
                searchResults = meetRepository.findByTagContainingIgnoreCase(query);
                break;
            case "intro":
                searchResults = meetRepository.findByIntroContainingIgnoreCase(query);
                break;
            case "all":
                searchResults = meetRepository.findByAllFieldsContainingIgnoreCase(query);
                break;
            default:
                throw new InvalidRequestException("지원하지 않는 검색 필드입니다. (name, detail, tag, intro, all 중 선택)");
        }
        
        return searchResults.stream()
                .map(this::convertToMeetListResponse)
                .collect(Collectors.toList());
    }

    // 모임 참여하기
    // @param request 모임 참여 요청 정보
    @Transactional
    public MeetJoinResponse joinMeet(MeetJoinRequest request) {
        // 입력값 검증
        validateJoinRequest(request);
        
        // 사용자 조회
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new InvalidRequestException("존재하지 않는 사용자입니다."));
        
        // 모임 조회
        Meet meet = meetRepository.findById(request.getMeetId())
                .orElseThrow(() -> new InvalidRequestException("존재하지 않는 모임입니다."));
        
        // 모임 참여 가능 여부 확인
        if (meet.getCurrent() >= meet.getMaximum()) {
            return MeetJoinResponse.failure("E409", "모임 최대 인원을 초과했습니다.");
        }
        
        // 이미 참여했는지 확인
        if (user.hasJoinedMeet(meet)) {
            return MeetJoinResponse.failure("E409", "이미 참여한 모임입니다.");
        }
        
        // 모임 참여자 수 증가
        meet.setCurrent(meet.getCurrent() + 1);
        Meet updatedMeet = meetRepository.save(meet);
        
        // 사용자의 참여 모임 목록에 추가
        user.joinMeet(updatedMeet);
        userRepository.save(user);
        
        return MeetJoinResponse.success(updatedMeet.getId(), user.getId(), updatedMeet.getCurrent());
    }

    // 모임 나가기
    // @param request 모임 나가기 요청 정보
    @Transactional
    public MeetLeaveResponse leaveMeet(MeetLeaveRequest request) {
        // 입력값 검증
        validateLeaveRequest(request);
        
        // 사용자 조회
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new InvalidRequestException("존재하지 않는 사용자입니다."));
        
        // 모임 조회
        Meet meet = meetRepository.findById(request.getMeetId())
                .orElseThrow(() -> new InvalidRequestException("존재하지 않는 모임입니다."));
        
        // 참여하지 않은 모임인지 확인
        if (!user.hasJoinedMeet(meet)) {
            return MeetLeaveResponse.failure("E409", "참여하지 않은 모임입니다.");
        }
        
        // 모임 참여자 수 감소
        meet.setCurrent(meet.getCurrent() - 1);
        Meet updatedMeet = meetRepository.save(meet);
        
        // 사용자의 참여 모임 목록에서 제거
        user.leaveMeet(updatedMeet);
        userRepository.save(user);
        
        return MeetLeaveResponse.success(updatedMeet.getId(), user.getId(), updatedMeet.getCurrent());
    }

    // 모임 참여 요청 검증
    private void validateJoinRequest(MeetJoinRequest request) {
        if (request.getUserId() == null) {
            throw new InvalidRequestException("사용자 ID는 필수입니다.");
        }
        if (request.getMeetId() == null) {
            throw new InvalidRequestException("모임 ID는 필수입니다.");
        }
    }

    // 모임 나가기 요청 검증
    private void validateLeaveRequest(MeetLeaveRequest request) {
        if (request.getUserId() == null) {
            throw new InvalidRequestException("사용자 ID는 필수입니다.");
        }
        if (request.getMeetId() == null) {
            throw new InvalidRequestException("모임 ID는 필수입니다.");
        }
    }

    // JSON 형태의 태그 문자열을 List<String>으로 변환
    private List<String> parseTagsFromJson(String tagJson) {
        try {
            if (tagJson == null || tagJson.trim().isEmpty() || "[]".equals(tagJson.trim())) {
                return List.of();
            }
            return objectMapper.readValue(tagJson, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            // JSON 파싱 실패 시 빈 리스트 반환
            return List.of();
        }
    }
}