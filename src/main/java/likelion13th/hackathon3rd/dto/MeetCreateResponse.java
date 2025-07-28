package likelion13th.hackathon3rd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetCreateResponse {
    
    private Boolean success;
    private String code;
    private String message;
    private Data data;
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private Integer id;
    }
    
    public static MeetCreateResponse success(Integer meetId) {
        return MeetCreateResponse.builder()
                .success(true)
                .code("S201")
                .message("모임이 성공적으로 생성되었습니다.")
                .data(Data.builder().id(meetId).build())
                .build();
    }
}