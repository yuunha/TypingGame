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
        System.out.println(request.getLoginId());

        User user = User.builder()
            .username(request.getUsername())
            .loginId(request.getLoginId())
            .password(passwordEncoder.encode(request.getPassword())) // 인코딩
            .build();
        userRepository.save(user);

    }

    public void login(UserRequest request) {
        // System.out.println("loginId: " + request.getLoginId()+"password"+request.getPassword());
        // System.out.println("존재여부"+userRepository.existsByLoginId(request.getLoginId()));
        // userRepository.findByLoginId(request.getLoginId()).ifPresent(u -> {
        //     System.out.println("DB 저장된 password = " + u.getPassword());
        //     System.out.println("matches 결과 = " + passwordEncoder.matches(request.getPassword(), u.getPassword()));
        // });

        // 1. 아이디 존재 여부 확인
        User user = userRepository.findByLoginId(request.getLoginId())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 2. 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }
    }

    public boolean existsByLoginId(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }
}
