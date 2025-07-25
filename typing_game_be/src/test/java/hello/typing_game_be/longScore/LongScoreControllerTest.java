package hello.typing_game_be.longScore;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.typing_game_be.longScore.dto.LongScoreRequest;
import hello.typing_game_be.longScore.entity.LongScore;
import hello.typing_game_be.longScore.repository.LongScoreRepository;
import hello.typing_game_be.user.dto.UserRequest;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import hello.typing_game_be.user.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class LongScoreControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LongScoreRepository longScoreRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    String title ="별 헤는 밤";
    int score = 500;

    //user
    String username = "hong";
    String loginId = "testid";
    String password = "1111";

    @BeforeEach
    void beforeEach() {
        longScoreRepository.deleteAll();

        //패스워드 인코딩 과정이 필요하므로 userRepository 대신 userService 호출
        userService.register(
            UserRequest.builder()
                .username(username)
                .loginId(loginId)
                .password(password)
                .build()
        );
    }



    @Test
    void 점수저장_성공() throws Exception {
        LongScoreRequest request = LongScoreRequest.builder()
            .title(title)
            .score(score)
            .build();

        //when
        mockMvc.perform(post("/user/1/long-score")
                .with(httpBasic(loginId, password))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());

        //then
        Optional<LongScore> savedScore = longScoreRepository.getLongScoreByUser_UserId(1L);
        assertThat(savedScore).isPresent();
        assertThat(savedScore.get().getTitle()).isEqualTo(title);
        assertThat(savedScore.get().getScore()).isEqualTo(score);
    }
}
