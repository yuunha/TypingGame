package hello.typing_game_be.friendRequest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
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
import jakarta.persistence.EntityManager;

@SpringBootTest
@AutoConfigureMockMvc
// @Transactional
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
    @Autowired
    private EntityManager entityManager;

    private Long user1Id;
    private Long user2Id;
    private Long user3Id;

    @BeforeEach
    void beforeEach() {
        // 테스트 DB 초기화 (flush 생략 가능)
        friendRequestRepository.deleteAll();
        userRepository.deleteAll();


        //1. 테스트용 유저1,2 저장
        //패스워드 인코딩 과정이 필요하므로 userRepository 대신 userService 호출
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

        User user1 = userRepository.findByLoginId("user1").orElseThrow();
        User user2 = userRepository.findByLoginId("user2").orElseThrow();
        User user3 = userRepository.findByLoginId("user3").orElseThrow();
        user1Id = user1.getUserId();
        user2Id = user2.getUserId();
        user3Id = user3.getUserId();
    }

    @Test
    void 친구요청_성공() throws Exception {

        //유저2로의 친구 요청
        FriendRequestCreateRequest request = new FriendRequestCreateRequest(user2Id);

        //유저1 -> 유저2 친구 신청 (PENDING 상태)
        mockMvc.perform(post("/friend-requests")
                .with(httpBasic("user1", "1111" ))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());

        //DB에서 friend reqeust가 잘 저장되었는지 확인
        FriendRequest fr = friendRequestRepository.findByRequesterUserIdAndReceiverUserId(user1Id,user2Id).orElseThrow();
        //then
        assertEquals(user1Id, fr.getRequester().getUserId());
        assertEquals(user2Id, fr.getReceiver().getUserId());
        assertEquals(FriendRequestStatus.PENDING, fr.getStatus());

    }

    @Test
    void 친구요청_수락_성공() throws Exception {
        //given
        //유저1 -> 유저2 친구 신청
        User user1 = userRepository.findById(user1Id).orElseThrow();
        User user2 = userRepository.findById(user2Id).orElseThrow();
        friendRequestRepository.save(
            FriendRequest.builder()
                .requester(user1)
                .receiver(user2)
                .status(FriendRequestStatus.PENDING)
                .build()
        );

        //when&then
        //친구신청 조회
        FriendRequest fr = friendRequestRepository.findByRequesterUserIdAndReceiverUserId(user1Id,user2Id).orElseThrow();
        FriendRequestUpdateRequest updateRequest = new FriendRequestUpdateRequest("ACCEPT");

        //유저2가 유저1의 친구 신청 수락
        mockMvc.perform(patch("/friend-requests/{friendRequestId}",fr.getFriendRequestId())
                .with(httpBasic("user2", "2222" ))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk());

        //DB에서 friend reqeust의 status가 accept로 바뀌었는지 확인
        FriendRequest fr_ = friendRequestRepository.findByRequesterUserIdAndReceiverUserId(user1Id,user2Id).orElseThrow();
        //then
        assertEquals(user1Id, fr_.getRequester().getUserId());
        assertEquals(user2Id, fr_.getReceiver().getUserId());
        assertEquals(FriendRequestStatus.PENDING, fr.getStatus());
    }

    @Test
    void 보낸친구요청목록_조회_성공() throws Exception {
        //given

        User user1 = userRepository.findById(user1Id).orElseThrow();
        User user2 = userRepository.findById(user2Id).orElseThrow();
        User user3 = userRepository.findById(user3Id).orElseThrow();

        //유저1 -> 유저2 친구 신청
        friendRequestRepository.save(
            FriendRequest.builder()
                .requester(user1)
                .receiver(user2)
                .status(FriendRequestStatus.PENDING)
                .build()
        );
        //유저1 -> 유저3 친구 신청
        friendRequestRepository.save(
            FriendRequest.builder()
                .requester(user1)
                .receiver(user3)
                .status(FriendRequestStatus.PENDING)
                .build()
        );

        //유저1의 친구요청목록 조회
        mockMvc.perform(get("/friend-requests/sent")
                .with(httpBasic("user1", "1111" )))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].receiverName").value(user3.getUsername()))
            .andExpect(jsonPath("$[1].receiverName").value(user2.getUsername()));

    }



}
