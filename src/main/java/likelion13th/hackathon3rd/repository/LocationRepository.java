package likelion13th.hackathon3rd.repository;

import likelion13th.hackathon3rd.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    
    // 장소 이름으로 조회
    Optional<Location> findByName(String name);
    
    // 광고하는 장소를 우선으로 정렬하여 조회
    @Query("SELECT l FROM Location l ORDER BY l.advertisement DESC, l.id ASC")
    List<Location> findAllOrderByAdvertisementDesc();
}
