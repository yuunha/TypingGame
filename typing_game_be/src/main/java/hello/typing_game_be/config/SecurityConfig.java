package hello.typing_game_be.config;

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
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll() // 회원가입/로그인 허용
                .anyRequest().authenticated()            // 나머지는 인증 필요
            )
            .formLogin(form -> form.disable())  // 폼 로그인 비활성화
            .httpBasic(httpBasic -> httpBasic.disable()); // HTTP Basic 비활성화

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}