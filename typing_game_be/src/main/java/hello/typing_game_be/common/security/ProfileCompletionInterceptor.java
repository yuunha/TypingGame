package hello.typing_game_be.common.security;

import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ProfileCompletionInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 인증 정보 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return true; // 비로그인 사용자는 통과 (필요시 로그인페이지로 리다이렉트 가능)
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        // 프로필 미완성 상태면 특정 경로로 리다이렉트
        if (!user.isProfileComplete()) {
            String path = request.getRequestURI();
            // 닉네임 등록 관련 페이지는 통과
            if (!path.startsWith("/signup")) {
                response.sendRedirect("/signup/nickname");
                return false; // 요청 차단
            }
        }

        return true; // 통과
    }
};