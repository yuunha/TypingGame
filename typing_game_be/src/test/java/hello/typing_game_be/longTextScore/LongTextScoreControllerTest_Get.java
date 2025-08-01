package hello.typing_game_be.longTextScore;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.typing_game_be.longText.entity.LongText;
import hello.typing_game_be.longText.repository.LongTextRepository;
import hello.typing_game_be.longTextScore.dto.LongTextScoreRequest;
import hello.typing_game_be.longTextScore.entity.LongTextScore;
import hello.typing_game_be.longTextScore.repository.LongTextScoreRepository;
import hello.typing_game_be.longTextScore.service.LongTextScoreService;
import hello.typing_game_be.user.dto.UserRequest;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import hello.typing_game_be.user.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class LongTextScoreControllerTest_Get {

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
    private LongTextScoreService longTextScoreService;

    @Autowired
    private LongTextRepository longTextRepository;


    @BeforeEach
    void beforeEach() {

        //1. DB 초기화
        longTextScoreRepository.deleteAll();
        userRepository.deleteAll();
        longTextRepository.deleteAll();

        //2. 테스트용 유저 저장
        userService.register(   //패스워드 인코딩 과정이 필요하므로 userRepository 대신 userService 호출
            UserRequest.builder()
                .username("admin")
                .loginId("testid")
                .password("1111")
                .build()
        );
        User user = userRepository.findByLoginId("testid").orElseThrow();

        //3. 긴글 저장
        longTextRepository.save(LongText.builder()
            .title("애국가")
            .content("동해물과 백두산이")
            .build()
        );
        LongText longText = longTextRepository.findByTitle("애국가").orElseThrow();

        //4.긴글 점수 저장
        longTextScoreRepository.save(
            LongTextScore.builder()
                .user(user)
                .longText(longText)
                .score(500)
                .build()
        );
        //4.긴글 점수 저장
        longTextScoreRepository.save(
            LongTextScore.builder()
                .user(user)
                .longText(longText)
                .score(400)
                .build()
        );
    }

    @Test
    void 유저의_점수목록조회_성공() throws Exception {
        //when
        mockMvc.perform(get("/long-text/score")
                .with(httpBasic("testid", "1111" )))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].title").value("애국가"))
            .andExpect(jsonPath("$[0].score").value(500))
            .andExpect(jsonPath("$[1].title").value("애국가"))
            .andExpect(jsonPath("$[1].score").value(400));

    }

    @Test//로그인안됨 -> 401
    void 유저의_점수목록조회_실패_인증문제() throws Exception {
        //when&then
        mockMvc.perform(get("/long-text/score")
                .with(httpBasic("testid", "1112" )))
            .andExpect(status().isUnauthorized());
    }

    // @Test
    // void 긴글점수_순위_조회_성공() throws Exception {
    //     //given
    //     addUser_Score();
    //     //첫번째 글에 대한 2개의 점수와 유저이름 조회
    //     mockMvc.perform(get("/ranking/long-score")
    //             .param("title", title1)
    //             .with(httpBasic(loginId1, password1)))
    //         .andDo(print())
    //         .andExpect(status().isOk())
    //         .andExpect(jsonPath("$[0].score").value(600))
    //         .andExpect(jsonPath("$[0].username").value(username2))
    //         .andExpect(jsonPath("$[1].score").value(score1))
    //         .andExpect(jsonPath("$[1].username").value(username1));
    //
    // }
    // @Test
    // void 긴글점수_순위_조회_title누락() throws Exception {
    //     mockMvc.perform(get("/ranking/long-score")
    //         .with(httpBasic(loginId1, password1)))
    //         .andDo(print())
    //         .andExpect(status().isBadRequest());
    // }
    // @Test
    // void 긴글점수_랭킹_조회_존재하지않는_title() throws Exception {
    //     mockMvc.perform(get("/ranking/long-score")
    //             .param("title", "wrong")
    //             .with(httpBasic(loginId1, password1)))
    //         .andExpect(status().isNotFound());
    // }

}