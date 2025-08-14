package likelion13th.hackathon3rd.controller;

import likelion13th.hackathon3rd.dto.ChatRequest;
import likelion13th.hackathon3rd.dto.ChatResponse;
import likelion13th.hackathon3rd.dto.MeetRecommendResponse;
import likelion13th.hackathon3rd.service.MeetRecommendationService;
import likelion13th.hackathon3rd.service.OpenAIChatService;
import likelion13th.hackathon3rd.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class OpenAIController {
    private final OpenAIChatService openAiChatService;
    private final UserService userService;
    private final MeetRecommendationService recommendationService;

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request){
        String reply = openAiChatService.getChatResponse(request);
        return ResponseEntity.ok(new ChatResponse(reply));
    }

    @GetMapping("/{id}/recommend")
    public ResponseEntity<?> recommend(
            @PathVariable("id") int userId,
            @RequestParam(defaultValue = "3") int limit
    ) {
        List<String> keywords = userService.getUserKeywords(userId);
        if (keywords.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "사용자에게 저장된 키워드가 없습니다. 챗봇과 대화를 먼저 진행해주세요."
            ));
        }

        return ResponseEntity.ok(recommendationService.recommendForUser(userId, limit));
    }

}
