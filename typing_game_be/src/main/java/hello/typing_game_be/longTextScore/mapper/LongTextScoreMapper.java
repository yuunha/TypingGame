package hello.typing_game_be.longTextScore.mapper;

import java.util.List;
import java.util.stream.Collectors;

import hello.typing_game_be.longTextScore.dto.LongTextScoreWithTitleResponse;
import hello.typing_game_be.longTextScore.dto.LongTextScoreWithUsernameResponse;
import hello.typing_game_be.longTextScore.entity.LongTextScore;

public class LongTextScoreMapper {
    public static LongTextScoreWithTitleResponse toTitleResponse(LongTextScore entity) {
        if (entity.getScore() == null) {
            throw new IllegalArgumentException("점수가 존재하지 않습니다.");
        }

        return new LongTextScoreWithTitleResponse(
            entity.getScore(),
            entity.getLongText().getTitle()
        );
    }
    public static List<LongTextScoreWithTitleResponse> toTitleResponseList(List<LongTextScore> entities) {
        return entities.stream()
            .map(LongTextScoreMapper::toTitleResponse)
            .collect(Collectors.toList());
    }

    public static LongTextScoreWithUsernameResponse toUsernameResponse(LongTextScore entity) {
        return new LongTextScoreWithUsernameResponse(
            entity.getScore(),
            entity.getUser().getUsername()
        );
    }

    public static List<LongTextScoreWithUsernameResponse> toUsernameResponseList(List<LongTextScore> entities) {
        return entities.stream()
            .map(LongTextScoreMapper::toUsernameResponse)
            .collect(Collectors.toList());
    }
}
