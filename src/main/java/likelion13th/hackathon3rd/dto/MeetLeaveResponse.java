package likelion13th.hackathon3rd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetLeaveResponse {
    
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
    
    public static MeetLeaveResponse success(Integer meetId, Integer userId, Integer current) {
        return MeetLeaveResponse.builder()
                .success(true)
                .code("S200")
                .message("모임 참여 취소 완료")
                .data(Data.builder()
                        .meetId(meetId)
                        .userId(userId)
                        .current(current)
                        .isJoined(false)
                        .build())
                .build();
    }
    
    public static MeetLeaveResponse failure(String code, String message) {
        return MeetLeaveResponse.builder()
                .success(false)
                .code(code)
                .message(message)
                .build();
    }
} 