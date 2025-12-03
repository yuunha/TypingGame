// package hello.typing_game_be.rainingGameScore;
//
// import static org.assertj.core.api.AssertionsForClassTypes.*;
// import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
// import java.util.List;
// import java.util.Map;
//
//
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.MediaType;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.transaction.annotation.Transactional;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
//
// import hello.typing_game_be.common.security.CustomUserDetails;
// import hello.typing_game_be.friend.repository.FriendRequestRepository;
// import hello.typing_game_be.myLongText.repository.MyLongTextRepository;
// import hello.typing_game_be.rainingGameScore.dto.RainingGameScoreRequest;
// import hello.typing_game_be.rainingGameScore.entity.RainingGameScore;
// import hello.typing_game_be.rainingGameScore.repository.RainingGameScoreRepository;
// import hello.typing_game_be.user.entity.User;
// import hello.typing_game_be.user.repository.UserRepository;
//
// @SpringBootTest
// @AutoConfigureMockMvc
// @Transactional
// @ActiveProfiles("test")
// public class RainigGameScoreControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
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
//    private Authentication auth_A;
//
//    Authentication registerUser(String nickname, String providerId, String provider) {
//         User user = userRepository.save(User.builder()
//             .nickname(nickname)
//             .providerId(providerId)
//             .provider(provider)
//             .build()
//         );
//         // CustomUserDetails 생성
//         CustomUserDetails customUser = new CustomUserDetails(user, Map.of());
//
//         // Authentication 객체 생성
//         Authentication auth = new UsernamePasswordAuthenticationToken(
//             customUser, null, customUser.getAuthorities()
//         );
//
//         return auth;
//    }
//
//    @BeforeEach
//    void beforeEach() {
//        //1. DB 초기화
//        myLongTextRepository.deleteAll();
//        friendRequestRepository.deleteAll();
//        rainingGameScoreRepository.deleteAll();
//        userRepository.deleteAll();
//
//        //2. 테스트용 유저 저장
//        auth_A = registerUser("user1","11111","kakao");
//
//    }
//
//    @Test
//    void 비게임점수_저장_성공() throws Exception {
//
//        RainingGameScoreRequest request = new RainingGameScoreRequest(100);
//
//        mockMvc.perform(post("/raining-game-score")
//                .with(authentication(auth_A))
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
//                .with(authentication(auth_A))
//                .contentType(MediaType.APPLICATION_JSON))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$[0].score").value(102))
//            .andExpect(jsonPath("$[1].score").value(101));
//
//
//    }
// }
