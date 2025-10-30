package hello.typing_game_be.common.security;

import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, //클라이언트로부터 받은 요청 정보
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // 1️. 로그인된 사용자 정보 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        // 2. providerId 가져오기
        String providerId;
        if (user != null) {
            providerId = user.getProviderId();
        } else {
            // 신규 사용자일 경우 OAuth2User 속성에서 providerId 가져오기
            Map<String, Object> attributes = userDetails.getAttributes();
            providerId = attributes.get("id").toString();
        }

        // 3. 기존 사용자 확인
        if (user != null) {
            // 기존 사용자 → 홈으로 이동
            getRedirectStrategy().sendRedirect(request, response, "/home");
        } else {
            // 신규 사용자 → DB 저장 후 닉네임 입력 페이지로 이동
            User newUser = new User(providerId);
            newUser.setProvider("KAKAO"); // provider 고정
            userRepository.save(newUser);

            // 새로 저장한 User를 CustomUserDetails에 반영 (필요시)
            // userDetails.setUser(newUser); // 선택사항

            getRedirectStrategy().sendRedirect(request, response, "/signup/nickname");
        }
    }
}
