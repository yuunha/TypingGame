package hello.typing_game_be.common.security;

import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 카카오에서 내려주는 고유 ID
        String providerId = oAuth2User.getAttribute("id").toString();
        // OAuth2 클라이언트 이름 ("kakao")를 대문자로 변환
        String provider = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        // 카카오에서 받은 사용자 정보 추출
        Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
        Map<String, Object> properties = oAuth2User.getAttribute("properties");

        //String email = kakaoAccount != null ? (String) kakaoAccount.get("email") : null;
        String nickname = properties != null ? (String) properties.get("nickname") : null;

        // DB에 사용자 조회 -> 있으면 기존 사용자 사용, 없으면 새 사용자 등록
        User user = userRepository.findByProviderId(providerId)
                .orElseGet(() -> {
                    User newUser = new User(providerId);
                    newUser.setProvider(provider);
                    newUser.setNickname(nickname);
                    return userRepository.save(newUser);
                });

        // CustomUserDetails 반환
        return new CustomUserDetails(user, oAuth2User.getAttributes());
    }
}
