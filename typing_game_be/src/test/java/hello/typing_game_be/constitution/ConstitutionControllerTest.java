package hello.typing_game_be.constitution;

import hello.typing_game_be.friend.repository.FriendRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.typing_game_be.myLongText.repository.MyLongTextRepository;
import hello.typing_game_be.user.repository.UserRepository;
import hello.typing_game_be.user.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class ConstitutionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private MyLongTextRepository myLongTextRepository;

//
//    @BeforeEach
//    void beforeEach() {
//        myLongTextRepository.deleteAll();
//        friendRequestRepository.deleteAll();
//        userRepository.deleteAll();
//
//        //유저1 저장
//        userService.register( new UserCreateRequest("홍길동","user1","1111"));
//    }
//
//    @Test
//    void 조문번호로_헌법조회_성공() throws Exception {
//
//        int articleIndex = 71; // 제71조
//
//        mockMvc.perform(get("/constitution/{articleIndex}", articleIndex)
//                .with(httpBasic("user1", "1111")))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.articleNumber").value("제71조"))
//            .andExpect(jsonPath("$.content").value("대통령이 궐위되거나 사고로 인하여 직무를 수행할 수 없을 때에는 국무총리, 법률이 정한 국무위원의 순서로 그 권한을 대행한다."));
//
//
//    }
}
