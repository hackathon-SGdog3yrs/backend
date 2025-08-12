package likelion13th.hackathon3rd.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MeetJoinRequest {
    
    private Integer userId;
    private Integer meetId;
} 