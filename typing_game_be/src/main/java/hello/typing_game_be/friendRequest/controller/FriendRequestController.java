package hello.typing_game_be.friendRequest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.typing_game_be.common.security.CustomUserDetails;
import hello.typing_game_be.friendRequest.dto.FriendRequestCreateRequest;
import hello.typing_game_be.friendRequest.dto.FriendRequestResponse;
import hello.typing_game_be.friendRequest.dto.FriendRequestUpdateRequest;
import hello.typing_game_be.friendRequest.service.FriendRequestService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friend-requests")
public class FriendRequestController {
    private final FriendRequestService friendRequestService;

    @PostMapping
    public ResponseEntity<FriendRequestResponse> createFriendRequest(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody FriendRequestCreateRequest request
    ){
        Long requesterId = userDetails.getUserId();
        FriendRequestResponse response = friendRequestService.sendFriendRequest(requesterId, request.getReceiverId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{friendRequestId}")
    public ResponseEntity<Void> respondFriendRequest(
        @PathVariable Long friendRequestId,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody FriendRequestUpdateRequest request
    ) {
        friendRequestService.respondToFriendRequest(
            friendRequestId,
            userDetails.getUserId(),
            request.getAction()
        );
        return ResponseEntity.ok().build();
    }

}
