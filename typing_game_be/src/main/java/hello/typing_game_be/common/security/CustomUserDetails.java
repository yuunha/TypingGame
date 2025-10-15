package hello.typing_game_be.common.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import hello.typing_game_be.user.entity.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

// CustomUserDetails - OAuth2User 구현
public class CustomUserDetails implements OAuth2User {

    private final User user;
    private final Map<String, Object> attributes;

    public CustomUserDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    // User 엔티티 접근용
    public User getUser() {
        return user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 없음 - 빈 리스트 반환
        return Collections.emptyList();
    }

    @Override
    public String getName() {
        return String.valueOf(user.getUserId());
    }

    public Long getUserId() {
        return user.getUserId();
    }

//    public Long getProviderId() {
//        // 카카오 고유 ID 반환
//        return user.getProviderId();
//    }

    public String getUsername() {
        // 카카오 고유 ID 반환
        return user.getNickname();
    }

    public String getProfileImageKey() {
        // 카카오 고유 ID 반환
        return user.getProfileImageKey();
    }

}