package likelion13th.hackathon3rd.config;

import likelion13th.hackathon3rd.domain.Location;
import likelion13th.hackathon3rd.domain.Meet;
import likelion13th.hackathon3rd.domain.User;
import likelion13th.hackathon3rd.repository.LocationRepository;
import likelion13th.hackathon3rd.repository.MeetRepository;
import likelion13th.hackathon3rd.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
// 테스트 위한 것
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final MeetRepository meetRepository;

    @Override
    public void run(String... args) throws Exception {
        // 기존 데이터가 있으면 초기화하지 않음
        if (userRepository.count() > 0) {
            return;
        }

        // 사용자 데이터 추가
        User user1 = User.builder()
                .name("김은혜")
                .gender(User.Gender.F)
                .age(65)
                .keyword("[\"산책\",\"건강\",\"야외\"]")
                .build();
        
        User user2 = User.builder()
                .name("이정호")
                .gender(User.Gender.M)
                .age(70)
                .keyword("[\"게임\",\"실내\",\"친목\"]")
                .build();

        User user3 = User.builder()
                .name("박미영")
                .gender(User.Gender.F)
                .age(68)
                .keyword("[\"요리\",\"독서\",\"차\"]")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        // 장소 데이터 추가
        Location location1 = Location.builder()
                .name("마포 실버공원")
                .address("서울시 마포구 상수동")
                .picture("https://example.com/mapo-park.jpg")
                .advertisement(false)
                .build();

        Location location2 = Location.builder()
                .name("마포 실버센터")
                .address("서울시 마포구 합정동")
                .picture("https://example.com/mapo-center.jpg")
                .advertisement(false)
                .build();

        Location location3 = Location.builder()
                .name("홍대 커피숍")
                .address("서울시 마포구 홍익로")
                .picture("https://example.com/hongdae-cafe.jpg")
                .advertisement(true)
                .build();

        locationRepository.save(location1);
        locationRepository.save(location2);
        locationRepository.save(location3);

        // 모임 데이터 추가
        Meet meet1 = Meet.builder()
                .name("실버 산책 모임")
                .email("kimeunhye@temp.com")
                .dateTime(LocalDateTime.of(2025, 8, 10, 9, 0))
                .detail("매주 일요일 아침에 마포 실버공원에서 가볍게 산책합니다. 건강한 하루를 시작해요!")
                .maximum(10)
                .current(3)
                .tag("[\"산책\",\"야외\",\"건강\"]")
                .hostUser(user1)
                .meetLocation(location1)
                .build();

        Meet meet2 = Meet.builder()
                .name("실버 게임 클럽")
                .email("leejungho@temp.com")
                .dateTime(LocalDateTime.of(2025, 8, 12, 14, 0))
                .detail("보드게임과 카드게임으로 즐거운 시간을 보내요. 초보자도 환영합니다!")
                .maximum(8)
                .current(5)
                .tag("[\"게임\",\"실내\",\"친목\"]")
                .hostUser(user2)
                .meetLocation(location2)
                .build();

        Meet meet3 = Meet.builder()
                .name("홍대 차 모임")
                .email("parkmiyoung@temp.com")
                .dateTime(LocalDateTime.of(2025, 8, 15, 15, 30))
                .detail("다양한 차를 마시며 독서와 담소를 나누는 여유로운 모임입니다.")
                .maximum(6)
                .current(2)
                .tag("[\"차\",\"독서\",\"힐링\"]")
                .hostUser(user3)
                .meetLocation(location3)
                .build();

        meetRepository.save(meet1);
        meetRepository.save(meet2);
        meetRepository.save(meet3);

        System.out.println("테스트 데이터 초기화");
        System.out.println("사용자 3명, 장소 3곳, 모임 3개가 추가되었습니다.");
    }
}