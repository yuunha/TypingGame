package hello.typing_game_be.user.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import hello.typing_game_be.longTextScore.entity.LongTextScore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder //allArgsConstructor 필요
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //외부에서 기본 생성자 막기
@AllArgsConstructor
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(unique = true, length = 20)
    private String nickname;

    @Column(name = "profile_image_key")
    private String profileImageKey; // S3 key만 저장

    @Column(name = "provider_id",unique = true, nullable = false)
    private String providerId; //카카오에서 발급하는 고유 사용자 id

    private String provider; // kakao, google 등 소셜 로그인 제공자

    // --- 생성/수정 시간 ---
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // 유저가 작성한 긴 글 점수들
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LongTextScore> longTextScores = new ArrayList<>();


    // 커스텀 생성자
    public User(String providerId) {
        this.providerId = providerId;
    }
}
