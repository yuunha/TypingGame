package hello.typing_game_be.user.entity;

import java.util.ArrayList;
import java.util.List;

import hello.typing_game_be.longTextScore.entity.LongTextScore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder //allArgsConstructor 필요
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //외부에서 기본 생성자 막기
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(unique = true, nullable = false, length = 20)
    private String nickname;

    @Column(name = "profile_image_key")
    private String profileImageKey; // S3 key만 저장

    @Column(name = "provider_id",unique = true, nullable = false)
    private String providerId; //카카오에서 발급하는 고유 사용자 id

    private String provider; // kakao, google 등 소셜 로그인 제공자

    // 유저가 작성한 긴 글 점수들
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LongTextScore> longTextScores = new ArrayList<>();

    // 커스텀 생성자
    public User(String providerId) {
        this.providerId = providerId;
    }
}
