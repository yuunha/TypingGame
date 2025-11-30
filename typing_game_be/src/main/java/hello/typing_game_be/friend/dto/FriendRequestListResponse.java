package hello.typing_game_be.friend.dto;

import java.time.LocalDateTime;

import hello.typing_game_be.friend.entity.FriendRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestListResponse {
    private Long friendRequestId;
    private String requesterName;
    private String receiverName;
    private LocalDateTime createdAt;

    public static FriendRequestListResponse fromEntity(FriendRequest fr) {
        return new FriendRequestListResponse(
            fr.getFriendRequestId(),
            fr.getRequester().getNickname(),
            fr.getReceiver().getNickname(),
            fr.getCreatedAt()
        );
    }
}
