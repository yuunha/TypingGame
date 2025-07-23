package hello.typing_game_be.user;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import hello.typing_game_be.user.dto.UserRequest;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import hello.typing_game_be.user.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void clearDB() {
        userRepository.deleteAll();
    }

    String username = "홍길동";
    String loginId = "testid";
    String password = "1234";

    @Test
    void 회원가입_성공() throws Exception {
        UserRequest request = UserRequest.builder()
            .username(username)
            .loginId(loginId)
            .password(password)
            .build();

        // when & then
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());


        User savedUser = userRepository.findByUsername(username);
        assertThat(savedUser.getLoginId()).isEqualTo(loginId);
        assertThat(passwordEncoder.matches(password, savedUser.getPassword())).isTrue();
    }
    @Test
    void 회원가입_실패_필수필드미입력() throws Exception {

        UserRequest request = UserRequest.builder()
            .username(username)
            .loginId("")
            .password(password)
            .build();

        // when & then
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.loginId").value("아이디는 필수입니다."));
    }
    @Test
    void 회원가입_실패_아이디중복() throws Exception {

        UserRequest request = UserRequest.builder()
            .username(username)
            .loginId(loginId)
            .password(password)
            .build();
        UserRequest request1= UserRequest.builder()
            .username("aaa")
            .loginId(loginId)
            .password("111")
            .build();
        //given
        userService.register(request); //통합테스트에서는 서비스 호출
        // when & then
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
            .andExpect(status().isConflict());
    }
    // @Test
    // void 로그인_성공() throws Exception {
    //
    //     UserRequest request = UserRequest.builder()
    //         .username(username)
    //         .loginId(loginId)
    //         .password(password)
    //         .build();
    //     // given
    //     userService.register(request);
    //
    //     // when & then
    //     mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(request)))
    //         .andExpect(status().isOk());
    // }
    //
    // @Test
    // void 로그인_실패_비밀번호_틀림() throws Exception {
    //     UserRequest request1 = UserRequest.builder()
    //         .username(username)
    //         .loginId(loginId)
    //         .password(password)
    //         .build();
    //     UserRequest request2 = UserRequest.builder()
    //         .username(username)
    //         .loginId(loginId)
    //         .password("0000")
    //         .build();
    //     //given
    //     userService.register(request1); //통합테스트에서는 서비스 호출
    //     // when & then
    //     mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(request2)))
    //         .andExpect(status().isUnauthorized())
    //         .andExpect(jsonPath("$.message").value("비밀번호가 일치하지 않습니다."));
    //
    //     // .andDo(print()); -> 디버깅해보기
    // }
    // @Test
    // void 로그인_실패_아이디존재X() throws Exception {
    //     UserRequest request = UserRequest.builder()
    //         .username(username)
    //         .loginId("noid")
    //         .password(password)
    //         .build();
    //
    //     // when & then
    //     mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(request)))
    //         .andExpect(status().isNotFound())
    //         .andExpect(jsonPath("$.message").value("존재하지 않는 아이디입니다."));
    //
    // }

}
