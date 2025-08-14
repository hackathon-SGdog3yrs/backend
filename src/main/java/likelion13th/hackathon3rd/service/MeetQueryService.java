package likelion13th.hackathon3rd.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import likelion13th.hackathon3rd.domain.Meet;
import likelion13th.hackathon3rd.domain.User;
import likelion13th.hackathon3rd.dto.MeetListResponse;
import likelion13th.hackathon3rd.repository.MeetRepository;
import likelion13th.hackathon3rd.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MeetQueryService {

    private final MeetRepository meetRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<MeetListResponse> getCreatedList(Integer userId, String sortBy, String order) {
        String sortProperty = switch (sortBy == null ? "dateTime" : sortBy) {
            case "name", "maximum", "current", "dateTime" -> sortBy;
            default -> "dateTime";
        };

        Sort.Direction direction = "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortProperty);

        List<Meet> meets = meetRepository.findByHostUser_Id(userId, sort);

        return meets.stream()
                .map(this::toMeetListResponse)
                .toList();
    }

    public List<MeetListResponse> getJoinedList(Integer userId, String sortBy, String order) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        List<Meet> joined = new ArrayList<>(Optional.ofNullable(user.getJoinedMeets()).orElseGet(List::of));

        String sortKey = switch (sortBy == null ? "dateTime" : sortBy) {
            case "name", "maximum", "current", "dateTime" -> sortBy;
            default -> "dateTime";
        };
        boolean asc = "asc".equalsIgnoreCase(order);

        Comparator<Meet> comp = switch (sortKey) {
            case "name" -> Comparator.comparing(Meet::getName, Comparator.nullsLast(String::compareToIgnoreCase));
            case "maximum" -> Comparator.comparingInt(Meet::getMaximum);
            case "current" -> Comparator.comparingInt(Meet::getCurrent);
            default -> Comparator.comparing(Meet::getDateTime, Comparator.nullsLast(Comparator.naturalOrder()));
        };
        if (!asc) comp = comp.reversed();
        joined.sort(comp);

        return joined.stream()
                .map(this::toMeetListResponse)
                .toList();
    }

    private List<String> parseTag(String json) {
        if (json == null || json.isBlank()) return Collections.emptyList();
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private MeetListResponse toMeetListResponse(Meet m) {
        return MeetListResponse.builder()
                .id(m.getId())
                .name(m.getName())
                .datetime(m.getDateTime())
                .current(m.getCurrent())
                .maximum(m.getMaximum())
                .hostName(m.getHostUser() != null ? m.getHostUser().getName() : null)
                .hostAge(m.getHostUser() != null ? m.getHostUser().getAge() : null)
                .hostGender(m.getHostUser() != null && m.getHostUser().getGender() != null
                        ? m.getHostUser().getGender().name()
                        : null)
                .locationName(m.getMeetLocation() != null ? m.getMeetLocation().getName() : null)
                .locationPicture(
                        m.getMeetLocation() != null && m.getMeetLocation().getPicture() != null
                                ? List.of(m.getMeetLocation().getPicture())
                                : Collections.emptyList()
                )                .tag(parseTag(m.getTag()))
                .intro(m.getIntro())
                .detail(m.getDetail())
                .build();
    }
}