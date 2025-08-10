package likelion13th.hackathon3rd.repository;

import likelion13th.hackathon3rd.domain.Meet;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetRepository extends JpaRepository<Meet, Integer> {
    List<Meet> findByHostUser_Id(Integer userId, Sort sort);
}
