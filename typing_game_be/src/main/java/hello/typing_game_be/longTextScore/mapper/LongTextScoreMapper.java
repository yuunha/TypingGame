package hello.typing_game_be.longTextScore.mapper;

import java.util.List;
import java.util.stream.Collectors;

import hello.typing_game_be.longTextScore.dto.LongTextScoreSimpleResponse;
import hello.typing_game_be.longTextScore.dto.LongTextScoreSummuryResponse;
import hello.typing_game_be.longTextScore.entity.LongTextScore;

public class LongTextScoreMapper {
    public static LongTextScoreSummuryResponse toTitleResponse(LongTextScore entity) {
        if (entity.getScore() == null) {
            throw new IllegalArgumentException("점수가 존재하지 않습니다.");
        }

        return new LongTextScoreSummuryResponse(
            entity.getLongScoreId(),
            entity.getScore(),
            entity.getLongText().getTitle(),
            entity.getCreatedAt()
        );
    }
    public static List<LongTextScoreSummuryResponse> toTitleResponseList(List<LongTextScore> entities) {
        return entities.stream()
            .map(LongTextScoreMapper::toTitleResponse)
            .collect(Collectors.toList());
    }

    public static LongTextScoreSimpleResponse toUsernameResponse(LongTextScore entity) {
        return new LongTextScoreSimpleResponse(
            entity.getLongScoreId(),
            entity.getScore(),
            entity.getCreatedAt()
        );
    }

    public static List<LongTextScoreSimpleResponse> toUsernameResponseList(List<LongTextScore> entities) {
        return entities.stream()
            .map(LongTextScoreMapper::toUsernameResponse)
            .collect(Collectors.toList());
    }
}
