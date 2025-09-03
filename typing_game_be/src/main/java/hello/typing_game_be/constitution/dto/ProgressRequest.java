package hello.typing_game_be.constitution.dto;

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
public class ProgressRequest {
    @NotNull
    private Integer articleIndex; // 조
    @NotNull
    private Integer lastPosition;    // 조 내에서 멈춘 글자 위치
}
