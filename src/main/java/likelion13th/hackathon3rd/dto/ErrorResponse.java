package likelion13th.hackathon3rd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    
    private Integer status;
    private String error;
    private String message;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    private String path;
    
    public static ErrorResponse of(Integer status, String error, String message, String path) {
        return ErrorResponse.builder()
                .status(status)
                .error(error)
                .message(message)
                .timestamp(LocalDateTime.now())
                .path(path)
                .build();
    }
}