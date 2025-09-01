package hello.typing_game_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableJpaAuditing
public class TypingGameBeApplication {

    public static void main(String[] args) {


        SpringApplication.run(TypingGameBeApplication.class, args);
    }

}
