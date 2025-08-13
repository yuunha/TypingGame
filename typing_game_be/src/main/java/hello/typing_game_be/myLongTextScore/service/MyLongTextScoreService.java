package hello.typing_game_be.myLongTextScore.service;

import org.springframework.stereotype.Service;

import hello.typing_game_be.common.exception.BusinessException;
import hello.typing_game_be.common.exception.ErrorCode;
import hello.typing_game_be.longText.entity.LongText;
import hello.typing_game_be.longTextScore.dto.LongTextScoreRequest;
import hello.typing_game_be.longTextScore.entity.LongTextScore;
import hello.typing_game_be.myLongText.entity.MyLongText;
import hello.typing_game_be.myLongText.repository.MyLongTextRepository;
import hello.typing_game_be.myLongTextScore.entity.MyLongTextScore;
import hello.typing_game_be.myLongTextScore.repository.MyLongTextScoreRepository;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyLongTextScoreService {
    private final MyLongTextScoreRepository myLongTextScoreRepository;
    private final MyLongTextRepository myLongTextRepository;
    private final UserService userService;

    public Long register(Long myLongTextId, LongTextScoreRequest request, Long userId) {
        User user = userService.getUserById(userId);

        // 긴글 존재하지 않으면 예외 발생
        MyLongText myLongText = myLongTextRepository.findById(myLongTextId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MY_LONG_TEXT_NOT_FOUND));

        MyLongTextScore myLongTextScore = MyLongTextScore.builder()
            .user(user)
            .myLongText(myLongText)
            .score(request.getScore())
            .build();
        myLongTextScoreRepository.save(myLongTextScore);

        return myLongTextScore.getMyLongTextScoreId();
    }
}
