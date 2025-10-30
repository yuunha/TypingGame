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
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService
    ,CustomOAuth2SuccessHandler customOAuth2SuccessHandler) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customOAuth2SuccessHandler = customOAuth2SuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            //.csrf(csrf -> csrf.disable()) // CSRF 비활성화 (REST API)
            .headers(headers -> headers.frameOptions(frame -> frame.disable())) // H2 콘솔 허용
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll() // H2 콘솔과 인증 관련 API는 인증 없이 허용
                .requestMatchers(HttpMethod.POST, "/user").permitAll()   // 회원가입 등 POST만 허용
                .requestMatchers("/user/**").authenticated()            // 그 외 /user 하위 경로는 인증 필요
                .requestMatchers(HttpMethod.GET, "/long-text/**").permitAll()  // GET 요청만 허용
                .requestMatchers(HttpMethod.GET, "/quote/today").permitAll() //오늘의 명언
                 // 홈페이지와 회원가입은 모든 사용자 접근 허용
                //.requestMatchers("/", "/signup", "/login").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form.disable())  // 폼 로그인 비활성화
            //.httpBasic(Customizer.withDefaults()) //HTTP Basic 인증 활성화
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 적용
            //oauth2 로그인 설정
            .oauth2Login(oauth2 -> oauth2
                    .userInfoEndpoint(userInfo -> userInfo
                            .userService(customOAuth2UserService) // 구현한 CustomOAuth2UserService 연결
                    )
                    .successHandler(customOAuth2SuccessHandler) // 로그인 성공 시 직접 제어
            );
        return http.build();
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