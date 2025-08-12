package likelion13th.hackathon3rd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetJoinResponse {
    
    private Boolean success;
    private String code;
    private String message;
    private Data data;
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private Integer meetId;
        private Integer userId;
        private Integer current;
        private Boolean isJoined;
    }
    
    public static MeetJoinResponse success(Integer meetId, Integer userId, Integer current) {
        return MeetJoinResponse.builder()
                .success(true)
                .code("S200")
                .message("모임 참여 완료")
                .data(Data.builder()
                        .meetId(meetId)
                        .userId(userId)
                        .current(current)
                        .isJoined(true)
                        .build())
                .build();
    }
    
    public static MeetJoinResponse failure(String code, String message) {
        return MeetJoinResponse.builder()
                .success(false)
                .code(code)
                .message(message)
                .build();
    }
} 