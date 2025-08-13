package hello.typing_game_be.longTextScore.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LongTextScoreSimpleResponse {
    private Long longScoreId;
    private int score;
    private LocalDateTime createdAt;
}
