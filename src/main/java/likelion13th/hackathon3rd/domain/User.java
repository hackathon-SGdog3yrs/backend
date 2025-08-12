package likelion13th.hackathon3rd.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USERS")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_u", unique = true, nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "keyword", columnDefinition = "TEXT")
    private String keyword;

    @OneToMany(mappedBy = "hostUser", fetch = FetchType.LAZY)
    private List<Meet> createdMeets;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "USER_JOINED_MEETS",
            joinColumns = @JoinColumn(name = "id_u"),
            inverseJoinColumns = @JoinColumn(name = "id_m")
    )
    private List<Meet> joinedMeets;

                    public enum Gender {
                    M, F
                }

                // 특정 모임에 참여했는지 확인
                public boolean hasJoinedMeet(Meet meet) {
                    return joinedMeets != null && joinedMeets.contains(meet);
                }

                // 모임 참여 추가
                public void joinMeet(Meet meet) {
                    if (joinedMeets == null) {
                        joinedMeets = new ArrayList<>();
                    }
                    if (!hasJoinedMeet(meet)) {
                        joinedMeets.add(meet);
                    }
                }

                // 모임 나가기
                public boolean leaveMeet(Meet meet) {
                    if (joinedMeets != null && hasJoinedMeet(meet)) {
                        return joinedMeets.remove(meet);
                    }
                    return false;
                }
            }
