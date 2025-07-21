package hello.typing_game_be.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(String loginId, String username ,String password) {
        userRepository.save(User.builder()
            .loginId(loginId)
            .username(username)
            .password(passwordEncoder.encode(password)) // 암호화 저장
            .build()
        );
        //중복 체크를 existsByLoginId()로 처리.
    }

    public boolean login(String loginId, String password) {
        // System.out.println("loginId: " + loginId+"password"+password);
        // System.out.println("존재여부"+userRepository.existsByLoginId(loginId));
        // userRepository.findByLoginId(loginId).ifPresent(u -> {
        //     System.out.println("DB 저장된 password = " + u.getPassword());
        //     System.out.println("matches 결과 = " + passwordEncoder.matches(password, u.getPassword()));
        // });
        return userRepository.findByLoginId(loginId)
            .map(user -> passwordEncoder.matches(password, user.getPassword()))
                 .orElse(false);
    }

    public boolean existsByLoginId(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }
}
