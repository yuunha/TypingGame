package hello.typing_game_be.friendRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
import hello.typing_game_be.friendRequest.dto.FriendRequestCreateRequest;
import hello.typing_game_be.friendRequest.entity.FriendRequest;
import hello.typing_game_be.friendRequest.entity.FriendRequestStatus;
import hello.typing_game_be.friendRequest.repository.FriendRequestRepository;
// import hello.typing_game_be.myLongText.repository.MyLongTextRepository;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import hello.typing_game_be.user.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
// @Transactional
@ActiveProfiles("test")
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
       // 테스트 DB 초기화 (flush 생략 가능)
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
   void 친구요청_성공() throws Exception {

       // 상황 : user A -> user B 친구 요청

       // given
       // user A,B 엔티티 저장
       Authentication auth_A = registerUser("userA", "11111", "KAKAO");
       registerUser("userB", "22222", "KAKAO");
       User userA = userRepository.findByNickname("userA").orElseThrow(()->new BusinessException(
           ErrorCode.USER_NOT_FOUND));
       User userB = userRepository.findByNickname("userB").orElseThrow(()->new BusinessException(
           ErrorCode.USER_NOT_FOUND));

       //유저2로의 친구 요청 DTO
       FriendRequestCreateRequest request = new FriendRequestCreateRequest(userB.getUserId());

       // when & then
       mockMvc.perform(post("/friend-requests")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(request))
               .with(authentication(auth_A)))
           .andExpect(status().isCreated());

       //DB에서 friend reqeust가 잘 저장되었는지 확인
       FriendRequest fr = friendRequestRepository.findByRequesterUserIdAndReceiverUserId(userA.getUserId(),userB.getUserId()).orElseThrow();
       //then
       assertEquals(userA.getUserId(), fr.getRequester().getUserId());
       assertEquals(userB.getUserId(), fr.getReceiver().getUserId());
       assertEquals(FriendRequestStatus.PENDING, fr.getStatus());

   }

    @Test
    void 친구요청_실패_이미존재하는요청() throws Exception {
        //given
        //유저A -> 유저B 친구 신청

        Authentication auth_A = registerUser("userA", "11111", "KAKAO");
        Authentication auth_B = registerUser("userB", "22222", "KAKAO");
        User userA = userRepository.findByNickname("userA").orElseThrow(()->new BusinessException(
            ErrorCode.USER_NOT_FOUND));
        User userB = userRepository.findByNickname("userB").orElseThrow(()->new BusinessException(
            ErrorCode.USER_NOT_FOUND));

        friendRequestRepository.save(
            FriendRequest.builder()
                .requester(userA)
                .receiver(userB)
                .status(FriendRequestStatus.PENDING)
                .build()
        );

        //유저1 -> 유저2 친구 신청
        FriendRequestCreateRequest request1 = new FriendRequestCreateRequest(userB.getUserId());
        mockMvc.perform(post("/friend-requests")
                .with(authentication(auth_A))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("이미 보낸 친구 요청이 존재합니다."));

        //유저2 -> 유저1 친구 신청
        FriendRequestCreateRequest request2 = new FriendRequestCreateRequest(userA.getUserId());
        mockMvc.perform(post("/friend-requests")

                .with(authentication(auth_B))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value( "상대방이 이미 친구 요청을 보냈습니다."));

    }


//
//    @Test
//    void 친구요청_수락_성공() throws Exception {
//        //given
//        //유저1 -> 유저2 친구 신청
//        User user1 = userRepository.findById(user1Id).orElseThrow();
//        User user2 = userRepository.findById(user2Id).orElseThrow();
//        friendRequestRepository.save(
//            FriendRequest.builder()
//                .requester(user1)
//                .receiver(user2)
//                .status(FriendRequestStatus.PENDING)
//                .build()
//        );
//
//        //when&then
//        //친구신청 조회
//        FriendRequest fr = friendRequestRepository.findByRequesterUserIdAndReceiverUserId(user1Id,user2Id).orElseThrow();
//        FriendRequestUpdateRequest updateRequest = new FriendRequestUpdateRequest("ACCEPT");
//
//        //유저2가 유저1의 친구 신청 수락
//        mockMvc.perform(patch("/friend-requests/{friendRequestId}",fr.getFriendRequestId())
//                .with(httpBasic("user2", "2222" ))
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(updateRequest)))
//            .andExpect(status().isOk());
//
//        //DB에서 friend reqeust의 status가 accept로 바뀌었는지 확인
//        FriendRequest fr_ = friendRequestRepository.findByRequesterUserIdAndReceiverUserId(user1Id,user2Id).orElseThrow();
//        //then
//        assertEquals(user1Id, fr_.getRequester().getUserId());
//        assertEquals(user2Id, fr_.getReceiver().getUserId());
//        assertEquals(FriendRequestStatus.PENDING, fr.getStatus());
//    }
//
//    @Test
//    void 보낸친구요청목록_조회_성공() throws Exception {
//        //given
//        User user1 = userRepository.findById(user1Id).orElseThrow();
//        User user2 = userRepository.findById(user2Id).orElseThrow();
//        User user3 = userRepository.findById(user3Id).orElseThrow();
//
//        //유저1 -> 유저2 친구 신청
//        friendRequestRepository.save(
//            FriendRequest.builder()
//                .requester(user1)
//                .receiver(user2)
//                .status(FriendRequestStatus.PENDING)
//                .build()
//        );
//        //유저1 -> 유저3 친구 신청
//        friendRequestRepository.save(
//            FriendRequest.builder()
//                .requester(user1)
//                .receiver(user3)
//                .status(FriendRequestStatus.PENDING)
//                .build()
//        );
//
//        //유저1의 친구요청목록 조회
//        mockMvc.perform(get("/friend-requests/sent")
//                .with(httpBasic("user1", "1111" )))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.length()").value(2))
//            .andExpect(jsonPath("$[0].receiverName").value(user3.getNickname()))
//            .andExpect(jsonPath("$[1].receiverName").value(user2.getNickname()));
//
//    }
//
//    @Test
//    void 받은친구요청목록_조회_성공() throws Exception {
//        //given
//
//        User user1 = userRepository.findById(user1Id).orElseThrow();
//        User user2 = userRepository.findById(user2Id).orElseThrow();
//        User user3 = userRepository.findById(user3Id).orElseThrow();
//
//        //유저2 -> 유저1 친구 신청
//        friendRequestRepository.save(
//            FriendRequest.builder()
//                .requester(user2)
//                .receiver(user1)
//                .status(FriendRequestStatus.PENDING)
//                .build()
//        );
//        //유저3 -> 유저1 친구 신청
//        friendRequestRepository.save(
//            FriendRequest.builder()
//                .requester(user3)
//                .receiver(user1)
//                .status(FriendRequestStatus.PENDING)
//                .build()
//        );
//
//        //유저1의 친구요청목록 조회
//        mockMvc.perform(get("/friend-requests/received")
//                .with(httpBasic("user1", "1111" )))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.length()").value(2))
//            .andExpect(jsonPath("$[0].requesterName").value(user3.getNickname()))
//            .andExpect(jsonPath("$[1].requesterName").value(user2.getNickname()));
//
//    }


}
