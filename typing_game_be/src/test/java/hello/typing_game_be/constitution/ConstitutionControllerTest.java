package hello.typing_game_be.constitution;

import hello.typing_game_be.common.security.CustomUserDetails;
import hello.typing_game_be.friend.repository.FriendRequestRepository;
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
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.typing_game_be.myLongText.repository.MyLongTextRepository;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import hello.typing_game_be.user.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class ConstitutionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private MyLongTextRepository myLongTextRepository;

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
       auth_A = registerUser( "user1","11111","kakao");   }

   @Test
   void 조문번호로_헌법조회_성공() throws Exception {

       int articleIndex = 71; // 제71조

       mockMvc.perform(get("/constitution/{articleIndex}", articleIndex)
           .with(authentication(auth_A)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.articleNumber").value("제71조"))
           .andExpect(jsonPath("$.content").value("대통령이 궐위되거나 사고로 인하여 직무를 수행할 수 없을 때에는 국무총리, 법률이 정한 국무위원의 순서로 그 권한을 대행한다."));


   }
}
