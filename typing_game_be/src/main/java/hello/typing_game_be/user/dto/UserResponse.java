package hello.typing_game_be.user.dto;

import hello.typing_game_be.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {
    private Long userId;
    private String loginId;
    private String username;
    public static UserResponse fromEntity(User user) {
        return new UserResponse(user.getUserId(), user.getLoginId(), user.getUsername());
    }
}