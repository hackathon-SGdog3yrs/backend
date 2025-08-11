package likelion13th.hackathon3rd.repository;

import likelion13th.hackathon3rd.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    
    // 장소 이름으로 조회
    Optional<Location> findByName(String name);
}
