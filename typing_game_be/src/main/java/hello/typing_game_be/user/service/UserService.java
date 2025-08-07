package hello.typing_game_be.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import hello.typing_game_be.common.exception.BusinessException;
import hello.typing_game_be.common.exception.ErrorCode;
import hello.typing_game_be.user.dto.UserResponse;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import hello.typing_game_be.user.dto.UserCreateRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    @Transactional
    public Long register(UserCreateRequest request) {

        if (userRepository.existsByLoginId(request.getLoginId())) { //중복 유저 검증
            throw new BusinessException(ErrorCode.DUPLICATE_LOGINID);
        }
        User user = User.builder()
            .username(request.getUsername())
            .loginId(request.getLoginId())
            .password(passwordEncoder.encode(request.getPassword())) // 인코딩
            .build();
        userRepository.save(user);

        return user.getUserId();
    }

    // public void login(UserRequest request) {
    //     // System.out.println("loginId: " + request.getLoginId()+"password"+request.getPassword());
    //     // System.out.println("존재여부"+userRepository.existsByLoginId(request.getLoginId()));
    //     // userRepository.findByLoginId(request.getLoginId()).ifPresent(u -> {
    //     //     System.out.println("DB 저장된 password = " + u.getPassword());
    //     //     System.out.println("matches 결과 = " + passwordEncoder.matches(request.getPassword(), u.getPassword()));
    //     // });
    //
    //     // 1. 아이디 존재 여부 확인
    //     User user = userRepository.findByLoginId(request.getLoginId())
    //         .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    //
    //     // 2. 비밀번호 일치 여부 확인
    //     if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
    //         throw new BusinessException(ErrorCode.INVALID_PASSWORD);
    //     }
    // }
    @Transactional
    public void update(Long userId, String name) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        user.setUsername(name);
    }

    public UserResponse getUserByLoginId(String loginId) {
        User user = userRepository.findByLoginId(loginId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return new UserResponse(user.getUserId(),user.getLoginId(), user.getUsername());
    }

    @Transactional
    public void deleteUserByLoginId(String loginId) {
        if(!userRepository.existsByLoginId(loginId)){
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }else{
            userRepository.deleteByLoginId(loginId);
        }
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            System.out.println("해당 userId " + userId+ "가 존재하지 않습니다.");
            return new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        });
    }
}
