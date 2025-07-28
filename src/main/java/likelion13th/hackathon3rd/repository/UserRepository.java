package likelion13th.hackathon3rd.repository;

import likelion13th.hackathon3rd.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    
    // 사용자 이름으로 조회
    Optional<User> findByName(String name);
}
