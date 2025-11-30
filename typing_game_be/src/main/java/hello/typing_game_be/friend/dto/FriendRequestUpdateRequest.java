package hello.typing_game_be.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestUpdateRequest {
    private String action; // "ACCEPT" or "DECLINE"
}