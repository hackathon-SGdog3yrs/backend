package likelion13th.hackathon3rd.repository;

import likelion13th.hackathon3rd.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
