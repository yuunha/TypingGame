package hello.typing_game_be.user;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.typing_game_be.user.dto.UserCreateRequest;
import hello.typing_game_be.user.dto.UserResponse;
import hello.typing_game_be.user.dto.UserUpdateRequest;
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
    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    String username = "홍길동";
    String loginId = "testid";
    String password = "1234";

    @Test
    void 회원가입_성공() throws Exception {
        UserCreateRequest request = UserCreateRequest.builder()
            .username(username)
            .loginId(loginId)
            .password(password)
            .build();

        // when & then
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());


        User savedUser = userRepository.findByUsername(username);
        assertThat(savedUser.getLoginId()).isEqualTo(loginId);
        assertThat(passwordEncoder.matches(password, savedUser.getPassword())).isTrue();
    }
    @Test
    void 회원가입_실패_필수필드미입력() throws Exception {

        UserCreateRequest request = UserCreateRequest.builder()
            .username(username)
            .loginId("")
            .password(password)
            .build();

        // when & then
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.loginId").value("아이디는 필수입니다."));
    }
    @Test
    void 회원가입_실패_아이디중복() throws Exception {

        UserCreateRequest request = UserCreateRequest.builder()
            .username(username)
            .loginId(loginId)
            .password(password)
            .build();
        UserCreateRequest request1= UserCreateRequest.builder()
            .username("aaa")
            .loginId(loginId)
            .password("111")
            .build();
        //given
        userService.register(request); //통합테스트에서는 서비스 호출
        // when & then
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
            .andExpect(status().isConflict());
    }
    @Test
    void 회원조회_성공() throws Exception {
        UserCreateRequest request = UserCreateRequest.builder()
            .username(username)
            .loginId(loginId)
            .password(password)
            .build();
        //given
        userService.register(request);

        mockMvc.perform(get("/user")
                .with(httpBasic(loginId, password))) // Basic 인증 시뮬레이션
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.loginId").value(loginId))
            .andExpect(jsonPath("$.username").value(username));
    }
    @Test
    void 회원수정_성공() throws Exception {

        //given
        UserCreateRequest request = UserCreateRequest.builder()
            .username(username)
            .loginId(loginId)
            .password(password)
            .build();
        //given
        userService.register(request);
        UserResponse userResponse = userService.getUserByLoginId(loginId);
        Long userId = userResponse.getUserId();

        UserUpdateRequest updateRequest = new UserUpdateRequest("변경된 이름");

        mockMvc.perform(put("/user/"+userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest))
                .with(httpBasic(loginId, password))) // Basic 인증 시뮬레이션
            .andExpect(status().isOk());

        User updatedUser = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        assertEquals("변경된 이름", updatedUser.getUsername());
    }

    @Test
    void 회원수정_실패_존재하지않는ID() throws Exception {

        //given
        UserCreateRequest request = UserCreateRequest.builder()
            .username(username)
            .loginId(loginId)
            .password(password)
            .build();
        //given
        userService.register(request);
        UserResponse userResponse = userService.getUserByLoginId(loginId);
        Long userId = userResponse.getUserId();

        UserUpdateRequest updateRequest = new UserUpdateRequest("변경된 이름");

        mockMvc.perform(put("/user/"+100)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
                .with(httpBasic(loginId, password))) // Basic 인증 시뮬레이션
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("존재하지 않는 유저입니다."));
    }

    @Test
    void 회원삭제_성공() throws Exception {
        UserCreateRequest request = UserCreateRequest.builder()
            .username(username)
            .loginId(loginId)
            .password(password)
            .build();
        //given
        userService.register(request);

        mockMvc.perform(delete("/user")
                .with(httpBasic(loginId, password))) // Basic 인증 시뮬레이션
            .andExpect(status().isOk());

        boolean exists = userRepository.existsByLoginId(loginId);
        assertThat(exists).isFalse();

    }


}
