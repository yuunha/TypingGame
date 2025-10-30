package hello.typing_game_be.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PublicUserResponse {
    private Long userId;
    private String nickname;
    private String profileImageUrl; // S3 URL 등 가공된 데이터
}
