package hello.typing_game_be.user;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.antlr.v4.runtime.atn.ActionTransition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clearDB() {
        userRepository.deleteAll();
    }

    @Test
    void 회원가입_성공() throws Exception {
        //given
        String username = "홍길동";
        String loginId = "testid";
        String password = "1234";


        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                .param("username", username)
                .param("loginId", loginId)
                .param("password", password)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().isOk())
            .andExpect(content().string("회원가입 성공"));

        User savedUser = userRepository.findByUsername(username);
        assertThat(savedUser.getLoginId()).isEqualTo(loginId);
        //assertThat(passwordEncoder.matches(password, savedUser.getPassword())).isTrue();
    }

    @Test
    void 로그인_성공() throws Exception {
        // given
        String username = "홍길동";
        String loginId = "testid";
        String password = "1234";
        userService.register(loginId, username, password); //통합테스트에서는 서비스 호출

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .param("loginId", loginId)
                .param("password", password)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().isOk())
            .andExpect(content().string("로그인 성공"));
    }

}
