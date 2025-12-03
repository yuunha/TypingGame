package hello.typing_game_be.user;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import hello.typing_game_be.common.security.CustomUserDetails;
import hello.typing_game_be.user.dto.NicknameRequest;
import org.junit.jupiter.api.AfterEach;
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

import hello.typing_game_be.friend.repository.FriendRequestRepository;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;

import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void beforeEach() {
        friendRequestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

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

    @Test
    void 닉네임등록_실패_닉네임중복() throws Exception {
        // given
        // 기존 유저 등록
        registerUser("홍길동", "12345", "KAKAO");

        // 새로운 유저 생성
        Authentication auth = registerUser("임꺽정", "67890", "KAKAO");

        // request 객체 생성
        NicknameRequest request = new NicknameRequest();
        request.setNickname("홍길동");

        mockMvc.perform(post("/users/nickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) // Object → JSON
                        .with(authentication(auth)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 존재하는 nickname입니다."));
    }

    @Test
    void 회원조회_성공() throws Exception {
        // given: User 엔티티 저장
        Authentication auth = registerUser("홍길동", "12345", "KAKAO");

        // when & then
        mockMvc.perform(get("/users/me")
                        .with(authentication(auth))) // Principal 세팅
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("홍길동"));
    }

    //인증 관련 예외
    @Test
    void 회원조회_실패_인증없음() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().is3xxRedirection()); // OAuth2 로그인 리다이렉트
                // is3xxRedirection() : HTTP 상태 코드 300~399
    }

    @Test
    void 회원삭제_성공() throws Exception {
        //given
        Authentication auth = registerUser("홍길동", "12345", "KAKAO");

        //유저 삭제 요청
        mockMvc.perform(delete("/users/me")
                        .with(authentication(auth))) // Principal 세팅
                .andExpect(status().isOk());

        //DB에 유저가 삭제되었는지 확인
        boolean exists = userRepository.existsByNickname("홍길동");
        assertThat(exists).isFalse();
    }

    //인증 관련 예외
    @Test
    void 회원삭제_실패_인증없음() throws Exception {
        mockMvc.perform(delete("/users/me"))
                .andExpect(status().is3xxRedirection()); // 인증 실패
    }

    @Test
    void 회원삭제_실패_존재하지않는유저() throws Exception {
        Authentication auth = registerUser("홍길동", "12345", "KAKAO");

        // 먼저 삭제
        mockMvc.perform(delete("/users/me").with(authentication(auth)))
                .andExpect(status().isOk());

        // 다시 삭제 시도
        mockMvc.perform(delete("/users/me").with(authentication(auth)))
                .andExpect(status().isNotFound());
    }

    @Test
    void 회원검색_성공() throws Exception {
        //given: 유저1,2 등록
        Authentication auth1 = registerUser("홍길동", "11111", "KAKAO");
        Authentication auth2 = registerUser("홍길순", "22222", "KAKAO");

        // "홍길"로 부분 검색 시 결과 2개(loginId 일치여부 검증)
        mockMvc.perform(get("/users/search?nickname=홍길&page=0&size=5")
                        .with(authentication(auth1))) // Principal 세팅
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].nickname").value("홍길동"))
            .andExpect(jsonPath("$.content[1].nickname").value("홍길순"));
    }

    @Test
    void 회원검색_실패_파라미터누락() throws Exception {
        Authentication auth = registerUser("홍길동", "12345", "KAKAO");

        mockMvc.perform(get("/users/search").with(authentication(auth)))
                .andExpect(status().isBadRequest());
    }

}
