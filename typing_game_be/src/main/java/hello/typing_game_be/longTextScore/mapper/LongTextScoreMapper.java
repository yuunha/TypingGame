package hello.typing_game_be.longTextScore.mapper;

import java.util.List;
import java.util.stream.Collectors;

import hello.typing_game_be.longTextScore.dto.LongTextScoreResponse;
import hello.typing_game_be.longTextScore.entity.LongTextScore;


public class LongTextScoreMapper {
    public static LongTextScoreResponse toResponse(LongTextScore score) {
        return LongTextScoreResponse.builder()
            .score(score.getScore())
            .title(score.getTitle())
            .build();
    }

    public static List<LongTextScoreResponse> toResponseList(List<LongTextScore> scores) {
        return scores.stream()
            .map(LongTextScoreMapper::toResponse)
            .collect(Collectors.toList());
    }

}
