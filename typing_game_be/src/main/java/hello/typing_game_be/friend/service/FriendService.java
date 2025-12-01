package hello.typing_game_be.friend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.typing_game_be.friend.entity.Friend;
import hello.typing_game_be.friend.repository.FriendRepository;
import hello.typing_game_be.user.dto.UserListResponse;
import hello.typing_game_be.user.entity.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;

    public void addFriend(User requester, User receiver){
        User userA;
        User userB;
        //id 작은 user를 userA필드에 저장
        if(requester.getUserId() <= receiver.getUserId()){
            userA=requester;
            userB=receiver;
        }else{
            userA=receiver;
            userB=requester;
        }
        friendRepository.save(Friend.builder()
            .userA(userA)
            .userB(userB)
            .build()
        );
    }

    //@Transactional(readOnly = true)
    //public List<UserListResponse> getFriends(Long userId) {
        // // userId 기준으로 친구 관계 조회 (ACCEPTED 상태)
        // List<FriendRequest> friendRequests = friendRequestRepository.findAcceptedFriends(userId);
        //
        // // 친구 목록으로 변환 (본인을 제외한 상대방)
        // return friendRequests.stream()
        //     .map(fr -> {
        //         if (fr.getRequester().getUserId().equals(userId)) {
        //             return UserListResponse.fromEntity(fr.getReceiver());
        //         } else {
        //             return UserListResponse.fromEntity(fr.getRequester());
        //         }
        //     })
        //     .toList();
    //}
    @Transactional
    public void deleteFriend(Long friendRequestId, Long userId) {
        // FriendRequest fr = friendRequestRepository.findById(friendRequestId)
        //     .orElseThrow(() -> new BusinessException(ErrorCode.FRIEND_REQEUST_NOT_FOUND));
        //
        // // 요청 수신자가 맞는지 또는 요청자가 맞는지 확인 (삭제 권한)
        // if (!fr.getReceiver().getUserId().equals(userId) && !fr.getRequester().getUserId().equals(userId)) {
        //     throw new BusinessException(ErrorCode.FORBIDDEN_REQUEST); // 403 Forbidden
        // }
        //
        // // // 상태가 ACCEPTED인지 확인
        // // if (!FriendRequestStatus.ACCEPTED.equals(fr.getStatus())) {
        // //     throw new BusinessException(ErrorCode.INVALID_ACTION,"삭제가 불가능합니다."); // PENDING이면 삭제 불가
        // // }
        //
        // // 삭제
        // friendRequestRepository.delete(fr);
    }
}
