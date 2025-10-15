package hello.typing_game_be.myLongTextScore;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class MyLongTextScoreControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private MyLongTextScoreRepository myLongTextScoreRepository;
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private MyLongTextRepository myLongTextRepository;
//    @Autowired
//    private FriendRequestRepository friendRequestRepository;
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private User user;
//    private Long userId;
//    private MyLongText myLongText;
//    private Long myLongTextId;
//
//    @BeforeEach
//    void beforeEach() {
//        //1. DB 초기화
//        friendRequestRepository.deleteAll();
//        myLongTextScoreRepository.deleteAll();
//        myLongTextRepository.deleteAll();
//        userRepository.deleteAll();
//
//        //2. 테스트용 유저 저장 및 ID 저장
//        userService.register( new UserCreateRequest("admin","testid","1111"));
//        user = userRepository.findByLoginId("testid").orElseThrow();
//        userId = user.getUserId();
//
//        //3. 긴글 저장 및 ID 저장
//        myLongTextRepository.save(MyLongText.builder()
//            .title("나의긴글")
//            .content("나의긴글입니다..")
//            .build()
//        );
//        myLongText = myLongTextRepository.findByTitle("나의긴글").orElseThrow();
//        myLongTextId = myLongText.getMyLongTextId();
//    }
//    @AfterEach
//    void afterEach() {
//        myLongTextScoreRepository.deleteAll();
//        myLongTextRepository.deleteAll();
//        userRepository.deleteAll();
//    }
//
//    @Test
//    void 나의긴글점수_저장_성공() throws Exception {
//        //given
//        MyLongTextScoreRequest request = new MyLongTextScoreRequest(500);
//
//        //when&then
//        mockMvc.perform(post("/my-long-text/" + myLongTextId  + "/score")
//                .with(httpBasic("testid", "1111"))
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//            .andExpect(status().isCreated());
//
//        List<MyLongTextScore> savedScores = myLongTextScoreRepository.findByUserUserId(userId);
//        MyLongTextScore savedScore = savedScores.get(0);
//        assertThat(savedScore.getMyLongText().getTitle()).isEqualTo("나의긴글");
//        assertThat(savedScore.getScore()).isEqualTo(500);
//    }
//    @Test
//    void 특정_나의긴글에대한_최고점수_조회_성공() throws Exception {
//        //given
//        //긴글에 대한 점수 저장
//        myLongTextScoreRepository.save(
//            MyLongTextScore.builder()
//                .user(user)
//                .myLongText(myLongText)
//                .score(100)
//                .build()
//        );
//        myLongTextScoreRepository.save(
//            MyLongTextScore.builder()
//                .user(user)
//                .myLongText(myLongText)
//                .score(200)
//                .build()
//        );
//
//        //when&then
//        mockMvc.perform(get("/my-long-text/" + myLongTextId  + "/score")
//                .with(httpBasic("testid", "1111"))
//                .contentType(MediaType.APPLICATION_JSON))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.score").value(200));
//    }
}
