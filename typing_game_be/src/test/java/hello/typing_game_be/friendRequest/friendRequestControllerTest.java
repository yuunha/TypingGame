package hello.typing_game_be.friendRequest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.typing_game_be.friendRequest.dto.FriendRequestCreateRequest;
import hello.typing_game_be.friendRequest.dto.FriendRequestUpdateRequest;
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
public class friendRequestControllerTest {

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

    @BeforeEach
    void beforeEach() {
        //1. 테스트용 유저1,2 저장
        userService.register(   //패스워드 인코딩 과정이 필요하므로 userRepository 대신 userService 호출
            UserCreateRequest.builder()
                .username("admin1")
                .loginId("testid1")
                .password("1111")
                .build()
        );
        userService.register(
            UserCreateRequest.builder()
                .username("admin2")
                .loginId("testid2")
                .password("2222")
                .build()
        );
    }

    @Test
    void 친구요청_성공() throws Exception {
        //given
        User requester = userRepository.findByLoginId("testid1").orElseThrow();
        User receiver = userRepository.findByLoginId("testid2").orElseThrow();

        //when
        FriendRequestCreateRequest request = new FriendRequestCreateRequest(receiver.getUserId());

        //유저1 -> 유저2 친구 신청
        mockMvc.perform(post("/friend-requests")
                .with(httpBasic("testid1", "1111" ))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());

        FriendRequest fr = friendRequestRepository.findByRequesterAndReceiver(requester,receiver).orElseThrow();
        //then
        assertEquals(requester.getUserId(), fr.getRequester().getUserId());
        assertEquals(receiver.getUserId(), fr.getReceiver().getUserId());
        assertEquals(FriendRequestStatus.PENDING, fr.getStatus());

    }

    @Test
    void 친구요청_수락_성공() throws Exception {
        //given
        User requester = userRepository.findByLoginId("testid1").orElseThrow();
        User receiver = userRepository.findByLoginId("testid2").orElseThrow();

        friendRequestRepository.save(
            FriendRequest.builder()
                .requester(requester)
                .receiver(receiver)
                .status(FriendRequestStatus.PENDING)
                .build()
        );

        //when
        FriendRequest fr = friendRequestRepository.findByRequesterAndReceiver(requester,receiver).orElseThrow();
        FriendRequestUpdateRequest updateRequest = new FriendRequestUpdateRequest("ACCEPT");
        //유저2가 유저1의 친구 신청을 수락
        mockMvc.perform(patch("/friend-requests/{friendRequestId}",fr.getFriendRequestId())
                .with(httpBasic("testid2", "2222" ))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk());

    }
}
