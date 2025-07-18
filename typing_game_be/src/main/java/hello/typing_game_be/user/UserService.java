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
    }

    public boolean login(String loginId, String password) {
        return userRepository.findByLoginId(loginId)
                 .map(user -> user.getPassword().equals(password))
                 .orElse(false);
    }
}
