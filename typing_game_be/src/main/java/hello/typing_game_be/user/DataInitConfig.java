package hello.typing_game_be.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
//@Profile({"local", "dev"})
@RequiredArgsConstructor
public class DataInitConfig {

    private final UserService userService;

    @Bean
    CommandLineRunner initUsers() {
        return args -> {
            createUserIfNotExists("admin", "admin", "12345");
            createUserIfNotExists("홍길동", "dong", "1111");
            createUserIfNotExists("홍길순", "soon", "2222");
        };
    }

    private void createUserIfNotExists(String username, String loginId, String password) {
        if (!userService.existsByLoginId(loginId)) {
            userService.register(username, loginId, password);
        }
    }
}