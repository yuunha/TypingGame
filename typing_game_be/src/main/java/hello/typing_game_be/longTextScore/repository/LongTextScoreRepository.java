package hello.typing_game_be.longTextScore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import hello.typing_game_be.longTextScore.entity.LongTextScore;

@Repository
public interface LongTextScoreRepository extends JpaRepository<LongTextScore, Long> {

    //LongText를 페치 조인(longText의 name 필요)
    @Query("select s from LongTextScore s join fetch s.longText where s.user.userId = :userId")
    List<LongTextScore> findByUserId(@Param("userId") Long userId);

    List<LongTextScore> findByUser_UserIdAndLongText_LongTextId(Long userId, Long longTextId);
}
