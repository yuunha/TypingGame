package hello.typing_game_be.longTextScore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import hello.typing_game_be.longTextScore.dto.LongTextScoreWithTitleResponse;
import hello.typing_game_be.longTextScore.dto.LongTextScoreWithUsernameResponse;
import hello.typing_game_be.longTextScore.entity.LongTextScore;

@Repository
public interface LongTextScoreRepository extends JpaRepository<LongTextScore, Long> {
    List<LongTextScore> getLongScoreByUser_UserId(Long user_userId);

    // @Query("SELECT new hello.typing_game_be.longTextScore.dto.LongTextScoreResponse(s.score, t.title) " +
    //     "FROM LongTextScore s JOIN s.longText t WHERE s.user.userId = :userId")
    // List<LongTextScoreResponse> findScoreAndTitleByUserId(@Param("userId") Long userId);


    //지연로딩 문제 해결
    @Query("SELECT new hello.typing_game_be.longTextScore.dto.LongTextScoreWithTitleResponse(s.score, t.title) " +
        "FROM LongTextScore s JOIN s.longText t WHERE s.user.userId = :userId")
    List<LongTextScoreWithTitleResponse> findScoresWithTitleByUserId(@Param("userId") Long userId);

    @Query("SELECT new hello.typing_game_be.longTextScore.dto.LongTextScoreWithUsernameResponse(s.score, u.username) " +
        "FROM LongTextScore s JOIN s.user u WHERE s.longText.longTextId = :longTextId")
    List<LongTextScoreWithUsernameResponse> findScoresWithUsernameByLongTextId(@Param("longTextId") Long longTextId);
}
