package hello.typing_game_be.longText.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import hello.typing_game_be.common.exception.BusinessException;
import hello.typing_game_be.common.exception.ErrorCode;
import hello.typing_game_be.longText.dto.LongTextListResponse;
import hello.typing_game_be.longText.dto.LongTextResponse;
import hello.typing_game_be.longText.entity.LongText;
import hello.typing_game_be.longText.repository.LongTextRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LongTextService {
    private final LongTextRepository longTextRepository;

    public List<LongTextListResponse> getLongTextList() {
        List<LongText> longTextList = longTextRepository.findAll();

        return longTextList.stream()
            .map(LongTextListResponse::new)
            .collect(Collectors.toList());
    }

    public LongTextResponse getLongTextById(Long id) {
        LongText longText = longTextRepository.findById(id)
            .orElseThrow(
                () -> new BusinessException(ErrorCode.LONG_TEXT_NOT_FOUND)
            );
        return LongTextResponse.builder()
            .longTextId(longText.getLongTextId())
            .title(longText.getTitle())
            .content(longText.getContent())
            .build();
    }

}
