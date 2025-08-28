package hello.typing_game_be.rainingGameScore.service;

import java.util.List;

import org.springframework.stereotype.Service;

import hello.typing_game_be.rainingGameScore.dto.RainingGameScoreRequest;
import hello.typing_game_be.rainingGameScore.dto.RainingGameScoreResponse;
import hello.typing_game_be.rainingGameScore.entity.RainingGameScore;
import hello.typing_game_be.rainingGameScore.repository.RainingGameScoreRepository;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RainingGameScoreService {
    private final RainingGameScoreRepository rainingGameScoreRepository;
    private final UserService userService;

    public Long register(RainingGameScoreRequest request, Long userId) {
        User user = userService.getUserById(userId);
        RainingGameScore score = RainingGameScore.builder()
            .user(user)
            .score(request.getScore())
            .build();
        rainingGameScoreRepository.save(score);
        return score.getRainingGameScoreId();
    }

    public List<RainingGameScoreResponse> getScores(Long userId) {

        List<RainingGameScore> scores = rainingGameScoreRepository.findByUserUserIdOrderByCreatedAtDesc(userId);

        return scores.stream()
            .map(RainingGameScoreResponse::from)
            .toList();
    }
}
