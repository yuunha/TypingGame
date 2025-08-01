package hello.typing_game_be.longText.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hello.typing_game_be.longText.entity.LongText;

@Repository
public interface LongTextRepository extends JpaRepository<LongText, Long> {

    boolean existsByTitle(String title);
    Optional<LongText> findByTitle(String title);
}
