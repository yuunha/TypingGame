package hello.typing_game_be.longScore;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.typing_game_be.longScore.dto.LongScoreRequest;
import hello.typing_game_be.longScore.repository.LongScoreRepository;
import hello.typing_game_be.longScore.service.LongScoreService;
import hello.typing_game_be.user.dto.UserRequest;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import hello.typing_game_be.user.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class LongScoreControllerTest_Get {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LongScoreRepository longScoreRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LongScoreService longScoreService;


    String title1 ="별 헤는 밤";
    int score1 = 500;
    String title2 ="별 헤는 밤2";
    int score2 = 501;

    //user
    String username = "hong";
    String loginId = "testid";
    String password = "1111";

    private User user;
    private Long userId;

    @BeforeEach
    void beforeEach() {
        longScoreRepository.deleteAll();
        userRepository.deleteAll();

        //패스워드 인코딩 과정이 필요하므로 userRepository 대신 userService 호출
        userService.register(
            UserRequest.builder()
                .username(username)
                .loginId(loginId)
                .password(password)
                .build()
        );

        // 테스트용 유저 저장 및 ID 저장
        user = userRepository.findByLoginId(loginId).orElseThrow();
        userId = user.getUserId();

        longScoreService.register( userId,
            LongScoreRequest.builder()
                .title(title1)
                .score(score1)
                .build()
        );
        longScoreService.register( userId,
            LongScoreRequest.builder()
                .title(title2)
                .score(score2)
                .build()
        );
    }

    @Test
    void 점수조회_성공() throws Exception {
        //when
        mockMvc.perform(get("/user/" + userId + "/long-score")
                .with(httpBasic(loginId, password)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].title").value(title1))
            .andExpect(jsonPath("$[0].score").value(score1))
            .andExpect(jsonPath("$[1].title").value(title2))
            .andExpect(jsonPath("$[1].score").value(score2));

    }
    @Test //로그인안됨 -> 401
    void 점수조회_실패_인증문제() throws Exception {
        //when&then
        mockMvc.perform(get("/user/" + userId + "/long-score")
                .with(httpBasic(loginId, "wrong")))
            .andExpect(status().isUnauthorized());
    }

    @Test //로그인했지만,url의 유저id와 다름! -> 403
    void 점수조회_실패_인가문제() throws Exception {
        long invalidUserId = userId +1;
        //when&then
        mockMvc.perform(get("/user/" + invalidUserId + "/long-score")
                .with(httpBasic(loginId, password)))
            .andExpect(status().isForbidden());
    }
}
