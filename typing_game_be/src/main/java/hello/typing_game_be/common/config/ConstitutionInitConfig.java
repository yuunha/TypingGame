package hello.typing_game_be.common.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.typing_game_be.constitution.entity.Constitution;
import hello.typing_game_be.constitution.repository.ConstitutionRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ConstitutionInitConfig {

    private final ConstitutionRepository constitutionRepository;

    @Bean
    CommandLineRunner initConstitution() {
        return args -> {
            if (constitutionRepository.count() == 0) {
                List<Constitution> articles = parseTxtToArticles("/constitution.txt");
                constitutionRepository.saveAll(articles);
            }
        };
    }

    private List<Constitution> parseTxtToArticles(String path) throws IOException {
        List<Constitution> articles = new ArrayList<>();

        String chapter = null;
        String section = null;
        String subsection = null;
        String articleNumber = null;
        Integer articleIndex = 0; //0=선언문
        StringBuilder content = new StringBuilder();

        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(getClass().getResourceAsStream(path), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                Pattern chapterPattern = Pattern.compile("^제([1-9]|[1-9][0-9]|100)장");
                Pattern sectionPattern = Pattern.compile("^제([1-9]|[1-9][0-9]|100)절");
                Pattern subsectionPattern = Pattern.compile("^제([1-9]|[1-9][0-9]|100)관");
                Pattern articlePattern = Pattern.compile("^제(0|[1-9][0-9]?|1[0-4][0-9]|150)조");
                //0 → 0조, [1-9][0-9]? → 1~99조 ,  1[0-4][0-9] → 100~149조 , 150 → 150조

                if (chapterPattern.matcher(line).find()) { //“제1장”부터 “제100장”까지
                    chapter = line;
                } else if (sectionPattern.matcher(line).find()) { //“제1절”부터 “제100절”
                    section = line;
                } else if (subsectionPattern.matcher(line).find()) { //“제1관”부터 “제100관”
                    subsection = line;
                } else if (articlePattern.matcher(line).find()) { //“제1조”부터 “제150조”
                    // 이전 조가 있으면 저장 후 새로운 조 시작
                    if (articleIndex != null) {
                        articles.add(new Constitution(chapter, section, subsection,
                            articleNumber, articleIndex, content.toString().trim()));
                        content.setLength(0);
                    }

                    articleNumber = line;
                    articleIndex = extractNumber(line); // "제10조" -> 10
                } else {
                    // 조문 내용
                    content.append(line).append("\n");
                }
            }

            // 마지막 조 저장
            if (articleNumber != null) {
                articles.add(new Constitution(chapter, section, subsection,
                    articleNumber, articleIndex, content.toString().trim()));
            }
        }

        return articles;
    }

    //조 번호 추출 : "제10조"->10
    private int extractNumber(String articleLine) {
        String num = articleLine.replaceAll("[^0-9]", "");
        return Integer.parseInt(num);
    }
}
