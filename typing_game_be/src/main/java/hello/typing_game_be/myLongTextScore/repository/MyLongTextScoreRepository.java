package hello.typing_game_be.myLongTextScore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hello.typing_game_be.longTextScore.entity.LongTextScore;
import hello.typing_game_be.myLongTextScore.entity.MyLongTextScore;

public interface MyLongTextScoreRepository extends JpaRepository<MyLongTextScore, Long> {

    List<MyLongTextScore> findByUserUserId(Long userId);

    @Query("SELECT MAX(lts.score) FROM MyLongTextScore lts WHERE lts.user.userId = :userId AND lts.myLongText.myLongTextId = :myLongTextId")
    Integer findMaxScoreByUserIdAndLongTextId(@Param("userId") Long userId, @Param("myLongTextId") Long myLongTextId);
}
