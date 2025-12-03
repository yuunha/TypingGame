package hello.typing_game_be.myLongTextScore;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.typing_game_be.common.security.CustomUserDetails;
import hello.typing_game_be.friend.repository.FriendRequestRepository;
import hello.typing_game_be.myLongText.entity.MyLongText;
import hello.typing_game_be.myLongText.repository.MyLongTextRepository;
import hello.typing_game_be.myLongTextScore.dto.MyLongTextScoreRequest;
import hello.typing_game_be.myLongTextScore.entity.MyLongTextScore;
import hello.typing_game_be.myLongTextScore.repository.MyLongTextScoreRepository;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class MyLongTextScoreControllerTest {

   @Autowired
   private MockMvc mockMvc;
   @Autowired
   private MyLongTextScoreRepository myLongTextScoreRepository;
   @Autowired
   private UserRepository userRepository;
   @Autowired
   private MyLongTextRepository myLongTextRepository;
   @Autowired
   private FriendRequestRepository friendRequestRepository;
   @Autowired
   private ObjectMapper objectMapper;

   private User user;
   private Long userId;
   private MyLongText myLongText;
   private Long myLongTextId;
   private Authentication auth_A;


    Authentication registerUser(String nickname, String providerId, String provider) {
        User user = userRepository.save(User.builder()
            .nickname(nickname)
            .providerId(providerId)
            .provider(provider)
            .build()
        );
        // CustomUserDetails 생성
        CustomUserDetails customUser = new CustomUserDetails(user, Map.of());

        // Authentication 객체 생성
        Authentication auth = new UsernamePasswordAuthenticationToken(
            customUser, null, customUser.getAuthorities()
        );

        return auth;
   }
   @BeforeEach
   void beforeEach() {
       //1. DB 초기화
       friendRequestRepository.deleteAll();
       myLongTextScoreRepository.deleteAll();
       myLongTextRepository.deleteAll();
       userRepository.deleteAll();

       //2. 테스트용 유저 저장 및 ID 저장
       auth_A = registerUser( "user1","11111","kakao");
       user = userRepository.findByNickname("user1").orElseThrow();
       userId = user.getUserId();

       //3. 긴글 저장 및 ID 저장
       myLongTextRepository.save(MyLongText.builder()
           .title("나의긴글")
           .content("나의긴글입니다..")
           .build()
       );
       myLongText = myLongTextRepository.findByTitle("나의긴글").orElseThrow();
       myLongTextId = myLongText.getMyLongTextId();
   }


   @Test
   void 나의긴글점수_저장_성공() throws Exception {
       //given
       MyLongTextScoreRequest request = new MyLongTextScoreRequest(500);

       //when&then
       mockMvc.perform(post("/my-long-text/" + myLongTextId  + "/score")
               .with(authentication(auth_A))
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(request)))
           .andExpect(status().isCreated());

       List<MyLongTextScore> savedScores = myLongTextScoreRepository.findByUserUserId(userId);
       MyLongTextScore savedScore = savedScores.get(0);
       assertThat(savedScore.getMyLongText().getTitle()).isEqualTo("나의긴글");
       assertThat(savedScore.getScore()).isEqualTo(500);
   }

   @Test
   void 특정_나의긴글에대한_최고점수_조회_성공() throws Exception {
       //given
       //긴글에 대한 점수 저장
       myLongTextScoreRepository.save(
           MyLongTextScore.builder()
               .user(user)
               .myLongText(myLongText)
               .score(100)
               .build()
       );
       myLongTextScoreRepository.save(
           MyLongTextScore.builder()
               .user(user)
               .myLongText(myLongText)
               .score(200)
               .build()
       );

       //when&then
       mockMvc.perform(get("/my-long-text/" + myLongTextId  + "/score")
               .with(authentication(auth_A))
               .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.score").value(200));
   }
}
