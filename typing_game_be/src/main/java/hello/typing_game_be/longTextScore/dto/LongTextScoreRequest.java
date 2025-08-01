package hello.typing_game_be.longTextScore.dto;

import hello.typing_game_be.longText.entity.LongText;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LongTextScoreRequest {

    @Min(0)
    @NotNull
    private Integer score; // score가 안들어오면 400에러
                        //Integer 자료형이라면 null이 입력됨

    @NotNull
    private Long longTextId;
}
