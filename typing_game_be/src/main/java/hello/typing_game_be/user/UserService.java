package hello.typing_game_be.user;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public void register(String loginId, String username ,String password) {
        userRepository.save(User.builder()
            .loginId(loginId)
            .username(username)
            .password(password)
            .build()
        );
        //중복 체크를 existsByLoginId()로 처리.
    }

    public boolean login(String loginId, String password) {
        System.out.println("loginId: " + loginId+"password"+password);
        System.out.println("존재여부"+userRepository.existsByLoginId(loginId));
        return userRepository.findByLoginId(loginId)
                 .map(user -> user.getPassword().equals(password))
                 .orElse(false);
    }

    public boolean existsByLoginId(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }
}
