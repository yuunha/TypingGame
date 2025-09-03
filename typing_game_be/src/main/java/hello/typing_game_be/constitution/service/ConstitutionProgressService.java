package hello.typing_game_be.constitution.service;

import org.springframework.stereotype.Service;

import hello.typing_game_be.common.exception.BusinessException;
import hello.typing_game_be.common.exception.ErrorCode;
import hello.typing_game_be.constitution.entity.ConstitutionProgress;
import hello.typing_game_be.constitution.repository.ConsitutionProgressRepository;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConstitutionProgressService {

    private final ConsitutionProgressRepository consitutionProgressRepository;
    private final UserRepository userRepository;

    public void saveProgress(Long userId, Integer articleIndex, Integer lastPosition) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));

        consitutionProgressRepository.save(
            ConstitutionProgress.builder()
                .user(user)
                .articleIndex(articleIndex)
                .lastPosition(lastPosition)
            .build());
    }
}
