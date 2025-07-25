package hello.typing_game_be.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import hello.typing_game_be.user.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;


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
            .httpBasic(Customizer.withDefaults()) //HTTP Basic 인증 활성화
            .cors(cors -> {});
        return http.build();
    }
    @Bean //인증관리자를 Bean으로 등록
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
        return builder.build();
    }
    //사용자 정보를 로드하는 서비스 지정, 비밀번호 비교 시 사용할 인코더 지정

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}