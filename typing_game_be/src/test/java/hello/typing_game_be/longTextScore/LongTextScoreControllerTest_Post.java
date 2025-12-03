package hello.typing_game_be.longTextScore;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Map;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.typing_game_be.common.security.CustomUserDetails;
import hello.typing_game_be.friend.repository.FriendRequestRepository;
import hello.typing_game_be.longText.entity.LongText;
import hello.typing_game_be.longText.repository.LongTextRepository;
import hello.typing_game_be.longTextScore.dto.LongTextScoreRequest;
import hello.typing_game_be.longTextScore.entity.LongTextScore;
import hello.typing_game_be.longTextScore.repository.LongTextScoreRepository;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import hello.typing_game_be.user.service.UserService;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
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

   @Autowired
   private FriendRequestRepository friendRequestRepository;

   private Long longTextId;
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
       longTextScoreRepository.deleteAll();
       userRepository.deleteAll();
       longTextRepository.deleteAll();

       //2. 테스트용 유저 저장 및 ID 저장
       auth_A = registerUser("user1","11111","kakao");
       User user = userRepository.findByNickname("user1").orElseThrow();

       //3. 긴글 저장 및 ID 저장
       longTextRepository.save(LongText.builder()
           .title("애국가")
           .content("동해물과 백두산이")
           .build()
       );
       LongText longText = longTextRepository.findByTitle("애국가").orElseThrow();
       longTextId = longText.getLongTextId();

   }


   @Test
   @Transactional
   void 긴글점수_저장_성공() throws Exception {
       //given
       //User, LongText 저장 + score는 500
       LongTextScoreRequest request = new LongTextScoreRequest(500);
       User user = userRepository.findByNickname("user1").orElseThrow();

       //when
       mockMvc.perform(post("/long-text/" + longTextId  + "/score")
               .with(authentication(auth_A))
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(request)))
           .andExpect(status().isCreated());

       //then
       List<LongTextScore> savedScores = longTextScoreRepository.findByUserId(user.getUserId());
       LongTextScore savedScore = savedScores.get(0);
       assertThat(savedScore.getLongText().getTitle()).isEqualTo("애국가");
       assertThat(savedScore.getScore()).isEqualTo(500);
   }
   @Test
   void 긴글점수_저장_실패_유효성문제_score누락() throws Exception {
       //@valid -> @Preauthorized 순서로 동작

       //given
       //User, LongText 저장 + score는 500
       LongTextScoreRequest request = new LongTextScoreRequest(); // Score 누락

       //when
       mockMvc.perform(post("/long-text/" + longTextId  + "/score")
               .with(authentication(auth_A))
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(request)))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.score").value("must not be null"));

   }


   @Test
   void 긴글점수_저장_실패_존재하지않는_longTextId() throws Exception {
       //given
       //User, LongText 저장 + score는 500
       LongTextScoreRequest request = new LongTextScoreRequest(500);
       //when&then
       mockMvc.perform(post("/long-text/" + 100 + "/score")
               .with(authentication(auth_A))
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(request)))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath("$.message").value("존재하지 않는 긴글입니다."));
   }
}
