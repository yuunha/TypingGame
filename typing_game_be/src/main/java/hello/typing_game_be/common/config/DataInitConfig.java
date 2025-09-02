package hello.typing_game_be.common.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import hello.typing_game_be.common.exception.BusinessException;
import hello.typing_game_be.common.exception.ErrorCode;
import hello.typing_game_be.friendRequest.entity.FriendRequest;
import hello.typing_game_be.friendRequest.entity.FriendRequestStatus;
import hello.typing_game_be.friendRequest.repository.FriendRequestRepository;
import hello.typing_game_be.longText.entity.LongText;
import hello.typing_game_be.longText.repository.LongTextRepository;
import hello.typing_game_be.myLongText.entity.MyLongText;
import hello.typing_game_be.myLongText.repository.MyLongTextRepository;
import hello.typing_game_be.user.dto.UserCreateRequest;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import hello.typing_game_be.user.service.UserService;
import lombok.RequiredArgsConstructor;

@Configuration
//@Profile({"local", "dev"})
@RequiredArgsConstructor
public class DataInitConfig {

    private final UserService userService;
    private final LongTextRepository longTextRepository;
    private final MyLongTextRepository myLongTextRepository;
    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;
    String content1="동해물과 백두산이 마르고 닳도록\n"
        + "하느님이 보우하사 우리나라 만세\n"
        + "무궁화 삼천리 화려 강산\n"
        + "대한 사람 대한으로 길이 보전하세";

    String content2 =
        "여름 장이란 애시당초에 글러서, 해는 아직 중천에 있건만 장판은 벌써 쓸쓸하고 더운 햇발이 벌여놓은 전 휘장 밑으로 등줄기를 훅훅 볶는다."
            + "마을 사람들은 거지반 돌아간 뒤요, 팔리지 못한 나뭇군패가 길거리에 궁싯거리고들 있으나 석윳병이나 받고 고깃마리나 사면 족할 이 축들을 바라고 언제까지든지 버티고 있을 법은 없다. "
            + "춥춥스럽게 날아드는 파리떼도 장난군 각다귀들도 귀치않다."
            + "얽둑배기요 왼손잡이인 드팀전의 허생원은 기어코 동업의 조선달에게 낚아보았다.";
    String content3 =
        "제1조\n"
        + "1. 대한민국은 민주공화국이다.\n"
        + "2. 대한민국의 주권은 국민에게 있고, 모든 권력은 국민으로부터 나온다.\n";
    String content4 =
        "제2조\n"
            + "1. 대한민국의 국민이 되는 요건은 법률로 정한다.\n"
            + "2. 국가는 법률이 정하는 바에 의하여 재외국민을 보호할 의무를 진다.\n";





    @Bean
    @Order(1)
    CommandLineRunner initUsers() {
        return args -> {
            createUserIfNotExists("admin_name", "admin", "12345");
            createUserIfNotExists("홍길동", "dong", "1111");
            createUserIfNotExists("홍길순", "soon", "2222");
        };
    }

    @Bean
    @Order(2)
    CommandLineRunner initLongTexts() {
        return args -> {
            createLongTextIfNotExists("애국가",content1);
            createLongTextIfNotExists("메밀꽃 필 무렵",content2);
        };
    }

    @Bean
    @Order(3)
    CommandLineRunner initMyLongTexts() {
        return args -> {
            User user = userRepository.findByLoginId("admin").orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
            );

            createMyLongTextIfNotExists("헌법1조", content3, user);
            createMyLongTextIfNotExists("헌법2조", content4, user);

        };
    }

    @Bean
    @Order(4)
    CommandLineRunner initFriendRequests() {
        return args -> {
            User user1 = userRepository.findByLoginId("admin").orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
            );
            User user2 = userRepository.findByLoginId("dong").orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
            );
            User user3 = userRepository.findByLoginId("soon").orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
            );
            //user1 - user2 친구 (accepted)
            createFriendRequestIfNotExists(user1,user2,true);
            //user1 <- user3 친구 신청 (pending)
            createFriendRequestIfNotExists(user3,user1,false);
        };
    }




    private void createUserIfNotExists(String username, String loginId, String password) {
        if (!userRepository.existsByLoginId(loginId)) {
            UserCreateRequest request = UserCreateRequest.builder()
                .username(username)
                .loginId(loginId)
                .password(password)
                .build();
            userService.register(request);
        }
    }
    private void createLongTextIfNotExists(String title, String content) {
        if (!longTextRepository.existsByTitle(title)) {
            longTextRepository.save(
                LongText.builder()
                    .title(title)
                    .content(content)
                    .build()
            );
        }
    }

    private void createMyLongTextIfNotExists(String title, String content,User user) {
        if (!myLongTextRepository.existsByTitle(title)) {
            myLongTextRepository.save(
                MyLongText.builder()
                    .title(title)
                    .content(content)
                    .user(user)
                    .build()
            );
        }
    }

    private void createFriendRequestIfNotExists(User user1, User user2, boolean isAccepted) {

        if (!friendRequestRepository.existsByRequesterAndReceiver(user1, user2)&&
            !friendRequestRepository.existsByRequesterAndReceiver(user2, user1)){

            if(isAccepted) {
                friendRequestRepository.save(
                    FriendRequest.builder()
                        .requester(user2)
                        .receiver(user1)
                        .status(FriendRequestStatus.ACCEPTED)
                        .build());
            }else {
                friendRequestRepository.save(
                    FriendRequest.builder()
                        .requester(user2)
                        .receiver(user1)
                        .status(FriendRequestStatus.PENDING)
                        .build());
            }

        }

    }


}