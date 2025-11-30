package hello.typing_game_be.friend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.typing_game_be.common.security.CustomUserDetails;
import hello.typing_game_be.friend.dto.FriendRequestCreateRequest;
import hello.typing_game_be.friend.dto.FriendRequestListResponse;
import hello.typing_game_be.friend.dto.FriendRequestUpdateRequest;
import hello.typing_game_be.friend.service.FriendRequestService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friend-requests")
public class FriendRequestController {
    private final FriendRequestService friendRequestService;

    // 친구 요청 생성
    @PostMapping
    public ResponseEntity<FriendRequestIdResponse> createFriendRequest(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody FriendRequestCreateRequest request
    ){
        Long requesterId = userDetails.getUserId();
        Long friendRequestId = friendRequestService.sendFriendRequest(requesterId, request.getReceiverId());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new FriendRequestIdResponse(friendRequestId));
    }

    // 친구 요청 응답 (수락/거부)
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

    // 보낸 친구 목록 요청 조회
    @GetMapping("/sent")
    public ResponseEntity<List<FriendRequestListResponse>> getSentFriendRequest(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body( friendRequestService.getSentFriendRequests(userDetails.getUserId()) );
    }

    // 받은 친구 요청 목록 조회
    @GetMapping("/received")
    public ResponseEntity<List<FriendRequestListResponse>> getReceivedFriendRequests(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<FriendRequestListResponse> receivedRequests =
            friendRequestService.getReceivedFriendRequests(userDetails.getUserId());

        return ResponseEntity.status(HttpStatus.OK)
            .body(receivedRequests);
    }

    @Data
    @AllArgsConstructor
    public class FriendRequestIdResponse {
        private Long friendRequestId;
    }
}
