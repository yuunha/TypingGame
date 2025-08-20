package hello.typing_game_be.friend;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*; // get, post, patch, delete 등
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;   // status, jsonPath 등
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*; // httpBasic

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.typing_game_be.friendRequest.entity.FriendRequest;
import hello.typing_game_be.friendRequest.entity.FriendRequestStatus;
import hello.typing_game_be.friendRequest.repository.FriendRequestRepository;
import hello.typing_game_be.user.dto.UserCreateRequest;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import hello.typing_game_be.user.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class FriendControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private User user1;
    private User user2;
    private User user3;
    private FriendRequest friendRequest1;

    @BeforeEach
    void setUp() {
        friendRequestRepository.deleteAll();
        userRepository.deleteAll();

        userService.register(UserCreateRequest.builder()
            .username("홍길동")
            .loginId("user1")
            .password("1111")
            .build());

        userService.register(UserCreateRequest.builder()
            .username("홍길순")
            .loginId("user2")
            .password("2222")
            .build());

        userService.register(UserCreateRequest.builder()
            .username("홍길자")
            .loginId("user3")
            .password("3333")
            .build());

        user1 = userRepository.findByLoginId("user1").orElseThrow();
        user2 = userRepository.findByLoginId("user2").orElseThrow();
        user3 = userRepository.findByLoginId("user3").orElseThrow();

        // 친구 관계 생성 (user1 <-> user2)
        friendRequest1 =friendRequestRepository.save(FriendRequest.builder()
            .requester(user1)
            .receiver(user2)
            .status(FriendRequestStatus.ACCEPTED)
            .build());

        // 친구 요청 PENDING (user1 -> user3)
        friendRequestRepository.save(FriendRequest.builder()
            .requester(user1)
            .receiver(user3)
            .status(FriendRequestStatus.PENDING)
            .build());
    }

    @AfterEach
    void tearDown() {
        friendRequestRepository.deleteAll();
         userRepository.deleteAll();
    }

    @Test
    void 친구목록_조회_성공() throws Exception {
        // user1 기준 친구 목록 조회
        mockMvc.perform(get("/friends")
                .with(httpBasic("user1", "1111")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].userId").value(user2.getUserId()))
            .andExpect(jsonPath("$[0].username").value(user2.getUsername()));

    }
    @Test
    void 친구삭제_성공() throws Exception {
        //when
        // 유저1가 유저2과의 친구 요청 삭제
        mockMvc.perform(delete("/friends/{friendRequestId}", friendRequest1.getFriendRequestId())
                .with(httpBasic("user1", "1111")))
            .andExpect(status().isNoContent());

        //then
        // DB에서 삭제되었는지 확인
        Optional<FriendRequest> deletedFr = friendRequestRepository.findById(friendRequest1.getFriendRequestId());
        assertThat(deletedFr).isEmpty();
    }
}
