package hello.typing_game_be.longTextScore.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import hello.typing_game_be.longTextScore.dto.LongTextScoreResponse;
import hello.typing_game_be.longTextScore.dto.UserTextScoreProjection;
import hello.typing_game_be.longTextScore.entity.LongTextScore;

@Repository
public interface LongTextScoreRepository extends JpaRepository<LongTextScore, Long> {
    List<LongTextScore> getLongScoreByUser_UserId(Long user_userId);

    @Query("SELECT new hello.typing_game_be.longTextScore.dto.LongTextScoreResponse(s.score, t.title) " +
        "FROM LongTextScore s JOIN s.longText t WHERE s.user.userId = :userId")
    List<LongTextScoreResponse> findScoreAndTitleByUserId(@Param("userId") Long userId);
    //지연로딩 문제 해결


    // @Query("SELECT ls.user.userId AS userId, ls.user.username AS username, ls.score AS score " +
    //     "FROM LongTextScore ls " +
    //     "WHERE ls.title = :title " +
    //     "ORDER BY ls.score DESC")
    // List<UserTextScoreProjection> findRankingByTitleOrderByScoreDesc(@Param("title") String title, Pageable pageable);
}
