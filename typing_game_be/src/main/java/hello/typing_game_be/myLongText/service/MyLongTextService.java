package hello.typing_game_be.myLongText.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.typing_game_be.common.exception.BusinessException;
import hello.typing_game_be.common.exception.ErrorCode;
import hello.typing_game_be.myLongText.dto.MyLongTextRequest;
import hello.typing_game_be.myLongText.dto.MyLongTextResponse;
import hello.typing_game_be.myLongText.entity.MyLongText;
import hello.typing_game_be.myLongText.repository.MyLongTextRepository;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyLongTextService {
    private final MyLongTextRepository myLongTextRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long register(MyLongTextRequest request, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        MyLongText saved = myLongTextRepository.save(MyLongText.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .user(user)
            .build()
        );

        return saved.getMyLongTextId();
    }

    @Transactional
    public void deleteById(Long myLongTextId, Long userId) {
        MyLongText longText = myLongTextRepository.findById(myLongTextId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MY_LONG_TEXT_NOT_FOUND));

        if (!longText.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("삭제 권한이 없습니다."); //403 Forbidden 응답
        }
        myLongTextRepository.delete(longText);
    }

    public List<MyLongTextResponse> getByUserId(Long userId) {
        List<MyLongText> myLongTexts = myLongTextRepository.findByUser_UserId(userId);

        return myLongTexts.stream()
            .map(entity -> MyLongTextResponse.fromEntity(entity))  // fromEntity는 DTO 변환 메서드
            .collect(Collectors.toList());

    }
}
