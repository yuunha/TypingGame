package hello.typing_game_be.longScore.service;

import org.springframework.stereotype.Service;

import hello.typing_game_be.longScore.dto.LongScoreRequest;
import hello.typing_game_be.longScore.entity.LongScore;
import hello.typing_game_be.longScore.repository.LongScoreRepository;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LongScoreService {

    private final LongScoreRepository longScoreRepository;
    private final UserService userService;

    public void register(Long userId, LongScoreRequest request) {
        User user = userService.getUserById(userId);
        LongScore longScore = LongScore.builder()
            .user(user)
            .title(request.getTitle())
            .score(request.getScore())
            .build();
        longScoreRepository.save(longScore);
    }
}
