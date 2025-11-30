package hello.typing_game_be.friend.service;

import org.springframework.stereotype.Service;

import hello.typing_game_be.common.exception.BusinessException;
import hello.typing_game_be.common.exception.ErrorCode;
import hello.typing_game_be.friend.entity.FriendRequest;
import hello.typing_game_be.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendManagementService {

    private final FriendRequestService friendRequestService;
    private final FriendService friendService;

    public void respondFriendRequest(Long friendRequestId, Long userId, String action) {

        FriendRequest friendRequest = friendRequestService.findById(friendRequestId);

        // 요청 받은 사용자가 맞는지 검증
        if (!friendRequest.getReceiver().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_FRIEND_REQUEST);
        }

        if (action.equals("ACCEPT")) {
            // 친구 관계 생성
            friendService.addFriend(request.getRequester(), request.getReceiver());
        }

        // 요청 삭제 (거절도 포함)
        friendRequestService.deleteRequest(friendRequest);
    }


}
