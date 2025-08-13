package hello.typing_game_be.myLongTextScore;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.typing_game_be.longTextScore.entity.LongTextScore;
import hello.typing_game_be.myLongText.entity.MyLongText;
import hello.typing_game_be.myLongText.repository.MyLongTextRepository;
import hello.typing_game_be.myLongTextScore.dto.MyLongTextScoreRequest;
import hello.typing_game_be.myLongTextScore.entity.MyLongTextScore;
import hello.typing_game_be.myLongTextScore.repository.MyLongTextScoreRepository;
import hello.typing_game_be.myLongTextScore.service.MyLongTextScoreService;
import hello.typing_game_be.user.dto.UserCreateRequest;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import hello.typing_game_be.user.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MyLongTextScoreControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MyLongTextScoreRepository myLongTextScoreRepository;
    @Autowired
    private MyLongTextScoreService myLongTextScoreService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MyLongTextRepository myLongTextRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private Long userId;
    private Long myLongTextId;

    @BeforeEach
    void beforeEach() {
        //1. DB 초기화
        myLongTextScoreRepository.deleteAll();
        myLongTextRepository.deleteAll();
        userRepository.deleteAll();

        //2. 테스트용 유저 저장 및 ID 저장
        userService.register(   //패스워드 인코딩 과정이 필요하므로 userRepository 대신 userService 호출
            UserCreateRequest.builder()
                .username("admin")
                .loginId("testid")
                .password("1111")
                .build()
        );
        User user = userRepository.findByLoginId("testid").orElseThrow();
        userId = user.getUserId();

        //3. 긴글 저장 및 ID 저장
        myLongTextRepository.save(MyLongText.builder()
            .title("나의긴글")
            .content("나의긴글입니다..")
            .build()
        );
        MyLongText myLongText = myLongTextRepository.findByTitle("나의긴글").orElseThrow();
        myLongTextId = myLongText.getMyLongTextId();
    }
    @AfterEach
    void afterEach() {
        myLongTextScoreRepository.deleteAll();
        myLongTextRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void 나의긴글점수_저장_성공() throws Exception {
        //given
        MyLongTextScoreRequest request = new MyLongTextScoreRequest(500);

        //when&then
        mockMvc.perform(post("/my-long-text/" + myLongTextId  + "/score")
                .with(httpBasic("testid", "1111"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());

        List<MyLongTextScore> savedScores = myLongTextScoreRepository.findByUserUserId(userId);
        MyLongTextScore savedScore = savedScores.get(0);
        assertThat(savedScore.getMyLongText().getTitle()).isEqualTo("나의긴글");
        assertThat(savedScore.getScore()).isEqualTo(500);
    }
}
