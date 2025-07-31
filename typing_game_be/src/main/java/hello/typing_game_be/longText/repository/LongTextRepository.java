package hello.typing_game_be.longText.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hello.typing_game_be.longText.entity.LongText;

@Repository
public interface LongTextRepository extends JpaRepository<LongText, Long> {

}
