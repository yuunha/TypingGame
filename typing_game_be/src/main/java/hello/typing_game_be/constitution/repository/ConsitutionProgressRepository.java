package hello.typing_game_be.constitution.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.typing_game_be.constitution.entity.ConstitutionProgress;

public interface ConsitutionProgressRepository extends JpaRepository<ConstitutionProgress,Long> {
    Optional<ConstitutionProgress> findByUser_LoginId(String user1);
}
