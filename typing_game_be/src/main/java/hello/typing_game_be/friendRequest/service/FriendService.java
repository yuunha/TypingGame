package hello.typing_game_be.friendRequest.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.typing_game_be.friendRequest.entity.FriendRequest;
import hello.typing_game_be.friendRequest.repository.FriendRequestRepository;
import hello.typing_game_be.user.dto.UserListResponse;
import hello.typing_game_be.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRequestRepository friendRequestRepository;

    @Transactional(readOnly = true)
    public List<UserListResponse> getFriends(Long userId) {
        // userId 기준으로 친구 관계 조회 (ACCEPTED 상태)
        List<FriendRequest> friendRequests = friendRequestRepository.findAcceptedFriends(userId);

        // 친구 목록으로 변환 (본인을 제외한 상대방)
        return friendRequests.stream()
            .map(fr -> {
                if (fr.getRequester().getUserId().equals(userId)) {
                    return UserListResponse.fromEntity(fr.getReceiver());
                } else {
                    return UserListResponse.fromEntity(fr.getRequester());
                }
            })
            .toList();
    }
}
