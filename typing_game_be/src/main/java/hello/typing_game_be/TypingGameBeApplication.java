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
        System.setProperty("AWS_REGION", dotenv.get("AWS_REGION"));
        System.setProperty("AWS_ACCESS_KEY", dotenv.get("AWS_ACCESS_KEY"));
        System.setProperty("AWS_SECRET_KEY", dotenv.get("AWS_SECRET_KEY"));

        SpringApplication.run(TypingGameBeApplication.class, args);
    }

}
