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
import hello.typing_game_be.myLongTextScore.entity.MyLongTextScore;
import hello.typing_game_be.myLongTextScore.repository.MyLongTextScoreRepository;
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
    private final MyLongTextScoreRepository myLongTextScoreRepository;
    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;

    String content1="동해물과 백두산이 마르고 닳도록\n"
        + "하느님이 보우하사 우리나라 만세\n"
        + "무궁화 삼천리 화려 강산\n"
        + "대한 사람 대한으로 길이 보전하세";

    String content2 = """
        여름 장이란 애시당초에 글러서, 해는 아직 중천에 있건만 장판은 벌써 쓸쓸하고 더운 햇발이 벌여놓은 전 휘장 밑으로 등줄기를 훅훅 볶는다.
        마을 사람들은 거지반 돌아간 뒤요, 팔리지 못한 나뭇군패가 길거리에 궁싯거리고들 있으나 석윳병이나 받고 고깃마리나 사면 족할 이 축들을 바라고 언제까지든지 버티고 있을 법은 없다.
        춥춥스럽게 날아드는 파리떼도 장난군 각다귀들도 귀치않다.
        얽둑배기요 왼손잡이인 드팀전의 허생원은 기어코 동업의 조선달에게 낚아보았다.
        "그만 거둘까?"
        "잘 생각했네. 봉평장에서 한 번이나 흐붓하게 사 본 일 있었을까. 내일 대화장에 서나 한몫 벌어야겠네."
        """;

    String content3 =
        "Yesterday all my troubles"
        + "seemed so far away"
        + "Now it looks as though they're here to stay"
        + "Oh, I believe in yesterday"
        + "Suddenly I'm not half the man I used to be"
        + "There's a shadow hanging over me"
        + "Oh, yesterday came suddenly";

    String content4 =
        "Who knows how long I've loved you"
        + "You know I love you still"
        + "Will I wait a lonely lifetime"
        + "If you want me to, I will"
        + "For if I ever saw you I didn't catch your name"
        + "But it never really mattered I will always feel the same";

    String content5 =
            """
            계절이 지나가는 하늘에는
            가을로 가득차 있습니다.
            \s
            나는 아무 걱정도 없이
            가을 속의 별들을 다 헤일 듯합니다.
            \s
            가슴 속에 하나 둘 새겨지는 별을
            이제 다 못 헤는 것은
            쉬이 아침이 오는 까닭이요,
            내일 밤이 남은 까닭이요,
            아직 나의 청춘이 다하지 않은 까닭입니다.
            \s
            별 하나에 추억과
            별 하나에 사랑과
            별 하나에 쓸쓸함과
            별 하나에 동경과
            별 하나에 시와
            별 하나에 어머니, 어머니.
            \s
            어머님, 나는 별 하나에 아름다운 말 한마디씩 불러 봅니다. 소학교 때 책상을 같이 했던 아이들의 이름과 패, 경, 옥 이런 이국 소녀들의 이름과, 벌써 애기 어머니 된 계집애들의 이름과, 가난한 이웃사람들의 이름과, 비둘기 강아지, 토끼, 노새, 노루, 프란시스 잼, 라이너 마리아 릴케, 이런 시인들의 이름을 불러 봅니다.
            \s
            이네들은 너무나 멀리 있습니다.
            별이 아스라이 멀 듯이
            \s
            어머님,
            그리고 당신은 멀리 북간도에 계십니다.
            \s
            나는 무엇인지 그리워
            이 많은 별빛이 내린 언덕 위에
            내 이름자를 써 보고
            흙으로 덮어 버리었습니다.
            \s
            따는 밤을 새워 우는 벌레는
            부끄러운 이름을 슬퍼하는 까닭입니다.
            \s
            그러나 겨울이 지나고 나의 별에도 봄이 오면
            무덤 위에 파란 잔디가 피어나듯이
            내 이름자 묻힌 언덕 위에도
            자랑처럼 풀이 무성할 거외다.
            """;




    @Bean
    @Order(1)
    CommandLineRunner initUsers() {
        return args -> {
            createUserIfNotExists("admin", "11111");
            createUserIfNotExists("dong", "22222");
            createUserIfNotExists("soon", "33333");
            createUserIfNotExists("hwa", "44444");

        };
    }

    @Bean
    @Order(2)
    CommandLineRunner initLongTexts() {
        return args -> {
            createLongTextIfNotExists("애국가",content1);
            createLongTextIfNotExists("메밀꽃 필 무렵",content2);
            createLongTextIfNotExists("별 헤는 밤",content5);
        };
    }

    @Bean
    @Order(3)
    CommandLineRunner initMyLongTexts() {
        return args -> {
            User user = userRepository.findByNickname("admin").orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
            );

            createMyLongTextIfNotExists("Yesterday - 비틀즈", content3, user);
            createMyLongTextIfNotExists("I Will - 비틀즈", content4, user);

        };
    }

    @Bean
    @Order(4)
    CommandLineRunner initMyLongTextScore() {
        return args -> {
            User user = userRepository.findByNickname("admin").orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
            );
            MyLongText myLongText1 = myLongTextRepository.findByTitle("Yesterday - 비틀즈").orElseThrow(
                () -> new BusinessException(ErrorCode.MY_LONG_TEXT_NOT_FOUND)
            );
            MyLongText myLongText2 = myLongTextRepository.findByTitle("I Will - 비틀즈").orElseThrow(
                () -> new BusinessException(ErrorCode.MY_LONG_TEXT_NOT_FOUND)
            );

            createMyLongTextScore(myLongText1,user,200);
            createMyLongTextScore(myLongText1,user,250);
        };
    }

    @Bean
    @Order(5)
    CommandLineRunner initFriendRequests() {
        return args -> {
            User user1 = userRepository.findByNickname("admin").orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
            );
            User user2 = userRepository.findByNickname("dong").orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
            );
            User user3 = userRepository.findByNickname("soon").orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
            );
            User user4 = userRepository.findByNickname("hwa").orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
            );
            //user1 - user2 친구 (accepted)
            createFriendRequestIfNotExists(user1,user2,true);
            //user3 -> user1 친구 신청 (pending)
            createFriendRequestIfNotExists(user3,user1,false);
            //user1 -> user4 친구 신청 (pending)
            createFriendRequestIfNotExists(user1,user4,false);

        };
    }




    private void createUserIfNotExists(String nickname, String providerId) {
        if (!userRepository.existsByNickname(nickname)) {
            userRepository.save(
                User.builder()
                    .nickname(nickname)
                    .provider("kakao")
                    .providerId(providerId)
                    .build()
            );
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

    private void createMyLongTextScore(MyLongText myLongText, User user, int score) { // int vs Integer
        myLongTextScoreRepository.save(
            MyLongTextScore.builder()
                .score(score)
                .myLongText(myLongText)
                .user(user)
                .build()
        );
    }

    private void createFriendRequestIfNotExists(User user1, User user2, boolean isAccepted) {

        if (!friendRequestRepository.existsByRequesterAndReceiver(user1, user2)&&
            !friendRequestRepository.existsByRequesterAndReceiver(user2, user1)){

            if(isAccepted) {
                friendRequestRepository.save(
                    FriendRequest.builder()
                        .requester(user1)
                        .receiver(user2)
                        .status(FriendRequestStatus.ACCEPTED)
                        .build());
            }else {
                friendRequestRepository.save(
                    FriendRequest.builder()
                        .requester(user1)
                        .receiver(user2)
                        .status(FriendRequestStatus.PENDING)
                        .build());
            }

        }

    }


}