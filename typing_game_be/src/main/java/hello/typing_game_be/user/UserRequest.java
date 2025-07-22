package hello.typing_game_be.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class UserRequest {
    @NotBlank(message = "이름은 필수입니다.")
    @Size(max = 9, message = "이름은 최대 9자까지 가능합니다.")
    private String username;

    @Size(max = 20, message = "아이디는 최대 20자까지 가능합니다.")
    @NotBlank(message = "아이디는 필수입니다.")
    private String loginId;

    @Size(max = 20, message = "비밀번호는 최대 20자까지 가능합니다.")
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;


}