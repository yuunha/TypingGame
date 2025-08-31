package hello.typing_game_be.user.dto;

import hello.typing_game_be.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long userId;
    private String loginId;
    private String username;
    private String profileImageUrl;
}