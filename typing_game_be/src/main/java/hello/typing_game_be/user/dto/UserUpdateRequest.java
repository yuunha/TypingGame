package hello.typing_game_be.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateRequest {
    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Size(max = 9, message = "닉네임은 최대 9자까지 가능합니다.")
    private String nickname;
}
