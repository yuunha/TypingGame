package hello.typing_game_be.constitution.service;

import org.springframework.stereotype.Service;

import hello.typing_game_be.common.exception.BusinessException;
import hello.typing_game_be.common.exception.ErrorCode;
import hello.typing_game_be.constitution.dto.ConstitutionResponse;
import hello.typing_game_be.constitution.entity.Constitution;
import hello.typing_game_be.constitution.repository.ConstitutionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConstitutionService {
    private final ConstitutionRepository constitutionRepository;

    public ConstitutionResponse getConstitutionByArticleIndex(int articleIndex) {
        Constitution constitution = constitutionRepository.findByArticleIndex(articleIndex)
            .orElseThrow(()->new BusinessException(ErrorCode.CONSTITUTION_NOT_FOUND));
        return ConstitutionResponse.fromEntity(constitution);
    }

    // 조 단위 조회: WHERE articleNumber = ?
    // 절 단위 조회: WHERE chapter = ? AND section = ?
    // 관 단위 조회: WHERE chapter = ? AND section = ? AND subsection = ?
}
