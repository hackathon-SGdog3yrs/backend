package likelion13th.hackathon3rd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetRecommendResponse {

    private Integer id;
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime datetime;

    private Integer current;
    private Integer maximum;

    // 호스트/장소/태그/소개
    private String hostName;
    private Integer hostAge;
    private String hostGender;
    private String locationName;
    private List<String> locationPicture;
    private List<String> tag;
    private String intro;
    private String detail;

    /** 유사도 점수 (0.0 ~ 1.0), 예: 0.67 */
    private Double score;
}