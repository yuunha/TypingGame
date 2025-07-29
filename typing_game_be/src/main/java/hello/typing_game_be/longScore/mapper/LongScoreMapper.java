package hello.typing_game_be.longScore.mapper;

import java.util.List;
import java.util.stream.Collectors;

import hello.typing_game_be.longScore.dto.LongScoreResponse;
import hello.typing_game_be.longScore.entity.LongScore;


public class LongScoreMapper {
    public static LongScoreResponse toResponse(LongScore score) {
        return LongScoreResponse.builder()
            .score(score.getScore())
            .title(score.getTitle())
            .build();
    }
    public static List<LongScoreResponse> toResponseList(List<LongScore> scores) {
        return scores.stream()
            .map(LongScoreMapper::toResponse)
            .collect(Collectors.toList());
    }
}
