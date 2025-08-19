package hello.typing_game_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableJpaAuditing
public class TypingGameBeApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("AWS_USERNAME", dotenv.get("AWS_USERNAME"));
        System.setProperty("AWS_PASSWORD", dotenv.get("AWS_PASSWORD"));

        SpringApplication.run(TypingGameBeApplication.class, args);
    }

}
