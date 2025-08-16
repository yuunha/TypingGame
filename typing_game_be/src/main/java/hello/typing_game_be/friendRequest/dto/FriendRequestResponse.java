package hello.typing_game_be.friendRequest.dto;

import hello.typing_game_be.friendRequest.entity.FriendRequest;
import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
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