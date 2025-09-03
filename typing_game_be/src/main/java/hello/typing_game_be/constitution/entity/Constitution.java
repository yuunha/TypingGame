package hello.typing_game_be.constitution.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //외부에서 기본 생성자 막기
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "constitution")
public class Constitution {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long constitutionId;

    //장/절/관을 따로 필드에 저장 → 프론트에서 필요하면 보여주고, null이면 생략
    private String chapter;   // 제1장 총강
    private String section;   // 제2절 행정부
    private String subsection; // 제1관 국무총리와 국무위원
    private String articleNumber; // 제1조

    private Integer articleIndex; // 10처럼 숫자 형태, 조 조회용


    @Lob
    private String content;   // 조문 내용

    public Constitution(String chapter, String section, String subsection,
        String articleNumber, Integer articleIndex, String content) {
        this.chapter = chapter;
        this.section = section;
        this.subsection = subsection;
        this.articleNumber = articleNumber;
        this.articleIndex = articleIndex;
        this.content = content;
    }

}
