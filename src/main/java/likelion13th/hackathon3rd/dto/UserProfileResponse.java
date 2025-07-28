package likelion13th.hackathon3rd.dto;

import likelion13th.hackathon3rd.domain.Meet;
import likelion13th.hackathon3rd.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {

    private int id;
    private String name;
    private String gender;
    private int age;
    private String keyword;
    private List<Integer> createdMeetID;
    private List<Integer> joinedMeetID;

    public static UserProfileResponse from(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .gender(user.getGender().name()) // "M" or "F"
                .age(user.getAge())
                .keyword(user.getKeyword())
                .createdMeetID(user.getCreatedMeets().stream()
                        .map(Meet::getId)
                        .collect(Collectors.toList()))
                .joinedMeetID(user.getJoinedMeets().stream()
                        .map(Meet::getId)
                        .collect(Collectors.toList()))
                .build();
    }
}
