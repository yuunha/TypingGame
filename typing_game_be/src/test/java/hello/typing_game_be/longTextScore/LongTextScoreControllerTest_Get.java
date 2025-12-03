package hello.typing_game_be.longTextScore;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import hello.typing_game_be.common.security.CustomUserDetails;
import hello.typing_game_be.friend.repository.FriendRequestRepository;
import hello.typing_game_be.longText.entity.LongText;
import hello.typing_game_be.longText.repository.LongTextRepository;
import hello.typing_game_be.longTextScore.entity.LongTextScore;
import hello.typing_game_be.longTextScore.repository.LongTextScoreRepository;
import hello.typing_game_be.myLongText.repository.MyLongTextRepository;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import hello.typing_game_be.user.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LongTextScoreControllerTest_Get {

   @Autowired
   private MockMvc mockMvc;
   @Autowired
   private LongTextScoreRepository longTextScoreRepository;
   @Autowired
   private UserService userService;
   @Autowired
   private UserRepository userRepository;
   @Autowired
   private LongTextRepository longTextRepository;
   @Autowired
   private FriendRequestRepository friendRequestRepository;
   @Autowired
   private MyLongTextRepository myLongTextRepository;


   private Authentication auth_A;
   private Long longTextId1;
   private Long longTextId2;

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
       myLongTextRepository.deleteAll();
       longTextScoreRepository.deleteAll();
       userRepository.deleteAll();
       longTextRepository.deleteAll();

       //2. 테스트용 유저1,2 저장
       auth_A = registerUser("user1","11111","kakao");
       registerUser("user2","22222","kakao");

       User user1 = userRepository.findByNickname("user1").orElseThrow();
       User user2 = userRepository.findByNickname("user2").orElseThrow();

       //3. 긴글1,2 저장
       longTextRepository.save(LongText.builder()
           .title("애국가1")
           .content("동해물과 백두산이1")
           .build()
       );
       longTextRepository.save(LongText.builder()
           .title("애국가2")
           .content("동해물과 백두산이2")
           .build()
       );

       LongText longText1 = longTextRepository.findByTitle("애국가1").orElseThrow();
       LongText longText2 = longTextRepository.findByTitle("애국가2").orElseThrow();
       longTextId1 = longText1.getLongTextId();
       longTextId2 = longText2.getLongTextId();

       //4.유저1 - 긴글1의 점수(100,200) 저장
       //  유저2 - 긴글1,2의 점수 저장
       longTextScoreRepository.save(
           LongTextScore.builder()
               .user(user1)
               .longText(longText1)
               .score(100)
               .build()
       );
       longTextScoreRepository.save(
           LongTextScore.builder()
               .user(user1)
               .longText(longText1)
               .score(200)
               .build()
       );
       longTextScoreRepository.save(
           LongTextScore.builder()
               .user(user2)
               .longText(longText1)
               .score(300)
               .build()
       );
       longTextScoreRepository.save(
           LongTextScore.builder()
               .user(user2)
               .longText(longText1)
               .score(300)
               .build()
       );

   }

   @Test//유저1의 전체 점수 목록
   void 유저의_점수목록조회_성공() throws Exception {
       //when
       mockMvc.perform(get("/long-text/scores")
               .with(authentication(auth_A)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.data.length()").value(2))
           .andExpect(jsonPath("$.data[0].title").value("애국가1"))
           .andExpect(jsonPath("$.data[0].score").value(100))
           .andExpect(jsonPath("$.data[1].title").value("애국가1"))
           .andExpect(jsonPath("$.data[1].score").value(200));

   }

   //유저1의 긴글1에 대한 점수목록
   @Test
   void 유저가_특정글에대한_점수목록조회_성공() throws Exception {
       // given

       // when & then
       mockMvc.perform(get("/long-text/{longTextId}/scores", longTextId1)
               .with(authentication(auth_A)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.data[0].score").value(100))
           .andExpect(jsonPath("$.data[1].score").value(200));
   }

   //유저1의 긴글1에 대한 점수목록
   @Test
   void 유저가_특정글에대한_점수목록조회_실패_존재하지않는_longTextId() throws Exception {
       // when & then
       mockMvc.perform(get("/long-text/{longTextId}/scores", 1000)
               .with(authentication(auth_A)))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath("$.message").value("존재하지 않는 긴글입니다."));
   }


   @Test //유저1의 긴글1에 대한 최고점수 조회
   void 유저의_특정글에대한_최고점수조회_성공() throws Exception {
       // when & then
       mockMvc.perform(get("/long-text/{longTextId}/score", longTextId1)
               .with(authentication(auth_A)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.score").value(200));
   }

}