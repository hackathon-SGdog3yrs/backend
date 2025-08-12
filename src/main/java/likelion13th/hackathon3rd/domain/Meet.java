package likelion13th.hackathon3rd.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "MEET")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Meet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_m", nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "intro")
    private String intro;

    @Column(name = "datetime", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "detail", columnDefinition = "TEXT")
    private String detail;

    @Column(name = "maximum", nullable = false)
    private int maximum;

    @Column(name = "current", nullable = false)
    @Builder.Default
    private int current = 1;

    @Column(name = "tag", columnDefinition = "TEXT")
    private String tag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_u", nullable = false)
    private User hostUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_lo", nullable = false)
    private Location meetLocation;

    // 현재 참여자 수 설정
    public void setCurrent(int current) {
        this.current = current;
    }
}
