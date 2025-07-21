package hello.typing_game_be.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (REST API)
            .headers(headers -> headers.frameOptions(frame -> frame.disable())) // H2 콘솔 허용
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**", "/auth/**").permitAll() // H2 콘솔과 인증 관련 API 허용
                .anyRequest().authenticated()
            )
            .formLogin(form -> form.disable())  // 폼 로그인 비활성화
            .httpBasic(httpBasic -> httpBasic.disable()) // HTTP Basic 비활성화
            .cors(cors -> {});
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}