package hello.typing_game_be.user.dto;

import hello.typing_game_be.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserListResponse {
    private Long userId;
    private String username;
    public static UserListResponse fromEntity(User user) {
        return new UserListResponse(user.getUserId(), user.getNickname());
    }
}
