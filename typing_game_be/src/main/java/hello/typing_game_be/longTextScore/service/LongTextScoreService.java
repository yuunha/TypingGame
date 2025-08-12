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

    @Transactional
    public Long register(Long longTextId, LongTextScoreRequest request, Long userId) {
        User user = userService.getUserById(userId);

        // 긴글 존재하지 않으면 예외 발생
        LongText longText = longTextRepository.findById(longTextId)
            .orElseThrow(() -> new BusinessException(ErrorCode.LONG_TEXT_NOT_FOUND));

        LongTextScore longTextScore = LongTextScore.builder()
            .user(user)
            .longText(longText)
            .score(request.getScore())
            .build();
        longTextScoreRepository.save(longTextScore);

        return longTextScore.getLongScoreId();
    }

    //유저의 긴글 점수 전체 조회
    //응답값 : 리스트 - { title (긴글 제목) , score }
    @Transactional
    public List<LongTextScoreWithTitleResponse> getLongScoresByUserId(Long userId) {
        List<LongTextScore> scores = longTextScoreRepository.findByUserId(userId);

        return LongTextScoreMapper.toTitleResponseList(scores);
    }

    //유저의 특정 긴글에 대한 점수 조회
    public List<LongTextScoreWithUsernameResponse> getScoresWithUsernamesByLongTextIdAndUserId(Long userId,Long longTextId ) {
        if (!longTextRepository.existsById(longTextId)) {
            throw new BusinessException(ErrorCode.LONG_TEXT_NOT_FOUND);
        }
        List<LongTextScore> scores = longTextScoreRepository.findByUser_UserIdAndLongText_LongTextId(userId,longTextId);
        return  LongTextScoreMapper.toUsernameResponseList(scores);
    }
}
