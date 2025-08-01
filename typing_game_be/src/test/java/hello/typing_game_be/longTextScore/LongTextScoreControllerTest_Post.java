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

    String title ="별 헤는 밤";
    int score = 500;

    //user
    String username = "hong";
    String loginId = "testid";
    String password = "1111";

    private User user;
    private Long userId;

    private LongText longText1;
    private LongText longText2;
    private Long longTextId1;
    private Long longTextId2;

    @BeforeEach
    void beforeEach() {
        longTextScoreRepository.deleteAll();
        userRepository.deleteAll();
        longTextRepository.deleteAll();

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

        //긴글 저장
        longTextRepository.save(LongText.builder()
            .title("애국가")
            .content("동해물과 백두산이")
            .build()
        );
        longTextRepository.save(LongText.builder()
            .title("애국가2")
            .content("동해물과 백두산이2")
            .build()
        );

        // 긴글 객체, id 저장
        longText1 = longTextRepository.findByTitle("애국가").orElseThrow();
        longTextId1 = longText1.getLongTextId();

        longText2 = longTextRepository.findByTitle("애국가2").orElseThrow();
        longTextId2 = longText2.getLongTextId();

    }



    @Test
    @Transactional
    void 점수저장_성공() throws Exception {
        LongTextScoreRequest request = LongTextScoreRequest.builder()
            .longTextId(longTextId1)
            .score(score)
            .build();
        User user = userRepository.findByLoginId(loginId).orElseThrow();
        Long userId = user.getUserId();
        //when
        mockMvc.perform(post("/user/" + userId + "/long-score")
                .with(httpBasic(loginId, password))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());

        //then
        List<LongTextScore> savedScores = longTextScoreRepository.getLongScoreByUser_UserId(userId);
        LongTextScore savedScore = savedScores.get(0);
        assertThat(savedScore.getLongText().getTitle()).isEqualTo("애국가");
        assertThat(savedScore.getScore()).isEqualTo(score);
    }
    // @Test
    // void 점수저장_실패_유효성문제() throws Exception {
    //     //@valid -> @Preauthorized 순서로 동작
    //     //유효성 검사에 실패하면 컨트롤러 메서드 자체가 실행되지 않기 때문에 @PreAuthorize까지 가지 않는다.
    //     LongTextScoreRequest request = LongTextScoreRequest.builder()
    //         .title(title)
    //         .build();
    //     //when&then
    //     mockMvc.perform(post("/user/" + userId + "/long-score")
    //             .with(httpBasic(loginId, password))
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(request)))
    //         .andExpect(status().isBadRequest())
    //         .andExpect(jsonPath("$.score").value("must not be null"));
    //
    //     LongTextScoreRequest request1 = LongTextScoreRequest.builder()
    //         .score(score)
    //         .build();
    //
    //     //when&then
    //     mockMvc.perform(post("/user/" + userId + "/long-score")
    //             .with(httpBasic(loginId, password))
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(request1)))
    //         .andExpect(status().isBadRequest())
    //         .andExpect(jsonPath("$.title").value("must not be blank"));
    //
    // }
    //
    // @Test //로그인안됨 -> 401
    // void 점수저장_실패_인증문제() throws Exception {
    //     LongTextScoreRequest request = LongTextScoreRequest.builder()
    //         .title(title)
    //         .score(score)
    //         .build();
    //     //when&then
    //     mockMvc.perform(post("/user/" + userId + "/long-score")
    //             .with(httpBasic(loginId, "wrong"))
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(request)))
    //         .andExpect(status().isUnauthorized());
    // }
    //
    // @Test //로그인했지만,url의 유저id와 다름! -> 403
    // void 점수저장_실패_인가문제() throws Exception {
    //     LongTextScoreRequest request = LongTextScoreRequest.builder()
    //         .title(title)
    //         .score(score)
    //         .build();
    //     long invalidUserId = userId +1;
    //     //when&then
    //     mockMvc.perform(post("/user/" + invalidUserId + "/long-score")
    //             .with(httpBasic(loginId, password))
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(request)))
    //         .andExpect(status().isForbidden());
    // }
}
