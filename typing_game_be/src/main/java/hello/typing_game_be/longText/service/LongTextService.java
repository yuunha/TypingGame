package hello.typing_game_be.longText.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import hello.typing_game_be.longText.dto.LongTextResponse;
import hello.typing_game_be.longText.entity.LongText;
import hello.typing_game_be.longText.repository.LongTextRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LongTextService {
    private final LongTextRepository longTextRepository;

    public List<LongTextResponse> getLongTextList() {
        List<LongText> longTextList = longTextRepository.findAll();

        return longTextList.stream()
            .map(LongTextResponse::new)
            .collect(Collectors.toList());
    }

}
