package hello.typing_game_be.constitution.entity;

import hello.typing_game_be.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
@Table(name = "constitution_progress")
public class ConstitutionProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long constitutionProgressId;

    @ManyToOne
    private User user;

    private Integer articleIndex; // 조

    private Integer lastPosition;    // 조 내에서 멈춘 글자 위치
}
