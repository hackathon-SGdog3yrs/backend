package likelion13th.hackathon3rd.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import likelion13th.hackathon3rd.domain.Meet;
import likelion13th.hackathon3rd.dto.MeetCreatedItemResponse;
import likelion13th.hackathon3rd.repository.MeetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetQueryService {

    private final MeetRepository meetRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<MeetCreatedItemResponse> getCreatedList(Integer userId, String sortBy, String order) {

        String sortProperty = switch (sortBy == null ? "dateTime" : sortBy) {
            case "name", "maximum", "current", "dateTime" -> sortBy;
            default -> "dateTime";
        };

        Sort.Direction direction = "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortProperty);

        List<Meet> meets = meetRepository.findByHostUser_Id(userId, sort);

        return meets.stream()
                .map(m -> MeetCreatedItemResponse.builder()
                        .id(m.getId())
                        .name(m.getName())
                        .dateTime(m.getDateTime()) // 필드명 dateTime
                        .current(m.getCurrent())
                        .maximum(m.getMaximum())
                        .locationName(m.getMeetLocation() != null ? m.getMeetLocation().getName() : null)
                        .locationPicture(m.getMeetLocation() != null ? m.getMeetLocation().getPicture() : null)
                        .tag(parseTag(m.getTag()))
                        .detail(m.getDetail())
                        .build())
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
}