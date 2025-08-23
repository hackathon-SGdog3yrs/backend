package likelion13th.hackathon3rd.config;

import likelion13th.hackathon3rd.domain.Location;
import likelion13th.hackathon3rd.domain.Meet;
import likelion13th.hackathon3rd.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import likelion13th.hackathon3rd.repository.LocationRepository;
import likelion13th.hackathon3rd.repository.MeetRepository;
import likelion13th.hackathon3rd.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final MeetRepository meetRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) {
        // 기존 데이터가 있으면 초기화하지 않음
        if (userRepository.count() > 0) {
            return;
        }

        // 사용자 데이터 추가
        User user1 = User.builder()
                .name("대흥동 호랑이")
                .gender(User.Gender.F)
                .age(65)
                .keyword("[\"산책\",\"건강\",\"야외\"]")
                .build();
        
        User user2 = User.builder()
                .name("장미같은오후")
                .gender(User.Gender.M)
                .age(63)
                .keyword("[\"게임\",\"실내\",\"친목\"]")
                .build();

        User user3 = User.builder()
                .name("도덕경풍")
                .gender(User.Gender.F)
                .age(52)
                .keyword("[\"요리\",\"독서\",\"차\"]")
                .build();

        User user4 = User.builder()
                .name("장기도사")
                .gender(User.Gender.M)
                .age(66)
                .keyword("[\"산책\",\"야외\",\"건강\"]")
                .build();

        User user5 = User.builder()
                .name("황혼빛여정")
                .gender(User.Gender.M)
                .age(70)
                .keyword("[\"독서\",\"문화\",\"여행\"]")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);

        // 장소 데이터 추가
        Location location1 = Location.builder()
                .name("망원 한강공원")
                .address("서울 마포구 마포나루길 467")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/1_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/1_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/1_c.PNG")))
                .advertisement(true)
                .build();

        Location location2 = Location.builder()
                .name("마포구민 체육센터")
                .address("서울 마포구 월드컵로 25길 190")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/2_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/2_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/2_c.PNG")))
                .advertisement(false)
                .build();

        Location location3 = Location.builder()
                .name("을밀대 평양냉면")
                .address("서울 마포구 숭문길 24")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/3_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/3_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/3_c.PNG")))
                .advertisement(true)
                .build();

        Location location4 = Location.builder()
                .name("마포구립 서강도서관")
                .address("서울 마포구 독막로 165 서강동주민센터 5층")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/4_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/4_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/4_c.PNG")))
                .advertisement(false)
                .build();

        Location location5 = Location.builder()
                .name("서울 월드컵경기장")
                .address("서울 마포구 월드컵로 240")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/5_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/5_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/5_c.PNG")))
                .advertisement(true)
                .build();

        Location location6 = Location.builder()
                .name("매봉산")
                .address("서울 마포구 상암동")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/6_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/6_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/6_c.PNG")))
                .advertisement(false)
                .build();

        Location location7 = Location.builder()
                .name("코코로카라")
                .address("서울 마포구 연남로1길 41")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/7_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/7_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/7_c.PNG")))
                .advertisement(true)
                .build();

        Location location8 = Location.builder()
                .name("우리바다수산")
                .address("서울 마포구 월드컵로 102")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/8_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/8_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/8_c.PNG")))
                .advertisement(false)
                .build();

        Location location9 = Location.builder()
                .name("우이락 망원본점")
                .address("서울 마포구 포은로8길 22")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/9_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/9_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/9_c.PNG")))
                .advertisement(true)
                .build();

        Location location10 = Location.builder()
                .name("마포 여성 동행 센터")
                .address("서울 마포구 대흥로 122")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/10_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/10_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/10_c.PNG")))
                .advertisement(false)
                .build();

        Location location11 = Location.builder()
                .name("수저가")
                .address("서울 마포구 광성로4길 10 1층")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/11_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/11_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/11_c.PNG")))
                .advertisement(true)
                .build();

        Location location12 = Location.builder()
                .name("노고산 숯불갈비")
                .address("서울 마포구 백범로 14 1층")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/12_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/12_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/12_c.PNG")))
                .advertisement(false)
                .build();

        Location location13 = Location.builder()
                .name("홍원")
                .address("서울 마포구 백범로 23 케이터틀 지하1층 E호")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/13_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/13_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/13_c.PNG")))
                .advertisement(true)
                .build();

        Location location14 = Location.builder()
                .name("청석골감자탕순대국")
                .address("서울 마포구 백범로 13")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/14_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/14_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/14_c.PNG")))
                .advertisement(false)
                .build();

        Location location15 = Location.builder()
                .name("짜장상회")
                .address("서울 마포구 백범로 52")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/15_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/15_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/15_c.PNG")))
                .advertisement(true)
                .build();

        Location location16 = Location.builder()
                .name("정든그릇")
                .address("서울 마포구 독막로 239 1층 101호")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/16_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/16_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/16_c.PNG")))
                .advertisement(false)
                .build();

        Location location17 = Location.builder()
                .name("마포리 1987")
                .address("서울 마포구 독막로38길 3 1층")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/17_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/17_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/17_c.PNG")))
                .advertisement(true)
                .build();

        Location location18 = Location.builder()
                .name("달쏘")
                .address("서울 마포구 서강대길 40 마포자이2차아파트 상가동 104호")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/18_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/18_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/18_c.PNG")))
                .advertisement(false)
                .build();

        Location location19 = Location.builder()
                .name("술탄커피")
                .address("서울 마포구 백범로 89-4 1층")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/19_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/19_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/19_c.PNG")))
                .advertisement(true)
                .build();

        Location location20 = Location.builder()
                .name("홉커피")
                .address("서울 마포구 백범로16길 8-5 1층")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/20_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/20_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/20_c.PNG")))
                .advertisement(false)
                .build();

        Location location21 = Location.builder()
                .name("다인 찻집")
                .address("서울 마포구 신촌로 182 2층")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/21_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/21_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/21_c.PNG")))
                .advertisement(true)
                .build();

        Location location22 = Location.builder()
                .name("경의선숲길")
                .address("서울 마포구 연남동")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/22_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/22_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/22_c.PNG")))
                .advertisement(false)
                .build();

        Location location23 = Location.builder()
                .name("경의선공원")
                .address("서울 마포구 대흥동 325-82")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/23_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/23_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/23_c.PNG")))
                .advertisement(true)
                .build();

        Location location24 = Location.builder()
                .name("카페청록 샵01787C")
                .address("서울 마포구 대흥로 110 1층")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/24_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/24_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/24_c.PNG")))
                .advertisement(false)
                .build();

        Location location25 = Location.builder()
                .name("나이스워크투데이")
                .address("서울 마포구 백범로10길 26 2층")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/25_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/25_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/25_c.PNG")))
                .advertisement(true)
                .build();

        Location location26 = Location.builder()
                .name("옥정")
                .address("서울 마포구 신수로 106")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/26_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/26_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/26_c.PNG")))
                .advertisement(false)
                .build();

        Location location27 = Location.builder()
                .name("모어브라운")
                .address("서울 마포구 대흥로24길 24 A동102호")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/27_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/27_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/27_c.PNG")))
                .advertisement(true)
                .build();

        Location location28 = Location.builder()
                .name("정정")
                .address("서울 마포구 백범로24길 27")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/28_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/28_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/28_c.PNG")))
                .advertisement(false)
                .build();

        Location location29 = Location.builder()
                .name("나비카페")
                .address("서울 마포구 고산2길 6 1층")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/29_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/29_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/29_c.PNG")))
                .advertisement(true)
                .build();

        Location location30 = Location.builder()
                .name("자무쉬")
                .address("서울 마포구 백범로 40-2 1층")
                .picture(convertTagsToJson(List.of("https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/30_a.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/30_b.PNG", "https://raw.githubusercontent.com/hackathon-SGdog3yrs/backend/main/src/main/resources/images/30_c.PNG")))
                .advertisement(false)
                .build();

        locationRepository.save(location1);
        locationRepository.save(location2);
        locationRepository.save(location3);
        locationRepository.save(location4);
        locationRepository.save(location5);
        locationRepository.save(location6);
        locationRepository.save(location7);
        locationRepository.save(location8);
        locationRepository.save(location9);
        locationRepository.save(location10);
        locationRepository.save(location11);
        locationRepository.save(location12);
        locationRepository.save(location13);
        locationRepository.save(location14);
        locationRepository.save(location15);
        locationRepository.save(location16);
        locationRepository.save(location17);
        locationRepository.save(location18);
        locationRepository.save(location19);
        locationRepository.save(location20);
        locationRepository.save(location21);
        locationRepository.save(location22);
        locationRepository.save(location23);
        locationRepository.save(location24);
        locationRepository.save(location25);
        locationRepository.save(location26);
        locationRepository.save(location27);
        locationRepository.save(location28);
        locationRepository.save(location29);
        locationRepository.save(location30);

        // 모임 데이터 추가
        Meet meet1 = Meet.builder()
                .name("평양냉면 번개")
                .intro("더위가 가기 전, 을밀대에서 시원하게 냉면 한 그릇!")
                .dateTime(LocalDateTime.of(2025, 8, 30, 18, 30))
                .detail("8월의 마지막 토요일 저녁, 을밀대 본점에서 평양냉면 한 그릇 하실 분들을 모십니다. 6시 30분에 을밀대 앞에서 만나요. '마실'에서 제공하는 할인쿠폰 사용 예정입니다.")
                .maximum(6)
                .current(3)
                .tag(convertTagsToJson(List.of("평양냉면", "저녁식사")))
                .hostUser(user1)
                .meetLocation(location3)
                .build();

        Meet meet2 = Meet.builder()
                .name("FC서울 vs 안양FC 축구 단체관람")
                .intro("서울월드컵경기장, 100명 단체석에서 함께 직관합니다.")
                .dateTime(LocalDateTime.of(2025, 8, 31, 19, 0))
                .detail("축구 팬 여러분, 주목하세요! 오는 일요일, 서울월드컵경기장에서 펼쳐지는 FC서울과 안양FC의 빅매치를 함께 즐길 100인 원정대를 모집합니다. 미리 단체 예약을 해두었으니, 이제 여러분은 편하게 오셔서 함께 응원하고 즐기시면 됩니다!")
                .maximum(100)
                .current(68)
                .tag(convertTagsToJson(List.of("축구", "k리그", "fc서울", "fc안양", "스포츠")))
                .hostUser(user2)
                .meetLocation(location5)
                .build();

        Meet meet3 = Meet.builder()
                .name("서강도서관 독서모임")
                .intro("책 읽기 좋아하는 사람, 함께 책 읽고 이야기 나눠요!")
                .dateTime(LocalDateTime.of(2025, 9, 3, 13, 0))
                .detail("안녕하세요, 서강도서관에서 함께 책을 읽고 생각을 나누실 분들을 찾습니다. 혼자 읽기 어려웠던 책, 읽고 나서 함께 이야기하고 싶었던 책이 있으신가요? 서로 다른 관점과 생각을 공유하며 책 읽는 즐거움을 더하고, 새로운 사람들과 만나 소통하는 시간을 가져봐요. 어떤 책을 읽을지부터 함께 정하고, 부담 없이 편안한 분위기 속에서 진행되니 책을 좋아하는 분이라면 누구나 환영합니다.")
                .maximum(10)
                .current(3)
                .tag(convertTagsToJson(List.of("독서", "도서관")))
                .hostUser(user3)
                .meetLocation(location4)
                .build();

        Meet meet4 = Meet.builder()
                .name("가을 제철 전어회 맛보기")
                .intro("망원역 부근 '우리바다수산'에서 전어회를 함께 먹어요")
                .dateTime(LocalDateTime.of(2025, 9, 5, 18, 0))
                .detail("가을 제철을 맞은 맛있는 전어를 우리바다수산에서 함께 맛보려 합니다. 좋은 분들과 함께 식사하며 정겨운 이야기를 나누는 소박한 자리입니다. 맛있는 음식과 좋은 만남을 원하시는 분들의 신청을 기다립니다.")
                .maximum(8)
                .current(5)
                .tag(convertTagsToJson(List.of("전어회", "맛집", "저녁식사")))
                .hostUser(user4)
                .meetLocation(location8)
                .build();

        Meet meet5 = Meet.builder()
                .name("매봉산 등산 모임")
                .intro("매봉산에 오르며 함께 가을을 만끽할 분들을 찾습니다.")
                .dateTime(LocalDateTime.of(2025, 9, 8, 10, 0))
                .detail("매봉산은 가볍게 오르기 좋아 등산 초보자도 부담 없이 즐길 수 있는 아름다운 산입니다. 이번 등산에서는 가을의 정취를 느끼며 함께 건강도 챙기는 시간을 가지려 합니다. 정상에서 잠시 쉬며 준비해온 간식과 물을 나누고, 서로 좋은 이야기도 나누며 즐거운 시간을 보내려 합니다. 등산 후에는 근처 맛집에서 간단한 식사도 함께할 예정이니 많은 참여 바랍니다.")
                .maximum(10)
                .current(6)
                .tag(convertTagsToJson(List.of("매봉산", "등산", "운동", "건강")))
                .hostUser(user5)
                .meetLocation(location6)
                .build();

        Meet meet6 = Meet.builder()
                .name("정든 점심")
                .intro("토요일 낮, 소박하게 밥 한 끼.")
                .dateTime(LocalDateTime.of(2025, 9, 6, 12, 0))
                .detail("토요일 점심에 정든그릇에서 모여 간단히 식사하려고 합니다~~. 12시에 가게 앞에서 만나서 들어가요. 특별한 활동은 없고, 그냥 같이 밥 먹으면서 근황이나 가벼운 이야기를 나누는 자리입니다. 부담 없이 참여하시면 됩니다.")
                .maximum(4)
                .current(2)
                .tag(convertTagsToJson(List.of("점심", "식사", "대화")))
                .hostUser(user1)
                .meetLocation(location16)
                .build();

        Meet meet7 = Meet.builder()
                .name("고기좋아요")
                .intro("일요일 저녁에 숯불향 가득 느껴 봅시다!")
                .dateTime(LocalDateTime.of(2025, 9, 7, 18, 30))
                .detail("일요일 저녁 6시 30분, 노고산 숯불갈비에서 모여 고기 먹으면서 저녁을 보냅니다. 처음 보시는 분들도 편하게 참여하셔서 같이 식사하고 이야기 나누시면 좋겠습니다. 식사 후 원하시면 근처에서 가볍게 차 마시며 이어갈 수도 있습니다.^^.")
                .maximum(8)
                .current(3)
                .tag(convertTagsToJson(List.of("고기", "식사", "숯불", "친목", "대화")))
                .hostUser(user2)
                .meetLocation(location12)
                .build();

        Meet meet8 = Meet.builder()
                .name("달콤달콤")
                .intro("피곤한 오후에 달콤한디저트와 함께 같이 쉬어가요.")
                .dateTime(LocalDateTime.of(2025, 9, 10, 15, 0))
                .detail("수요일 오후 3시, 달쏘에서 만납니다. 디저트와 음료를 주문하고 같이 앉아 수다 떨면서 쉬는 자리예요. 집안일 하다가 혹은 공부 하다가 잠깐 쉬고 싶으신 분들 오시면 딱 맞습니다. 대략 1~2시간 정도 머물 예정이에요. 총총,, @>—,—'——")
                .maximum(5)
                .current(1)
                .tag(convertTagsToJson(List.of("카페", "디저트", "오후")))
                .hostUser(user3)
                .meetLocation(location18)
                .build();

        Meet meet9 = Meet.builder()
                .name("금요일 저녁 커피 한잔")
                .intro("일주일 마무리는 커피와 수다로~~ 약간의 알코올ㅋ?")
                .dateTime(LocalDateTime.of(2025, 9, 12, 19, 0))
                .detail("금요일 저녁 7시에 술탄커피에서 모여 하루를 마무리하려 합니다 ~ ! , , 커피나 차 마시면서 자유롭게 얘기 나누고 , 한 주 동안 있었던 일들도 편하게 공유해요 . . 늦게까지 있진 않고 1~2시간 정도 가볍게 모일 예정입니다 . . . . ^^ . .")
                .maximum(6)
                .current(3)
                .tag(convertTagsToJson(List.of("카페", "저녁", "수다")))
                .hostUser(user4)
                .meetLocation(location19)
                .build();

        Meet meet10 = Meet.builder()
                .name("주말 모닝 커피 모임")
                .intro("일요일 아침을 커피로 시작 하실 분을 구합니다~ ^^;;")
                .dateTime(LocalDateTime.of(2025, 9, 14, 9, 30))
                .detail("일요일 오전 9시 30분, \"\" 홉커피 \"\" 에서 만나 커피를 함께 마십니다. 이른 시간에 일어나서 주말을 상쾌하게 시작하고 싶은 분들 환영이에요. 커피 마시고 간단히 이야기 나누다가, 원하시면 끝나고 근처에서 점심으로 이어가도 괜찮습니다 , ,")
                .maximum(3)
                .current(2)
                .tag(convertTagsToJson(List.of("아침", "커피", "주말")))
                .hostUser(user5)
                .meetLocation(location20)
                .build();

        Meet meet11 = Meet.builder()
                .name("뜨개질 모임")
                .intro("함께 목도리 떠봐요~")
                .dateTime(LocalDateTime.of(2025, 9, 10, 15, 0))
                .detail("함께 목도리 떠봐요~ 초보자도 부담 없이 시작할 수 있는 쉬운 패턴부터 어려운 패턴까지 다 가능해요~ 서로의 작품을 보며 칭찬도 나누고, 수다도 한가득 풀어봐요~~ 맛 좋기로 유명한 모어브라운에서 만나요~~")
                .maximum(6)
                .current(2)
                .tag(convertTagsToJson(List.of("초보", "고수", "뜨개", "정성")))
                .hostUser(user1)
                .meetLocation(location27)
                .build();

        Meet meet12 = Meet.builder()
                .name("동네 한바퀴")
                .intro("할 것도 없는데 산책이라도 합시다")
                .dateTime(LocalDateTime.of(2025, 8, 29, 20, 0))
                .detail("할 것도 없는데 산책이라도 합시다 가까운 동네 골목부터 작은 공원까지, 발길 닿는 대로 걸읍시다 운동 겸 한 시간 정도 산책합니다 이번에는 공덕 방향의 경의선공원에서 봅시다")
                .maximum(8)
                .current(3)
                .tag(convertTagsToJson(List.of("산책", "소화", "불로장생")))
                .hostUser(user5)
                .meetLocation(location23)
                .build();

        Meet meet13 = Meet.builder()
                .name("장기 둡시다")
                .intro("같이 장기 둡시다")
                .dateTime(LocalDateTime.of(2025, 9, 5, 13, 0))
                .detail("같이 장기 두면서 치매 예방 어떱니까. 누가 제일 잘 두는지 내기도 한 판 하고. 그냥 아무 생각없이 장기 두면서 시간이나 보냅시다. 서강대 후문쪽에 센타에서 자주 봅시다.")
                .maximum(6)
                .current(1)
                .tag(convertTagsToJson(List.of("장기", "서강대", "후문")))
                .hostUser(user4)
                .meetLocation(location23)
                .build();

        Meet meet14 = Meet.builder()
                .name("다도")
                .intro("차 한잔 마시면서 여유 어때요?")
                .dateTime(LocalDateTime.of(2025, 9, 4, 16, 0))
                .detail("다인 찻집에서 여유 즐겨보아요. 이대역 6번 출구에서 바로 보이는 엘리베이터 쪽에 2층에 있는 곳이에요. 맛있는 다과와 함께 차를 즐겨봐요~~ 고상한 모임이랍니다^^")
                .maximum(4)
                .current(3)
                .tag(convertTagsToJson(List.of("다도", "여유", "힐링")))
                .hostUser(user3)
                .meetLocation(location21)
                .build();

        Meet meet15 = Meet.builder()
                .name("시인들의 다락방")
                .intro("마음 속의 말을 함께 써보아요~")
                .dateTime(LocalDateTime.of(2025, 9, 12, 12, 0))
                .detail("평소에 쉽게 꺼내지 못했던 마음을 글로 풀어보아요~~ 짧은 시 한 줄에도 서로의 마음을 확인할 수 있어요! 누군가의 글을 듣고, 또 내 글을 들려주며 따뜻한 울림을 나눠보아요~~ 장소는 카페청록 샵01787C 카페입니다~~ 점심 여기서 같이 먹고 해요~~")
                .maximum(5)
                .current(2)
                .tag(convertTagsToJson(List.of("시", "마음", "따뜻함", "행복")))
                .hostUser(user2)
                .meetLocation(location24)
                .build();

        meetRepository.save(meet1);
        meetRepository.save(meet2);
        meetRepository.save(meet3);
        meetRepository.save(meet4);
        meetRepository.save(meet5);
        meetRepository.save(meet6);
        meetRepository.save(meet7);
        meetRepository.save(meet8);
        meetRepository.save(meet9);
        meetRepository.save(meet10);
        meetRepository.save(meet11);
        meetRepository.save(meet12);
        meetRepository.save(meet13);
        meetRepository.save(meet14);
        meetRepository.save(meet15);

        System.out.println("테스트 데이터 초기화 완료");
        System.out.println("사용자 5명, 장소 30곳, 모임 15개가 추가되었습니다.");
    }

    // List<String> 태그를 JSON 문자열로 변환
    private String convertTagsToJson(List<String> tags) {
        try {
            if (tags == null || tags.isEmpty()) {
                return "[]";
            }
            return objectMapper.writeValueAsString(tags);
        } catch (Exception e) {
            return "[]";
        }
    }
}