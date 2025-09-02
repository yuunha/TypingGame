package hello.typing_game_be.longText;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import hello.typing_game_be.friendRequest.repository.FriendRequestRepository;
import hello.typing_game_be.longText.entity.LongText;
import hello.typing_game_be.longText.repository.LongTextRepository;
import hello.typing_game_be.myLongText.repository.MyLongTextRepository;
import hello.typing_game_be.user.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LongTextControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private LongTextRepository longTextRepository;
    @Autowired
    private MyLongTextRepository myLongTextRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @BeforeEach
    void beforeEach() {
        longTextRepository.deleteAll();
        friendRequestRepository.deleteAll();
        myLongTextRepository.deleteAll();
        userRepository.deleteAll();
    }
    @AfterEach
    void afterEach() {
        longTextRepository.deleteAll();
    }
    String title = "제목";
    String content = "내용입니다\n내용입니다";

    @Test
    void 긴글목록조회_성공() throws Exception {
        //given
        longTextRepository.save(
            LongText.builder()
                .title(title)
                .content(content)
            .build());
        //when
        mockMvc.perform(get("/long-text"))
                //인증 없이 허용
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].title").value(title));
    }
    @Test
    void 긴글단건조회_성공() throws Exception {
        //given
        longTextRepository.save(
            LongText.builder()
                .title(title)
                .content(content)
                .build());
        Long longTextId = longTextRepository.findAll().get(0).getLongTextId();
        //when
        mockMvc.perform(get("/long-text/{longTextId}",longTextId))
            //인증 없이 허용
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value(title))
            .andExpect(jsonPath("$..content").value(content));
    }

}
