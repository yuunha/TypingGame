package hello.typing_game_be.friendRequest.dto;

import java.time.LocalDateTime;

import hello.typing_game_be.friendRequest.entity.FriendRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestListResponse {
    private String receiverName;
    private LocalDateTime createdAt;

    public static FriendRequestListResponse fromEntity(FriendRequest fr) {
        return new FriendRequestListResponse(
            fr.getReceiver().getUsername(),
            fr.getCreatedAt()
        );
    }
}
