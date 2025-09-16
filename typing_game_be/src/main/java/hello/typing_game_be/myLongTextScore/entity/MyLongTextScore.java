package hello.typing_game_be.myLongTextScore.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import hello.typing_game_be.myLongText.entity.MyLongText;
import hello.typing_game_be.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "my_long_text_score")
public class MyLongTextScore {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long myLongTextScoreId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "my_long_text_id")
    private MyLongText myLongText;

    @Column(nullable = false)
    private Integer score;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
