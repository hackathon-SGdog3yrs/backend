package likelion13th.hackathon3rd.controller;

import likelion13th.hackathon3rd.dto.ChatRequest;
import likelion13th.hackathon3rd.dto.ChatResponse;
import likelion13th.hackathon3rd.service.OpenAIChatService;
import likelion13th.hackathon3rd.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class OpenAIController {
    private final OpenAIChatService openAiChatService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request){
        String reply = openAiChatService.getChatResponse(request); // ✅ 여기서 자동 저장까지 처리됨
        return ResponseEntity.ok(new ChatResponse(reply));
    }

}
