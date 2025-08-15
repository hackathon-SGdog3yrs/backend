package likelion13th.hackathon3rd.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.embedding.Embedding;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.service.OpenAiService;
import likelion13th.hackathon3rd.domain.Location;
import likelion13th.hackathon3rd.domain.Meet;
import likelion13th.hackathon3rd.domain.User;
import likelion13th.hackathon3rd.dto.MeetRecommendResponse;
import likelion13th.hackathon3rd.repository.MeetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetRecommendationService {

    private static final String EMBEDDING_MODEL = "text-embedding-3-small"; // 가성비 모델

    private final UserService userService;
    private final MeetRepository meetRepository;
    private final OpenAiService openAiService; // ✅ Embedding 호출용
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Embedding(코사인 유사도)로 상위 N개 추천
     * - 사용자 키워드: "산책, 건강, 야외" 형태로 합쳐 임베딩
     * - 모임 태그: ["산책","야외","건강"] → "산책, 야외, 건강"으로 합쳐 임베딩
     * - 배치 호출로 비용/지연 최소화 (user 1개 + meet M개 = M+1 입력)
     */
    public List<MeetRecommendResponse> recommendForUser(int userId, int limit) {
        // 1) 유저 키워드 확보
        List<String> userKeywords = userService.getUserKeywords(userId);
        if (userKeywords == null || userKeywords.isEmpty()) return List.of();

        String userText = String.join(", ", normalize(userKeywords)); // "산책, 건강, 야외"

        // 2) 모임 전체 읽고, 각 모임의 태그 문자열 준비
        List<Meet> meets = meetRepository.findAll();
        if (meets.isEmpty()) return List.of();

        List<String> meetTexts = new ArrayList<>(meets.size());
        for (Meet m : meets) {
            List<String> tags = readJsonStringArray(m.getTag()); // ["산책","야외","건강"]
            if (tags.isEmpty()) {
                meetTexts.add(""); // 빈 텍스트 → 유사도 0 처리될 것
            } else {
                meetTexts.add(String.join(", ", normalize(tags)));
            }
        }

        // 3) 임베딩 배치 호출 (입력: [userText, meetText1, meetText2, ...])
        List<String> inputs = new ArrayList<>(1 + meetTexts.size());
        inputs.add(userText);
        inputs.addAll(meetTexts);

        EmbeddingRequest request = EmbeddingRequest.builder()
                .model(EMBEDDING_MODEL)
                .input(inputs)
                .build();

        List<Embedding> data = openAiService.createEmbeddings(request).getData();
        if (data == null || data.size() != inputs.size()) return List.of();

        // 4) 벡터 분리
        List<Double> userVec = data.get(0).getEmbedding();
        // meet i의 벡터는 data.get(i+1)
        List<Scored> scored = new ArrayList<>();
        for (int i = 0; i < meets.size(); i++) {
            Meet meet = meets.get(i);
            List<Double> meetVec = data.get(i + 1).getEmbedding();

            double sim = cosine(userVec, meetVec); // 0~1
            if (sim > 0) {
                List<String> tags = readJsonStringArray(meet.getTag()); // 원문 태그 보존
                scored.add(new Scored(meet, tags, sim));
            }
        }

        // 5) 점수 내림차순, 동점이면 id 내림차순 → 상위 limit
        return scored.stream()
                .sorted(Comparator
                        .comparingDouble(Scored::score).reversed()
                        .thenComparing(s -> s.meet().getId(), Comparator.nullsLast(Comparator.reverseOrder()))
                )
                .limit(limit)
                .map(s -> toResponse(s.meet(), s.tags(), s.score()))
                .collect(Collectors.toList());
    }

    // -------------------- 유틸 --------------------

    private record Scored(Meet meet, List<String> tags, double score) {}

    private MeetRecommendResponse toResponse(Meet m, List<String> tags, double score) {
        User host = m.getHostUser();
        Location loc = m.getMeetLocation();
        List<String> pictures = readJsonStringArray(loc != null ? loc.getPicture() : null);

        return MeetRecommendResponse.builder()
                .id(m.getId())
                .name(m.getName())
                .datetime(m.getDateTime())
                .current(m.getCurrent())
                .maximum(m.getMaximum())
                .hostName(host != null ? host.getName() : null)
                .hostAge(host != null ? host.getAge() : null)
                .hostGender(host != null && host.getGender()!=null ? host.getGender().name() : null)
                .locationName(loc != null ? loc.getName() : null)
                .locationPicture(pictures)
                .tag(tags)
                .intro(m.getIntro())
                .detail(m.getDetail())
                .score(round2(score))
                .build();
    }

    private List<String> readJsonStringArray(String json) {
        try {
            if (json == null || json.isBlank()) return List.of();
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return List.of();
        }
    }

    // 공백/중복 정리 (소문자 통일; 한글 영향 적지만 일관성 유지)
    private List<String> normalize(List<String> list) {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        for (String s : list) {
            if (s == null) continue;
            String t = s.trim();
            if (t.isEmpty()) continue;
            set.add(t.toLowerCase(Locale.ROOT));
        }
        return new ArrayList<>(set);
    }

    // 코사인 유사도 (0~1)
    private double cosine(List<Double> a, List<Double> b) {
        int n = Math.min(a.size(), b.size());
        double dot = 0, na = 0, nb = 0;
        for (int i = 0; i < n; i++) {
            double x = a.get(i), y = b.get(i);
            dot += x * y; na += x * x; nb += y * y;
        }
        if (na == 0 || nb == 0) return 0.0;
        return dot / (Math.sqrt(na) * Math.sqrt(nb));
    }

    private double round2(double x) {
        return Math.round(x * 100.0) / 100.0;
    }
}