package hello.typing_game_be.longTextScore;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LongTextScoreControllerTest_Post {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private LongTextScoreRepository longTextScoreRepository;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private LongTextRepository longTextRepository;
//
//    @Autowired
//    private FriendRequestRepository friendRequestRepository;
//
//    private Long userId;
//    private Long longTextId;
//
//    @BeforeEach
//    void beforeEach() {
//        //1. DB 초기화
//        friendRequestRepository.deleteAll();
//        longTextScoreRepository.deleteAll();
//        userRepository.deleteAll();
//        longTextRepository.deleteAll();
//
//
//        //2. 테스트용 유저 저장 및 ID 저장
//        userService.register( new UserCreateRequest("admin","testid","1111"));
//        User user = userRepository.findByLoginId("testid").orElseThrow();
//        userId = user.getUserId();
//
//        //3. 긴글 저장 및 ID 저장
//        longTextRepository.save(LongText.builder()
//            .title("애국가")
//            .content("동해물과 백두산이")
//            .build()
//        );
//        LongText longText = longTextRepository.findByTitle("애국가").orElseThrow();
//        longTextId = longText.getLongTextId();
//
//    }
//    @AfterEach
//    void afterEach() {
//        longTextScoreRepository.deleteAll();
//        userRepository.deleteAll();
//        longTextRepository.deleteAll();
//    }
//
//    @Test
//    @Transactional
//    void 긴글점수_저장_성공() throws Exception {
//        //given
//        //User, LongText 저장 + score는 500
//        LongTextScoreRequest request = new LongTextScoreRequest(500);
//
//        //when
//        mockMvc.perform(post("/long-text/" + longTextId  + "/score")
//                .with(httpBasic("testid", "1111"))
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//            .andExpect(status().isCreated());
//
//        //then
//        List<LongTextScore> savedScores = longTextScoreRepository.findByUserId(userId);
//        LongTextScore savedScore = savedScores.get(0);
//        assertThat(savedScore.getLongText().getTitle()).isEqualTo("애국가");
//        assertThat(savedScore.getScore()).isEqualTo(500);
//    }
//    @Test
//    void 긴글점수_저장_실패_유효성문제_score누락() throws Exception {
//        //@valid -> @Preauthorized 순서로 동작
//
//        //given
//        //User, LongText 저장 + score는 500
//        LongTextScoreRequest request = new LongTextScoreRequest(); // Score 누락
//
//        //when
//        mockMvc.perform(post("/long-text/" + longTextId  + "/score")
//                .with(httpBasic("testid", "1111"))
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//            .andExpect(status().isBadRequest())
//            .andExpect(jsonPath("$.score").value("must not be null"));
//
//    }
//
//    @Test //로그인안됨 -> 401
//    void 긴글점수_저장_실패_인증문제() throws Exception {
//        //given
//        //User, LongText 저장 + score는 500
//        LongTextScoreRequest request = new LongTextScoreRequest(500);
//
//        //when&then
//        mockMvc.perform(post("/long-text/" + longTextId  + "/score")
//                .with(httpBasic("testid", "2222"))
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//            .andExpect(status().isUnauthorized());
//    }
//
//    @Test //로그인안됨 -> 401
//    void 긴글점수_저장_실패_존재하지않는_longTextId() throws Exception {
//        //given
//        //User, LongText 저장 + score는 500
//        LongTextScoreRequest request = new LongTextScoreRequest(500);
//        //when&then
//        mockMvc.perform(post("/long-text/" + 100 + "/score")
//                .with(httpBasic("testid", "1111"))
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//            .andExpect(status().isNotFound())
//            .andExpect(jsonPath("$.message").value("존재하지 않는 긴글입니다."));
//    }
}
