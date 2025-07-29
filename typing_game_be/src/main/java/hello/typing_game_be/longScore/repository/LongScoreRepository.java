package hello.typing_game_be.longScore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hello.typing_game_be.longScore.entity.LongScore;

@Repository
public interface LongScoreRepository extends JpaRepository<LongScore, Long> {
    List<LongScore> getLongScoreByUser_LoginId(String userLoginId);

    List<LongScore> getLongScoreByUser_UserId(Long user_userId);
}
