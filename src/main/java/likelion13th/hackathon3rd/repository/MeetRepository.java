package likelion13th.hackathon3rd.repository;

import likelion13th.hackathon3rd.domain.Meet;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MeetRepository extends JpaRepository<Meet, Integer> {
    
    // 현재 참여자 수가 최대 참여자 수보다 적은 진행 중인 모임들을 현재 참여자 수 기준 내림차순으로 조회
    @Query("SELECT m FROM Meet m WHERE m.current < m.maximum ORDER BY m.current DESC")
    List<Meet> findAvailableMeetsOrderByCurrentDesc();
    
    // 모임 ID로 상세 정보 조회 (연관 엔티티들과 함께)
    @Query("SELECT m FROM Meet m " +
           "JOIN FETCH m.hostUser " +
           "JOIN FETCH m.meetLocation " +
           "WHERE m.id = :meetId")
    Optional<Meet> findByIdWithDetails(@Param("meetId") Integer meetId);
    
    // 모임 이름으로 검색 (대소문자 무시)
    @Query("SELECT m FROM Meet m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Meet> findByNameContainingIgnoreCase(@Param("name") String name);
    
    // 모임 설명으로 검색 (대소문자 무시)
    @Query("SELECT m FROM Meet m WHERE LOWER(m.detail) LIKE LOWER(CONCAT('%', :detail, '%'))")
    List<Meet> findByDetailContainingIgnoreCase(@Param("detail") String detail);
    
    // 모임 태그로 검색 (대소문자 무시)
    @Query("SELECT m FROM Meet m WHERE LOWER(m.tag) LIKE LOWER(CONCAT('%', :tag, '%'))")
    List<Meet> findByTagContainingIgnoreCase(@Param("tag") String tag);
    
    // 모임 한줄소개로 검색 (대소문자 무시)
    @Query("SELECT m FROM Meet m WHERE LOWER(m.intro) LIKE LOWER(CONCAT('%', :intro, '%'))")
    List<Meet> findByIntroContainingIgnoreCase(@Param("intro") String intro);

    // 사용자가 생성한 모임 조회 (정렬 가능)
    List<Meet> findByHostUser_Id(Integer userId, Sort sort);
}
