package likelion13th.hackathon3rd.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class MeetCreatedItemResponse {
    private int id;
    private String name;
    private LocalDateTime dateTime;
    private int current;
    private int maximum;
    private String locationName;
    private String locationPicture;
    private List<String> tag;
    private String detail;
}