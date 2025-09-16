package hello.typing_game_be.myLongText.entity;

import java.util.ArrayList;
import java.util.List;

import hello.typing_game_be.myLongTextScore.entity.MyLongTextScore;
import hello.typing_game_be.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "my_long_text")
public class MyLongText {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long myLongTextId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "myLongText", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MyLongTextScore> scores = new ArrayList<>();
    //cascade = CascadeType.ALL → MyLongText 삭제 시 관련된 MyLongTextScore도 삭제됨
}
