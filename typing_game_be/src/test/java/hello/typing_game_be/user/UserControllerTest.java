package hello.typing_game_be.user;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.typing_game_be.friendRequest.repository.FriendRequestRepository;
import hello.typing_game_be.myLongText.repository.MyLongTextRepository;
import hello.typing_game_be.user.dto.UserCreateRequest;
import hello.typing_game_be.user.dto.UserUpdateRequest;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import hello.typing_game_be.user.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private MyLongTextRepository myLongTextRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void beforeEach() {
        myLongTextRepository.deleteAll();
        friendRequestRepository.deleteAll();
        userRepository.deleteAll();
    }
    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    String username = "홍길동";
    String loginId = "testid";
    String password = "1234";

    long registerUser(String username, String loginId, String password) throws Exception {
        UserCreateRequest request = new UserCreateRequest(username, loginId, password);

        userService.register(request);
        User user = userRepository.findByLoginId(loginId).orElseThrow();

        return user.getUserId();
    }
//    @Test
//    void 회원가입_성공() throws Exception {
//        UserCreateRequest request = new UserCreateRequest(username, loginId, password);
//
//        // when & then
//        mockMvc.perform(post("/user")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//            .andExpect(status().isCreated());
//
//        //유저 조회 -> 이름, 비번 검증
//        User savedUser = userRepository.findByLoginId(loginId).orElse(null);
//        assertThat(savedUser.getNickname()).isEqualTo(username);
//        assertThat(passwordEncoder.matches(password, savedUser.getPassword())).isTrue();
//    }

//    @Test
//    void 회원가입_실패_필수필드미입력() throws Exception {
//
//        UserCreateRequest request = new UserCreateRequest(username, "", password);
//
//        // when & then
//        // @Valid 유효성 검증 실패
//        mockMvc.perform(post("/user")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//            .andExpect(status().isBadRequest())
//            .andExpect(jsonPath("$.loginId").value("아이디는 필수입니다."));
//    }
//    @Test
//    void 회원가입_실패_loginId중복() throws Exception {
//
//        //given
//        //유저1 등록
//        registerUser(username, loginId, password);
//
//        // when & then
//        UserCreateRequest request = new UserCreateRequest("aaa", loginId, "111");
//
//        //유저2(loginId중복) 등록 시도
//        mockMvc.perform(post("/user")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//            .andExpect(status().isConflict());
//    }
//    @Test
//    void 회원가입_실패_username중복() throws Exception {
//
//        //given
//        //유저1 등록
//        registerUser(username, loginId, password);
//
//        // when & then
//        UserCreateRequest request = new UserCreateRequest(username, "bbbb","111");
//
//        mockMvc.perform(post("/user")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//            .andExpect(status().isConflict())
//            .andExpect(jsonPath("$.message").value("이미 존재하는 username입니다."));
//
//    }
    @Test
    void 회원조회_성공() throws Exception {
        //given
        //유저1 등록
        registerUser(username, loginId, password);

        // when & then
        mockMvc.perform(get("/user")
                .with(httpBasic(loginId, password)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.loginId").value(loginId))
            .andExpect(jsonPath("$.username").value(username));
    }
    @Test
    void 회원수정_성공() throws Exception {

        //given
        //유저1 등록
        long userId = registerUser(username, loginId, password);

        //유저 이름 수정 요청
        UserUpdateRequest updateRequest = new UserUpdateRequest("abc");

        mockMvc.perform(put("/user/"+userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest))
                .with(httpBasic(loginId, password))) // Basic 인증 시뮬레이션
            .andExpect(status().isOk());

        // 수정한 이름 검증
        User updatedUser = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        assertEquals("abc", updatedUser.getNickname());
    }

    @Test
    void 회원수정_실패_존재하지않는ID() throws Exception {

        //given
        //유저1 등록
        registerUser(username, loginId, password);

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
        //given
        //유저1 등록
        registerUser(username, loginId, password);

        //유저 삭제 요청
        mockMvc.perform(delete("/user")
                .with(httpBasic(loginId, password))) // Basic 인증 시뮬레이션
            .andExpect(status().isOk());

        //DB에 해당 유저의 loginId가 존재하는지 검증
        boolean exists = userRepository.existsByLoginId(loginId);
        assertThat(exists).isFalse();

    }
    @Test
    void 회원검색_성공() throws Exception {
        //given
        //유저1,2 등록
        registerUser("홍길동", loginId, password);
        registerUser("홍길순", "bbbb", password);

        // "홍길"로 부분 검색 시 결과 2개(loginId 일치여부 검증)
        mockMvc.perform(get("/users?username=홍길&page=0&size=5")
                .with(httpBasic(loginId, password))) // Basic 인증 시뮬레이션
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].loginId").value(loginId))
            .andExpect(jsonPath("$.content[1].loginId").value("bbbb"));
    }

}
