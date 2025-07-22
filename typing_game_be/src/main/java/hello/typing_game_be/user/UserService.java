package hello.typing_game_be.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import hello.typing_game_be.common.exception.BusinessException;
import hello.typing_game_be.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(UserRequest request) {
        if (userRepository.existsByLoginId(request.getLoginId())) {
            throw new BusinessException(ErrorCode.DUPLICATE_LOGINID);
        }
        User user = UserMapper.toEntity(request);
        userRepository.save(user);
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
