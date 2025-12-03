package hello.typing_game_be.constitution;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.typing_game_be.common.security.CustomUserDetails;
import hello.typing_game_be.constitution.dto.ProgressRequest;
import hello.typing_game_be.constitution.entity.ConstitutionProgress;
import hello.typing_game_be.constitution.repository.ConstitutionProgressRepository;
import hello.typing_game_be.friend.repository.FriendRequestRepository;
import hello.typing_game_be.myLongText.repository.MyLongTextRepository;
import hello.typing_game_be.user.entity.User;
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
       myLongTextRepository.deleteAll();
       friendRequestRepository.deleteAll();
       userRepository.deleteAll();

       //유저1 저장
       auth_A = registerUser( "user1","11111","kakao");
   }

   @Test
   void 유저의_헌법_진행상황_저장_성공() throws Exception {

       ProgressRequest request = new ProgressRequest(3,40); //3조 40번째 글자

       mockMvc.perform(post("/constitution/progress")
               .with(authentication(auth_A))
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(request)))
           .andExpect(status().isOk());

       User user = userRepository.findByNickname("user1").orElseThrow();

       ConstitutionProgress progress = constitutionProgressRepository.findByUser_UserId(user.getUserId()).orElseThrow();
       assertThat(progress.getArticleIndex()).isEqualTo(3);
       assertThat(progress.getLastPosition()).isEqualTo(40);

   }
   @Test
   void 유저의_헌법_진행상황_조회_성공() throws Exception {

       //given
       User user = userRepository.findByNickname("user1").get();

       //유저1의 진행상황(1,100) 저장
       constitutionProgressRepository.save(
           ConstitutionProgress.builder()
               .user(user)
               .articleIndex(1)
               .lastPosition(100)
               .build()
       );

       //when&then
       mockMvc.perform(get("/constitution/progress")
           .with(authentication(auth_A)))
           .andExpect(status().isOk())
           .andDo(print())
           .andExpect(jsonPath("$.articleIndex").value(1))
           .andExpect(jsonPath("$.lastPosition").value(100));

   }
}
