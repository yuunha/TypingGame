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
    
    // ContainingIgnoreCase : 대소문자 구분 없이 부분 일치 검색
    Page<User> findByNicknameContainingIgnoreCase(String nickname, Pageable pageable);
    
    Optional<User> findByProviderId(String providerId);

    boolean existsByUserId(Long userId);

    void deleteByUserId(Long userId);

    boolean existsByNickname(String nickname);

    Optional<User> findByNickname(String nickname);
}
