package hello.typing_game_be.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final ProfileCompletionInterceptor profileCompletionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(profileCompletionInterceptor)
                .addPathPatterns("/**")             // 모든 경로 적용
                .excludePathPatterns(               // 예외 경로
                        "/signup/**",
                        "/oauth2/**",
                        "/css/**",
                        "/js/**",
                        "/images/**"
                );
    }
}
