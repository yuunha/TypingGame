package hello.typing_game_be.friend.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import hello.typing_game_be.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long friendId;

    @ManyToOne(fetch = FetchType.LAZY)   // 요청 보낸 사람
    @JoinColumn(nullable = false)
    private User userA;

    @ManyToOne(fetch = FetchType.LAZY)   // 요청 보낸 사람
    @JoinColumn(nullable = false)
    private User userB;

    @CreatedDate
    private LocalDateTime createdAt;
}
