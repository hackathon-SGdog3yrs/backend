package likelion13th.hackathon3rd.controller;

import likelion13th.hackathon3rd.dto.ApiResponse;
import likelion13th.hackathon3rd.service.ChatService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ApiResponse<Map<String, String>> send(
            @RequestParam Integer userId,
            @RequestBody ChatRequest req
    ) {
        String answer = chatService.handleUserMessage(userId, req.getUserMessage());
        return ApiResponse.ok("success", java.util.Collections.singletonMap("answer", answer));    }

    @Data
    public static class ChatRequest {
        private String userMessage;
    }
}