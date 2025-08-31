package hello.typing_game_be.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hello.typing_game_be.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);

    void deleteByLoginId(String loginId);

    // ContainingIgnoreCase : 대소문자 구분 없이 부분 일치 검색
    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);

    boolean existsByUsername(String username);
}
