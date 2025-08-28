package hello.typing_game_be.rainingGameScore;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.typing_game_be.rainingGameScore.dto.RainingGameScoreRequest;
import hello.typing_game_be.rainingGameScore.entity.RainingGameScore;
import hello.typing_game_be.rainingGameScore.repository.RainingGameScoreRepository;
import hello.typing_game_be.user.dto.UserCreateRequest;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import hello.typing_game_be.user.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class RainigGameScoreControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RainingGameScoreRepository rainingGameScoreRepository;

    private User user;
    private Long userId;

    @BeforeEach
    void beforeEach() {
        //1. DB 초기화
        rainingGameScoreRepository.deleteAll();
        userRepository.deleteAll();

        //2. 테스트용 유저 저장 및 ID 저장
        userService.register(   //패스워드 인코딩 과정이 필요하므로 userRepository 대신 userService 호출
            UserCreateRequest.builder()
                .username("admin")
                .loginId("testid")
                .password("1111")
                .build()
        );

        user = userRepository.findByLoginId("testid").orElseThrow();
        userId = user.getUserId();
    }
    @Test
    void 비게임점수_저장_성공() throws Exception {

        RainingGameScoreRequest request = new RainingGameScoreRequest(100);

        mockMvc.perform(post("/raining-game-score")
                .with(httpBasic("testid", "1111"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());

        List<RainingGameScore> savedScores = rainingGameScoreRepository.findByUserUserIdOrderByCreatedAtDesc(userId);
        assertThat(savedScores.get(0).getScore()).isEqualTo(100);
    }

    @Test
    void 비게임점수_조회_성공() throws Exception {
        rainingGameScoreRepository.save(
            RainingGameScore.builder()
                .user(user)
                .score(101)
                .build()
        );
        rainingGameScoreRepository.save(
            RainingGameScore.builder()
                .user(user)
                .score(102)
                .build()
        );

        mockMvc.perform(get("/raining-game-score")
                .with(httpBasic("testid", "1111"))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].score").value(102))
            .andExpect(jsonPath("$[1].score").value(101));


    }
}
