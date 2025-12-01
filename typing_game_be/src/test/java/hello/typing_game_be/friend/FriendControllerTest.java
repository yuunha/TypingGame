package hello.typing_game_be.friend;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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

import hello.typing_game_be.common.exception.BusinessException;
import hello.typing_game_be.common.exception.ErrorCode;
import hello.typing_game_be.common.security.CustomUserDetails;
import hello.typing_game_be.friend.entity.Friend;
import hello.typing_game_be.friend.entity.FriendRequest;
import hello.typing_game_be.friend.repository.FriendRepository;
import hello.typing_game_be.friend.repository.FriendRequestRepository;
import hello.typing_game_be.friend.service.FriendService;
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
   private FriendService friendService;
   @Autowired
   private FriendRequestRepository friendRequestRepository;
   @Autowired
   private FriendRepository friendRepository;


   @BeforeEach
   void beforeEach() {
       friendRequestRepository.deleteAll();
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
   void 친구목록_조회_성공() throws Exception {
       //userA,userB,userC 저장
       Authentication auth = registerUser("userA", "11111", "KAKAO");
       registerUser("userB", "22222", "KAKAO");
       registerUser("userC", "33333", "KAKAO");

       User userA = userRepository.findByNickname("userA").orElseThrow(()->new BusinessException(
           ErrorCode.USER_NOT_FOUND));
       User userB = userRepository.findByNickname("userB").orElseThrow(()->new BusinessException(
           ErrorCode.USER_NOT_FOUND));
       User userC = userRepository.findByNickname("userC").orElseThrow(()->new BusinessException(
           ErrorCode.USER_NOT_FOUND));

       //userA의 친구 userB,userC 저장
       friendService.addFriend(userA,userB);
       friendService.addFriend(userA,userC);

       // userA 기준 친구 목록 조회
       mockMvc.perform(get("/friends")
               .with(authentication(auth)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[*].userId",
               containsInAnyOrder(
                   userB.getUserId().intValue(),
                   userC.getUserId().intValue()
               )
           ));
   }
   @Test
   void 친구삭제_성공() throws Exception {
       //given
       //userA,userB저장
       Authentication auth = registerUser("userA", "11111", "KAKAO");
       registerUser("userB", "22222", "KAKAO");

       User userA = userRepository.findByNickname("userA").orElseThrow(()->new BusinessException(
           ErrorCode.USER_NOT_FOUND));
       User userB = userRepository.findByNickname("userB").orElseThrow(()->new BusinessException(
           ErrorCode.USER_NOT_FOUND));

       //userA-userB 친구 관계 저장
       friendService.addFriend(userA,userB);
       List<Friend> friend = friendRepository.findAllByUserId(userA.getUserId());
       Long friendId = friend.get(0).getFriendId();

       //when
       // 유저1가 유저2과의 친구 요청 삭제
       mockMvc.perform(delete("/friends/{friendId}",friendId)
               .with(authentication(auth)))
           .andExpect(status().isNoContent());

       //then
       // DB에서 삭제되었는지 확인
       Optional<Friend> deletedFr = friendRepository.findById(friendId);
       assertThat(deletedFr).isEmpty();
   }
}
