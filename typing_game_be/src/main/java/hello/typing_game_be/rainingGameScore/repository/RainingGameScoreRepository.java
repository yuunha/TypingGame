package hello.typing_game_be.rainingGameScore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hello.typing_game_be.rainingGameScore.entity.RainingGameScore;

@Repository
public interface RainingGameScoreRepository extends JpaRepository<RainingGameScore, Long> {

    List<RainingGameScore> findByUserUserIdOrderByCreatedAtDesc(Long userId);
}
