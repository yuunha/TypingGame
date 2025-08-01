package hello.typing_game_be.longTextScore.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import hello.typing_game_be.common.exception.BusinessException;
import hello.typing_game_be.common.exception.ErrorCode;
import hello.typing_game_be.longText.entity.LongText;
import hello.typing_game_be.longText.repository.LongTextRepository;
import hello.typing_game_be.longTextScore.dto.LongTextScoreRankingResponse;
import hello.typing_game_be.longTextScore.dto.LongTextScoreRequest;
import hello.typing_game_be.longTextScore.dto.LongTextScoreResponse;
import hello.typing_game_be.longTextScore.dto.UserTextScoreProjection;
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

    // public List<LongTextScoreResponse> getLongScoreByUserId(Long userId) {
    //     List<LongTextScore> scores = longTextScoreRepository.getLongScoreByUser_UserId(userId);
    //
    //     return LongTextScoreMapper.toResponseList(scores);
    // }
    //
    // public List<LongTextScoreRankingResponse> getLongScoreByTitle(String title) {
    //     PageRequest pageRequest = PageRequest.of(0, 50); // 첫 페이지, 50개 가져오기
    //     List<UserTextScoreProjection> projections = longTextScoreRepository.findRankingByTitleOrderByScoreDesc(title,pageRequest);
    //
    //     if (projections.isEmpty()) {
    //         throw new BusinessException(ErrorCode.LONG_SCORE_TITLE_NOT_FOUND);
    //     }
    //
    //     return projections.stream()
    //         .map(p -> new LongTextScoreRankingResponse( p.getUsername(), p.getScore()))
    //         .collect(Collectors.toList());
    // }
}
