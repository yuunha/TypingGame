package hello.typing_game_be.constitution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.typing_game_be.constitution.repository.ConstitutionProgressRepository;
import hello.typing_game_be.friendRequest.repository.FriendRequestRepository;
import hello.typing_game_be.myLongText.repository.MyLongTextRepository;
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
    private ConstitutionProgressRepository constitutionProgressRepository;

//    @BeforeEach
//    void beforeEach() {
//        myLongTextRepository.deleteAll();
//        friendRequestRepository.deleteAll();
//        userRepository.deleteAll();
//
//        //유저1 저장
//        userService.register( new UserCreateRequest("홍길동","user1","1111"));
//    }
//    @Test
//    void 유저의_헌법_진행상황_저장_성공() throws Exception {
//
//        ProgressRequest request = new ProgressRequest(3,40); //3조 40번째 글자
//
//        mockMvc.perform(post("/constitution/progress")
//                .with(httpBasic("user1", "1111"))
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//            .andExpect(status().isOk());
//
//        ConstitutionProgress progress = constitutionProgressRepository.findByUser_Id("user1").orElseThrow();
//        assertThat(progress.getArticleIndex()).isEqualTo(3);
//        assertThat(progress.getLastPosition()).isEqualTo(40);
//
//    }
//    @Test
//    void 유저의_헌법_진행상황_조회_성공() throws Exception {
//
//        //given
//        User user = userRepository.findByLoginId("user1").get();
//
//        //유저1의 진행상황(1,100) 저장
//        constitutionProgressRepository.save(
//            ConstitutionProgress.builder()
//                .user(user)
//                .articleIndex(1)
//                .lastPosition(100)
//                .build()
//        );
//
//        //when&then
//        mockMvc.perform(get("/constitution/progress")
//                .with(httpBasic("user1", "1111")))
//            .andExpect(status().isOk())
//            .andDo(print())
//            .andExpect(jsonPath("$.articleIndex").value(1))
//            .andExpect(jsonPath("$.lastPosition").value(100));
//
//    }
}
