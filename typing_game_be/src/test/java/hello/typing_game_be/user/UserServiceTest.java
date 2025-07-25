package hello.typing_game_be.user;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import hello.typing_game_be.common.exception.BusinessException;
import hello.typing_game_be.user.service.UserService;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @Transactional
    void 유저삭제_실패_존재하지않는_아이디() {
        // given
        String nonExistentLoginId = "nonexistentUser";

        // when & then
        assertThatThrownBy(() -> userService.deleteUserByLoginId(nonExistentLoginId))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("존재하지 않는 아이디입니다");
    }
}
