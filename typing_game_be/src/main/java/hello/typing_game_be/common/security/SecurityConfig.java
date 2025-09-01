package hello.typing_game_be.common.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (REST API)
            .headers(headers -> headers.frameOptions(frame -> frame.disable())) // H2 콘솔 허용
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll() // H2 콘솔과 인증 관련 API는 인증 없이 허용
                .requestMatchers(HttpMethod.POST, "/user").permitAll()   // 회원가입 등 POST만 허용
                .requestMatchers("/user/**").authenticated()            // 그 외 /user 하위 경로는 인증 필요
                .requestMatchers(HttpMethod.GET, "/long-text/**").permitAll()  // GET 요청만 허용
                .anyRequest().authenticated()
            )
            .formLogin(form -> form.disable())  // 폼 로그인 비활성화
            .httpBasic(Customizer.withDefaults()) //HTTP Basic 인증 활성화
            .cors(cors -> cors.configurationSource(corsConfigurationSource())); // 적용
        return http.build();
    }
    @Bean //인증관리자(AuthenticationManager)를 Bean으로 등록
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);

        //UserDetailsService와 PasswordEncoder 등록
        //인증을 시도하면 → UserDetailsService에서 유저를 찾고 → PasswordEncoder로 비밀번호 검증
        builder.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());

        return builder.build();
    }
    //사용자 정보를 로드하는 서비스 지정, 비밀번호 비교 시 사용할 인코더 지정

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
            "https://typinggame-gold.vercel.app",
            "http://localhost:3000",
            "http://typinggame-gold.vercel.app"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE","PATCH" ,"OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}