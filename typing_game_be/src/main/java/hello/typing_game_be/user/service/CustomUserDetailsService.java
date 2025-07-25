package hello.typing_game_be.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hello.typing_game_be.common.exception.BusinessException;
import hello.typing_game_be.common.exception.ErrorCode;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        //loadUserByUsername메서드는 강제 구현해야함
        //username은 단순히 식별자를 의미함. -> loginId를 식별자로 지정
        User user = userRepository.findByLoginId(loginId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getLoginId())
            .password(user.getPassword())  // 암호화된 비밀번호
                                       // 필요한 경우 역할 추가
            .build();
    }
}