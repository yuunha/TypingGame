package hello.typing_game_be.longScore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import hello.typing_game_be.longScore.dto.UserScoreProjection;
import hello.typing_game_be.longScore.entity.LongScore;

@Repository
public interface LongScoreRepository extends JpaRepository<LongScore, Long> {
    List<LongScore> getLongScoreByUser_LoginId(String userLoginId);

    List<LongScore> getLongScoreByUser_UserId(Long user_userId);

    @Query("SELECT ls.user.userId AS userId, ls.user.username AS username, ls.score AS score " +
        "FROM LongScore ls " +
        "WHERE ls.title = :title " +
        "ORDER BY ls.score DESC")
    List<UserScoreProjection> findRankingByTitleOrderByScoreDesc(@Param("title") String title, Pageable pageable);
}
