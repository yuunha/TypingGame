package hello.typing_game_be.myLongText;

import static org.assertj.core.api.AssertionsForClassTypes.*;
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

import hello.typing_game_be.common.security.CustomUserDetails;
import hello.typing_game_be.friend.repository.FriendRequestRepository;
import hello.typing_game_be.myLongText.dto.MyLongTextRequest;
import hello.typing_game_be.myLongText.entity.MyLongText;
import hello.typing_game_be.myLongText.repository.MyLongTextRepository;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MyLongTextControllerTest {

   @Autowired
   private MockMvc mockMvc;
   @Autowired
   UserRepository userRepository;
   @Autowired
   MyLongTextRepository myLongTextRepository;
   @Autowired
   private FriendRequestRepository friendRequestRepository;
   @Autowired
   ObjectMapper objectMapper;

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
       friendRequestRepository.deleteAll();
       myLongTextRepository.deleteAll();
       userRepository.deleteAll();

       auth_A = registerUser("user1","11111","kakao");
       User user = userRepository.findByNickname("user1").orElseThrow();
       //테스트용 유저 저장
   }

   @Test//유저가 긴글 저장
   void 나의긴글_저장_성공() throws Exception {
       //given
       //유저 조회
       User user= userRepository.findByNickname("user1").orElseThrow();

       MyLongTextRequest request = MyLongTextRequest.builder()
           .title("애국가")
           .content("동해물과 백두산이\n")
           .build();

       //when&then
       mockMvc.perform(post("/my-long-text")
               .with(authentication(auth_A))
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(request)))
           .andExpect(status().isCreated());

       MyLongText myLongText = myLongTextRepository.findByUser(user).get(0);
       assertThat(myLongText.getTitle()).isEqualTo("애국가");
       assertThat(myLongText.getContent()).isEqualTo("동해물과 백두산이\n");
   }
   @Test
   void 나의긴글_삭제_성공() throws Exception {
       //given
       //유저 조회
       User user= userRepository.findByNickname("user1").orElseThrow();
       //유저가 나의긴글 저장
       MyLongText myLongText = myLongTextRepository.save(
           MyLongText.builder()
               .user(user)
               .title("애국가")
               .content("동해물과 백두산이\n")
           .build()
       );
       //when&then
       mockMvc.perform(delete("/my-long-text/{myLongTextId}",myLongText.getMyLongTextId())
           .with(authentication(auth_A)))
           .andExpect(status().isOk());

   }
   @Test
   void 나의긴글_삭제_실패_존재하지않는글_and_권한문제() throws Exception {
       //given
       //유저 조회
       User user= userRepository.findByNickname("user1").orElseThrow();
       //유저가 나의긴글 저장
       MyLongText myLongText = myLongTextRepository.save(
           MyLongText.builder()
               .user(user)
               .title("애국가")
               .content("동해물과 백두산이\n")
               .build()
       );
       //유저2 저장
       Authentication auth_B=registerUser("admin2","testid2","11112");

       //when&then
       mockMvc.perform(delete("/my-long-text/100",myLongText.getMyLongTextId())
           .with(authentication(auth_A)))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath("$.message").value("존재하지 않는 '나의 긴글'입니다."));

       //유저2가 유저1의 글 삭제
       mockMvc.perform(delete("/my-long-text/{myLongTextId}",myLongText.getMyLongTextId())
           .with(authentication(auth_B)))
           .andExpect(status().isForbidden());

   }
   @Test
   void 나의긴글목록_조회_성공() throws Exception {
       //given
       //유저 조회
       User user= userRepository.findByNickname("user1").orElseThrow();
       //유저가 나의긴글 저장
       myLongTextRepository.save(
           MyLongText.builder()
               .user(user)
               .title("애국가1")
               .content("동해물과 백두산이\n")
               .build()
       );
       myLongTextRepository.save(
           MyLongText.builder()
               .user(user)
               .title("애국가2")
               .content("동해물과 백두산이\n")
               .build()
       );
       //when&then
       mockMvc.perform(get("/my-long-text")
           .with(authentication(auth_A)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].title").value("애국가1"))
           .andExpect(jsonPath("$[1].title").value("애국가2"));
   }
   @Test
   void 나의긴글_단건조회_성공() throws Exception {
       //given
       //유저 조회
       User user= userRepository.findByNickname("user1").orElseThrow();
       //유저가 나의긴글 저장
       MyLongText myLongText = myLongTextRepository.save(
           MyLongText.builder()
               .user(user)
               .title("애국가")
               .content("동해물과 백두산이\n")
               .build()
       );

       //when&then
       mockMvc.perform(get("/my-long-text/{myLongTextId}",myLongText.getMyLongTextId())
           .with(authentication(auth_A)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.title").value("애국가"))
           .andExpect(jsonPath("$.content").value("동해물과 백두산이\n"));
   }
}