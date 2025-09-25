package hello.typing_game_be.longText.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) //외부에서 기본 생성자 막기
@AllArgsConstructor
public class LongText {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long longTextId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private  String content;
}
