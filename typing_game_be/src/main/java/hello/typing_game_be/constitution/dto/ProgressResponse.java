package hello.typing_game_be.constitution.dto;

import hello.typing_game_be.constitution.entity.ConstitutionProgress;
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
public class ProgressResponse {
    private Integer articleIndex;
    private Integer lastPosition;

    public static ProgressResponse fromEntity(ConstitutionProgress progress) {
        return new ProgressResponse(progress.getArticleIndex(),progress.getLastPosition());
    }
}
