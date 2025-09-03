package hello.typing_game_be.constitution.dto;

import hello.typing_game_be.constitution.entity.Constitution;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConstitutionResponse {
    private String chapter;   // 제1장 총강
    private String section;   // 제2절 행정부
    private String subsection; // 제1관 국무총리와 국무위원
    private String articleNumber; // 제1조

    private Integer articleIndex; //숫자 형태

    private String content;   // 조문 내용

    public static ConstitutionResponse fromEntity(Constitution constitution) {
        return new ConstitutionResponse(
            constitution.getChapter(), constitution.getSection(), constitution.getSubsection(),
            constitution.getArticleNumber(), constitution.getArticleIndex(), constitution.getContent()
        );
    }
}
