package hello.typing_game_be.constitution;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import hello.typing_game_be.constitution.dto.ProgressRequest;
import hello.typing_game_be.constitution.entity.ConstitutionProgress;
import hello.typing_game_be.constitution.repository.ConsitutionProgressRepository;
import hello.typing_game_be.friendRequest.repository.FriendRequestRepository;
import hello.typing_game_be.myLongText.repository.MyLongTextRepository;
import hello.typing_game_be.user.dto.UserCreateRequest;
import hello.typing_game_be.user.repository.UserRepository;
import hello.typing_game_be.user.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class ConstitutionProgressControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private MyLongTextRepository myLongTextRepository;
    @Autowired
    private ConsitutionProgressRepository consitutionProgressRepository;

    @BeforeEach
    void beforeEach() {
        myLongTextRepository.deleteAll();
        friendRequestRepository.deleteAll();
        userRepository.deleteAll();

        //유저1 저장
        userService.register( new UserCreateRequest("홍길동","user1","1111"));
    }
    @Test
    void 유저의_헌법_진행상황_저장() throws Exception {

        ProgressRequest request = new ProgressRequest(3,40); //3조 40번째 글자

        mockMvc.perform(post("/constitution/progress")
                .with(httpBasic("user1", "1111"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());

        ConstitutionProgress progress = consitutionProgressRepository.findByUser_LoginId("user1").orElseThrow();
        assertThat(progress.getArticleIndex()).isEqualTo(3);
        assertThat(progress.getLastPosition()).isEqualTo(40);

    }
}
