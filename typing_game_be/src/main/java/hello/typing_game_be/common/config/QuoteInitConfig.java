package hello.typing_game_be.common.config;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import hello.typing_game_be.quote.entity.Quote;
import hello.typing_game_be.quote.repository.QuoteRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class QuoteInitConfig {
    private final QuoteRepository quoteRepository;


    @Bean
    CommandLineRunner initQuotes() {
        return args -> {
            try {
                saveQuote();
            } catch (IOException | CsvValidationException e) {
                e.printStackTrace();
            }
        };
    }

    public void saveQuote() throws IOException, CsvValidationException {
        if (quoteRepository.count() > 0) return; // 이미 저장되어 있으면 종료

        try (CSVReader reader = new CSVReader(new InputStreamReader(
            getClass().getResourceAsStream("/quotes.csv"), StandardCharsets.UTF_8))) {

            String[] line;
            List<Quote> quotes = new ArrayList<>();

            reader.readNext(); // 헤더 건너뛰기

            while ((line = reader.readNext()) != null) {
                // line[0] = id, line[1] = 명언, line[2] = 저자
                String content = line[1].trim();
                String author = line[2].trim();
                quotes.add(Quote.builder()
                    .content(content)
                    .author(author)
                    .build()
                );
            }
            quoteRepository.saveAll(quotes);
        }
    }
}
