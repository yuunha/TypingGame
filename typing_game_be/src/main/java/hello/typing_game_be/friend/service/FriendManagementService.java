package hello.typing_game_be.friend.service;

import org.springframework.stereotype.Service;

import hello.typing_game_be.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendManagementService {

    private final FriendRequestService friendRequestService;
    private final FriendService friendService;


    @Transactional
    public void acceptFriendRequest(User requester, User receiver) {
        // 1. 친구 요청 삭제
        friendRequestService.deleteRequest(requester, receiver);

        // 2. 친구 관계 추가
        friendService.addFriend(requester, receiver);
    }
}
