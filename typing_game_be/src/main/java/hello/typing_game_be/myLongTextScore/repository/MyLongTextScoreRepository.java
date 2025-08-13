package hello.typing_game_be.myLongTextScore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.typing_game_be.longTextScore.entity.LongTextScore;
import hello.typing_game_be.myLongTextScore.entity.MyLongTextScore;

public interface MyLongTextScoreRepository extends JpaRepository<MyLongTextScore, Long> {

    List<LongTextScore> findByUserUserId(Long userId);
}
