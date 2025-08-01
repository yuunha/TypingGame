package hello.typing_game_be.longTextScore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LongTextScoreWithUsernameResponse {
    private int score;
    private String username;

}
