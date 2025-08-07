package hello.typing_game_be.longTextScore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import hello.typing_game_be.longTextScore.entity.LongTextScore;

@Repository
public interface LongTextScoreRepository extends JpaRepository<LongTextScore, Long> {

    List<LongTextScore> findByUser_UserId(Long user_userId);

    List<LongTextScore> findByLongText_LongTextId(Long longTextId);
}
