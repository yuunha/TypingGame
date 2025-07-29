package hello.typing_game_be.longScore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LongScoreRankingResponse {
    private String username;
    private Integer score;
}
