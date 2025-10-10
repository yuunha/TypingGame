package hello.typing_game_be.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NicknameRequest {
    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(max = 9, message = "닉네임은 최대 9자까지 가능합니다.")
    private String nickname;
}
