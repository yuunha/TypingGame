package hello.typing_game_be.longTextScore;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.typing_game_be.longText.entity.LongText;
import hello.typing_game_be.longText.repository.LongTextRepository;
import hello.typing_game_be.longTextScore.dto.LongTextScoreRequest;
import hello.typing_game_be.longTextScore.entity.LongTextScore;
import hello.typing_game_be.longTextScore.repository.LongTextScoreRepository;
import hello.typing_game_be.user.dto.UserRequest;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import hello.typing_game_be.user.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class LongTextScoreControllerTest_Post {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LongTextScoreRepository longTextScoreRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LongTextRepository longTextRepository;

    private Long userId;
    private Long longTextId;

    @BeforeEach
    void beforeEach() {
        //1. DB 초기화
        longTextScoreRepository.deleteAll();
        userRepository.deleteAll();
        longTextRepository.deleteAll();


        //2. 테스트용 유저 저장 및 ID 저장
        userService.register(   //패스워드 인코딩 과정이 필요하므로 userRepository 대신 userService 호출
            UserRequest.builder()
                .username("admin")
                .loginId("testid")
                .password("1111")
                .build()
        );
        User user = userRepository.findByLoginId("testid").orElseThrow();
        userId = user.getUserId();

        //3. 긴글 저장 및 ID 저장
        longTextRepository.save(LongText.builder()
            .title("애국가")
            .content("동해물과 백두산이")
            .build()
        );
        LongText longText = longTextRepository.findByTitle("애국가").orElseThrow();
        longTextId = longText.getLongTextId();

    }

    @Test
    @Transactional
    void 긴글점수_저장_성공() throws Exception {
        //given
        //User, LongText 저장 + score는 500
        LongTextScoreRequest request = new LongTextScoreRequest(500);

        //when
        mockMvc.perform(post("/long-text/" + longTextId  + "/score")
                .with(httpBasic("testid", "1111"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());

        //then
        List<LongTextScore> savedScores = longTextScoreRepository.findByUser_UserId(userId);
        LongTextScore savedScore = savedScores.get(0);
        assertThat(savedScore.getLongText().getTitle()).isEqualTo("애국가");
        assertThat(savedScore.getScore()).isEqualTo(500);
    }
    @Test
    void 긴글점수_저장_실패_유효성문제_score누락() throws Exception {
        //@valid -> @Preauthorized 순서로 동작

        //given
        //User, LongText 저장 + score는 500
        LongTextScoreRequest request = new LongTextScoreRequest(); // Score 누락

        //when
        mockMvc.perform(post("/long-text/" + longTextId  + "/score")
                .with(httpBasic("testid", "1111"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.score").value("must not be null"));

    }

    @Test //로그인안됨 -> 401
    void 긴글점수_저장_실패_인증문제() throws Exception {
        //given
        //User, LongText 저장 + score는 500
        LongTextScoreRequest request = new LongTextScoreRequest(500);

        //when&then
        mockMvc.perform(post("/long-text/" + longTextId  + "/score")
                .with(httpBasic("testid", "2222"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized());
    }

    @Test //로그인안됨 -> 401
    void 긴글점수_저장_실패_존재하지않는_longTextId() throws Exception {
        //given
        //User, LongText 저장 + score는 500
        LongTextScoreRequest request = new LongTextScoreRequest(500);
        //when&then
        mockMvc.perform(post("/long-text/" + 100 + "/score")
                .with(httpBasic("testid", "1111"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("존재하지 않는 긴글입니다."));
    }
}
