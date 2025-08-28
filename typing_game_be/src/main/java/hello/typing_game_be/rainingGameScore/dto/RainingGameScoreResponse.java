package hello.typing_game_be.rainingGameScore.dto;

import java.time.LocalDateTime;

import hello.typing_game_be.rainingGameScore.entity.RainingGameScore;
import jakarta.validation.constraints.Min;
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
public class RainingGameScoreResponse {
    @Min(0)
    @NotNull
    private Integer score;

    private LocalDateTime createdAt;

    public static RainingGameScoreResponse from(RainingGameScore entity) {
        return RainingGameScoreResponse.builder()
            .score(entity.getScore())
            .createdAt(entity.getCreatedAt())
            .build();
    }
}
