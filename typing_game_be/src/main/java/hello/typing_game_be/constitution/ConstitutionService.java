package hello.typing_game_be.constitution;

import org.springframework.stereotype.Service;

import hello.typing_game_be.constitution.repository.ConstitutionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConstitutionService {
    private final ConstitutionRepository constitutionRepository;

    // 조 단위 조회: WHERE articleNumber = ?
    // 절 단위 조회: WHERE chapter = ? AND section = ?
    // 관 단위 조회: WHERE chapter = ? AND section = ? AND subsection = ?
}
