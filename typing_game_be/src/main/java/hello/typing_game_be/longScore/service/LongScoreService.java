package hello.typing_game_be.longScore.service;

import java.util.List;

import org.springframework.stereotype.Service;

import hello.typing_game_be.longScore.dto.LongScoreRequest;
import hello.typing_game_be.longScore.dto.LongScoreResponse;
import hello.typing_game_be.longScore.entity.LongScore;
import hello.typing_game_be.longScore.mapper.LongScoreMapper;
import hello.typing_game_be.longScore.repository.LongScoreRepository;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.service.UserService;
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

    public List<LongScoreResponse> getLongScoreByUserId(Long userId) {
        List<LongScore> scores = longScoreRepository.getLongScoreByUser_UserId(userId);

        return LongScoreMapper.toResponseList(scores);
    }
}
