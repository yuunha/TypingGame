package hello.typing_game_be.friendRequest.dto;

import hello.typing_game_be.friendRequest.entity.FriendRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendRequestResponse {
    private Long friendRequestId;
    private Long requesterId;
    private Long receiverId;
    private String status;

    public static FriendRequestResponse fromEntity(FriendRequest fr) {
        return new FriendRequestResponse(
            fr.getFriendRequestId(),
            fr.getRequester().getUserId(),
            fr.getReceiver().getUserId(),
            fr.getStatus().name()
        );
    }
}