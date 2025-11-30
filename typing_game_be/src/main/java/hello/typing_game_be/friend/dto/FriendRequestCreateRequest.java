package hello.typing_game_be.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestCreateRequest {
    private Long receiverId;  // 친구 요청 받을 회원 ID
}