package hello.typing_game_be.rainingGameScore;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class RainigGameScoreControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private RainingGameScoreRepository rainingGameScoreRepository;
//    @Autowired
//    private FriendRequestRepository friendRequestRepository;
//    @Autowired
//    private MyLongTextRepository myLongTextRepository;
//
//    private User user;
//    private Long userId;
//
//    @BeforeEach
//    void beforeEach() {
//        //1. DB 초기화
//        myLongTextRepository.deleteAll();
//        friendRequestRepository.deleteAll();
//        rainingGameScoreRepository.deleteAll();
//        userRepository.deleteAll();
//
//        //2. 테스트용 유저 저장 및 ID 저장
//        userService.register(new UserCreateRequest("admin","testid","1111"));
//        user = userRepository.findByLoginId("testid").orElseThrow();
//        userId = user.getUserId();
//    }
//    @Test
//    void 비게임점수_저장_성공() throws Exception {
//
//        RainingGameScoreRequest request = new RainingGameScoreRequest(100);
//
//        mockMvc.perform(post("/raining-game-score")
//                .with(httpBasic("testid", "1111"))
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//            .andExpect(status().isCreated());
//
//        List<RainingGameScore> savedScores = rainingGameScoreRepository.findByUserUserIdOrderByCreatedAtDesc(userId);
//        assertThat(savedScores.get(0).getScore()).isEqualTo(100);
//    }
//
//    @Test
//    void 비게임점수_조회_성공() throws Exception {
//        rainingGameScoreRepository.save(
//            RainingGameScore.builder()
//                .user(user)
//                .score(101)
//                .build()
//        );
//        rainingGameScoreRepository.save(
//            RainingGameScore.builder()
//                .user(user)
//                .score(102)
//                .build()
//        );
//
//        mockMvc.perform(get("/raining-game-score")
//                .with(httpBasic("testid", "1111"))
//                .contentType(MediaType.APPLICATION_JSON))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$[0].score").value(102))
//            .andExpect(jsonPath("$[1].score").value(101));
//
//
//    }
}
