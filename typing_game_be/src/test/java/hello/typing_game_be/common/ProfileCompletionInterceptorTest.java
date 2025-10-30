package hello.typing_game_be.common;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.typing_game_be.common.security.CustomUserDetails;
import hello.typing_game_be.friendRequest.repository.FriendRequestRepository;
import hello.typing_game_be.myLongText.repository.MyLongTextRepository;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProfileCompletionInterceptorTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private MyLongTextRepository myLongTextRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach() {
        myLongTextRepository.deleteAll();
        friendRequestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    private User createAndSaveUser(String nickname, String providerId, String provider) {
        User user = User.builder()
                .nickname(nickname)
                .providerId(providerId)
                .provider(provider)
                .build();
        return userRepository.save(user);
    }

    @Test
    @DisplayName("닉네임이 없는 사용자가 일반 페이지 접근 시 /signup/nickname로 리다이렉트")
    void 닉네임없는사용자_일반페이지_접근() throws Exception {
        // given: 닉네임 없는 사용자 생성
        User user = createAndSaveUser(null, "11111", "KAKAO");
        CustomUserDetails userDetails = new CustomUserDetails(user, Map.of());

        // when & then
        mockMvc.perform(get("/long-text")
                     .with(user(userDetails)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/signup/nickname"))
                .andDo(print());
    }

    @Test
    @DisplayName("닉네임이 있는 사용자는 모든 페이지에 정상 접근 가능")
    void 닉네임있는사용자_모든페이지_접근() throws Exception {
        // given
        User user = createAndSaveUser("김김김", "11111", "KAKAO");
        CustomUserDetails userDetails = new CustomUserDetails(user, Map.of());
        // when & then
        mockMvc.perform(get("/long-text")
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("닉네임이 없어도 /signup 경로는 접근 가능")
    void 닉네임없는사용자_signup경로_접근() throws Exception {
        // given
        User user = createAndSaveUser(null, "11111", "KAKAO");
        CustomUserDetails userDetails = new CustomUserDetails(user, Map.of());

        // when & then
        mockMvc.perform(get("/signup/nickname")
                        .with(user(userDetails)))
                .andExpect(status().is4xxClientError()) // 404여도 괜찮음, 인터셉터가 막지 않았다는 것만 체크 용도
                .andDo(print());
    }
}
