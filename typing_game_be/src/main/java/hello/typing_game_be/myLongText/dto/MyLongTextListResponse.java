package hello.typing_game_be.myLongText.dto;

import hello.typing_game_be.myLongText.entity.MyLongText;
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
public class MyLongTextListResponse {
    private Long myLongTextId;
    private String title;

    public static MyLongTextListResponse fromEntity(MyLongText entity) {
        return new MyLongTextListResponse(
            entity.getMyLongTextId(),
            entity.getTitle()
        );
    }
}
