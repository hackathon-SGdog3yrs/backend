package likelion13th.hackathon3rd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetDetailResponse {
    
    private Integer id;
    private String name;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime datetime;
    
    private Integer current;
    private Integer maximum;
    private String hostName;
    private String locationName;
    private String locationPicture;
    private List<String> tag;
    private String detail;
    private Boolean isJoined;
}