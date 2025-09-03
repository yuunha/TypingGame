package hello.typing_game_be.constitution.service;

import org.springframework.stereotype.Service;

import hello.typing_game_be.common.exception.BusinessException;
import hello.typing_game_be.common.exception.ErrorCode;
import hello.typing_game_be.constitution.dto.ProgressResponse;
import hello.typing_game_be.constitution.entity.ConstitutionProgress;
import hello.typing_game_be.constitution.repository.ConstitutionProgressRepository;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConstitutionProgressService {

    private final ConstitutionProgressRepository constitutionProgressRepository;
    private final UserRepository userRepository;

    public void saveUserProgress(Long userId, Integer articleIndex, Integer lastPosition) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));

        //기존의 진행상황이 있다면 삭제
        constitutionProgressRepository.findByUser_UserId(userId)
            .ifPresent(constitutionProgressRepository::delete);

        constitutionProgressRepository.save(
            ConstitutionProgress.builder()
                .user(user)
                .articleIndex(articleIndex)
                .lastPosition(lastPosition)
            .build());
    }

    public ProgressResponse getUserProgress(Long userId) {

        userRepository.findById(userId)
            .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));

        ConstitutionProgress progress =constitutionProgressRepository.findByUser_UserId(userId)
            .orElseThrow(()->new BusinessException(ErrorCode.CONSTITUTION_PROGRESS_NOT_FOUND));

        return ProgressResponse.fromEntity(progress);
    }
}
