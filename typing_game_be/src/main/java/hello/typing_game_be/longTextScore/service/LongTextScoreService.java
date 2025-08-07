package hello.typing_game_be.longTextScore.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.typing_game_be.common.exception.BusinessException;
import hello.typing_game_be.common.exception.ErrorCode;
import hello.typing_game_be.longText.entity.LongText;
import hello.typing_game_be.longText.repository.LongTextRepository;
import hello.typing_game_be.longTextScore.dto.LongTextScoreRequest;
import hello.typing_game_be.longTextScore.dto.LongTextScoreWithTitleResponse;
import hello.typing_game_be.longTextScore.dto.LongTextScoreWithUsernameResponse;
import hello.typing_game_be.longTextScore.entity.LongTextScore;
import hello.typing_game_be.longTextScore.mapper.LongTextScoreMapper;
import hello.typing_game_be.longTextScore.repository.LongTextScoreRepository;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LongTextScoreService {

    private final LongTextScoreRepository longTextScoreRepository;
    private final UserService userService;
    private final LongTextRepository longTextRepository;

    public void register(Long longTextId, LongTextScoreRequest request, Long userId) {
        User user = userService.getUserById(userId);
        LongText longText = longTextRepository.findById(longTextId)
            .orElseThrow(() -> new BusinessException(ErrorCode.LONG_TEXT_NOT_FOUND));

        LongTextScore longTextScore = LongTextScore.builder()
            .user(user)
            .longText(longText)
            .score(request.getScore())
            .build();
        longTextScoreRepository.save(longTextScore);
    }

    @Transactional
    public List<LongTextScoreWithTitleResponse> getLongScoresByUserId(Long userId) {
        List<LongTextScore> scores = longTextScoreRepository.findByUser_UserId(userId);

        return LongTextScoreMapper.toTitleResponseList(scores);
    }

    public List<LongTextScoreWithUsernameResponse> getScoresWithUsernamesByLongTextId(Long longTextId) {
        if (!longTextRepository.existsById(longTextId)) {
            throw new BusinessException(ErrorCode.LONG_TEXT_NOT_FOUND);
        }
        List<LongTextScore> scores = longTextScoreRepository.findByLongText_LongTextId(longTextId);
        return  LongTextScoreMapper.toUsernameResponseList(scores);
    }

}
