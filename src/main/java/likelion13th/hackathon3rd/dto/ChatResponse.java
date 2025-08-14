package likelion13th.hackathon3rd.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ChatResponse {
    private String reply;
    public ChatResponse(String reply) {
        this.reply = reply;
    }
}