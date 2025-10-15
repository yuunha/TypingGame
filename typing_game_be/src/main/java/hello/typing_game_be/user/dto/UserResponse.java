package hello.typing_game_be.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long userId;
    private String nickname;
    private String profileImageKey;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}