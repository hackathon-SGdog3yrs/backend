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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;
    private LocalDateTime dateTime;

    @Column(columnDefinition = "TEXT")
    private String detail;

    @Column(nullable = false)
    private int maximum;

    @Column(nullable = false)
    @Builder.Default
    private int current = 1;

    @Column(columnDefinition = "TEXT")
    private String tag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_u", nullable = false)
    private User hostUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_lo", nullable = false)
    private Location meetLocation;

}