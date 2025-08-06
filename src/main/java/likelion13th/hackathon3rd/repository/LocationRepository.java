package likelion13th.hackathon3rd.repository;

import likelion13th.hackathon3rd.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Integer> {
}
