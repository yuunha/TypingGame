package hello.typing_game_be.friend;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.typing_game_be.common.exception.BusinessException;
import hello.typing_game_be.common.exception.ErrorCode;
import hello.typing_game_be.common.security.CustomUserDetails;
import hello.typing_game_be.friend.dto.FriendRequestUpdateRequest;
import hello.typing_game_be.friend.entity.FriendRequest;
import hello.typing_game_be.friend.repository.FriendRequestRepository;
import hello.typing_game_be.friend.service.FriendRequestService;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import hello.typing_game_be.user.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
// @Transactional
@ActiveProfiles("test")
public class FriendManagementServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private FriendRequestService friendRequestService;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
        friendRequestRepository.deleteAll();
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
    void 친구요청_수락() throws Exception {

        // 상황 : user A -> user B 친구 요청
        // userB가 친구 요청을 수락

        // given
        // user A,B 엔티티 생성
        registerUser("userA", "11111", "KAKAO");
        Authentication auth_B = registerUser("userB", "22222", "KAKAO");
        User userA = userRepository.findByNickname("userA").orElseThrow(()->new BusinessException(
            ErrorCode.USER_NOT_FOUND));
        User userB = userRepository.findByNickname("userB").orElseThrow(()->new BusinessException(
            ErrorCode.USER_NOT_FOUND));

        //친구 요청 생성(userA->userB)
        Long friendRequestId = friendRequestService.sendFriendRequest(userA.getUserId(), userB.getUserId());

        //친구요청 수락 DTO
        FriendRequestUpdateRequest request = new FriendRequestUpdateRequest("ACCEPT");

        // when & then
        // userB가 친구요청 수락
        mockMvc.perform(post("/friend-requests/{friendRequestId}", friendRequestId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(authentication(auth_B)))
            .andExpect(status().isOk());

        //DB에서 friend reqeust가 삭제되었는지 확인
        //FriendRequest fr = friendRequestRepository.findByRequesterUserIdAndReceiverUserId(userA.getUserId(),userB.getUserId()).orElseThrow();
        //DB에서 friend 가 생성되었는지 확인
        //assertEquals(userA.getUserId(), fr.getRequester().getUserId());

    }

}
