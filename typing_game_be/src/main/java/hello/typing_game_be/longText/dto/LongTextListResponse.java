package hello.typing_game_be.longText.dto;

import hello.typing_game_be.longText.entity.LongText;
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
public class LongTextListResponse {
    private Long longTextId;
    private String title;
    public LongTextListResponse(LongText longText) {
        this.longTextId = longText.getLongTextId();
        this.title = longText.getTitle();
    }
}
