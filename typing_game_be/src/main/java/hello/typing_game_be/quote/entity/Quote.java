package hello.typing_game_be.quote.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder //allArgsConstructor 필요
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //외부에서 기본 생성자 막기
@AllArgsConstructor
@Table(name = "quote")
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long quoteId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String author;
}
