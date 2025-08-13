package hello.typing_game_be.myLongText;

import static org.assertj.core.api.Assertions.*;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.typing_game_be.myLongText.dto.MyLongTextRequest;
import hello.typing_game_be.myLongText.entity.MyLongText;
import hello.typing_game_be.myLongText.repository.MyLongTextRepository;
import hello.typing_game_be.user.dto.UserCreateRequest;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import hello.typing_game_be.user.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class MyLongTextControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MyLongTextRepository myLongTextRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach() {
        myLongTextRepository.deleteAll();
        userRepository.deleteAll();

        //테스트용 유저 저장
        userService.register(   //패스워드 인코딩 과정이 필요하므로 userRepository 대신 userService 호출
            UserCreateRequest.builder()
                .username("admin1")
                .loginId("testid")
                .password("1111")
                .build()
        );
    }
    @AfterEach
    void afterEach() {
        myLongTextRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test//유저가 긴글 저장
    void 나의긴글_저장_성공() throws Exception {
        //given
        //유저 조회
        User user= userRepository.findByLoginId("testid").orElseThrow();

        MyLongTextRequest request = MyLongTextRequest.builder()
            .title("애국가")
            .content("동해물과 백두산이\n")
            .build();

        //when&then
        mockMvc.perform(post("/my-long-text")
                .with(httpBasic("testid", "1111"))
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
        User user= userRepository.findByLoginId("testid").orElseThrow();
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
            .with(httpBasic("testid", "1111")))
            .andExpect(status().isOk());

    }
    @Test
    void 나의긴글_삭제_실패_존재하지않는글_and_권한문제() throws Exception {
        //given
        //유저 조회
        User user= userRepository.findByLoginId("testid").orElseThrow();
        //유저가 나의긴글 저장
        MyLongText myLongText = myLongTextRepository.save(
            MyLongText.builder()
                .user(user)
                .title("애국가")
                .content("동해물과 백두산이\n")
                .build()
        );
        //유저2 저장
        userService.register(   //패스워드 인코딩 과정이 필요하므로 userRepository 대신 userService 호출
            UserCreateRequest.builder()
                .username("admin2")
                .loginId("testid2")
                .password("11112")
                .build()
        );

        //when&then
        mockMvc.perform(delete("/my-long-text/100",myLongText.getMyLongTextId())
                .with(httpBasic("testid", "1111")))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("존재하지 않는 '나의 긴글'입니다."));

        //유저2가 유저1의 글 삭제
        mockMvc.perform(delete("/my-long-text/{myLongTextId}",myLongText.getMyLongTextId())
            .with(httpBasic("testid2", "11112")))
            .andExpect(status().isForbidden());

    }
    @Test
    void 나의긴글목록_조회_성공() throws Exception {
        //given
        //유저 조회
        User user= userRepository.findByLoginId("testid").orElseThrow();
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
                .with(httpBasic("testid", "1111")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title").value("애국가1"))
            .andExpect(jsonPath("$[1].title").value("애국가2"));
    }
    @Test
    void 나의긴글_단건조회_성공() throws Exception {
        //given
        //유저 조회
        User user= userRepository.findByLoginId("testid").orElseThrow();
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
                .with(httpBasic("testid", "1111")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("애국가"))
            .andExpect(jsonPath("$.content").value("동해물과 백두산이\n"));
    }
}