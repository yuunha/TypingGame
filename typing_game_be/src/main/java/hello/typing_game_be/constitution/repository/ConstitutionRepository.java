package hello.typing_game_be.constitution.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hello.typing_game_be.constitution.entity.Constitution;

@Repository
public interface ConstitutionRepository extends JpaRepository<Constitution, Long> {
}
