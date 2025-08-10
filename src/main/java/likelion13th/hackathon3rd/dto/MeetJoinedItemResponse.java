package likelion13th.hackathon3rd.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class MeetJoinedItemResponse {
    private int id;
    private String name;
    private LocalDateTime datetime;
    private int current;
    private int maximum;
    private String locationName;
    private String locationPicture;
    private List<String> tag;
    private String detail;
    private String hostname;
}