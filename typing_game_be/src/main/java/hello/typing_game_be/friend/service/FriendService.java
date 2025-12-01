package hello.typing_game_be.friend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.typing_game_be.common.exception.BusinessException;
import hello.typing_game_be.common.exception.ErrorCode;
import hello.typing_game_be.friend.entity.Friend;
import hello.typing_game_be.friend.repository.FriendRepository;
import hello.typing_game_be.user.dto.UserListResponse;
import hello.typing_game_be.user.entity.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;

    public void addFriend(User requester, User receiver) {
        User userA;
        User userB;
        //id가 작은 user를 userA필드에 저장
        if (requester.getUserId() <= receiver.getUserId()) {
            userA = requester;
            userB = receiver;
        } else {
            userA = receiver;
            userB = requester;
        }
        friendRepository.save(Friend.builder()
            .userA(userA)
            .userB(userB)
            .build()
        );
    }

    @Transactional(readOnly = true)
    public List<UserListResponse> getFriends(Long userId) {
        // userId가 포함된 친구 관계 모두 가져오기
        List<Friend> friends = friendRepository.findAllByUserId(userId);

        return friends.stream()
            .map(friend -> extractFriend(friend, userId))  // 상대방 추출
            .map(UserListResponse::fromEntity)             // DTO 변환
            .toList();
    }

    /**
     * Friend 엔티티에서 요청된 userId가 아닌 "상대방 User" 추출
     */
    private User extractFriend(Friend friend, Long userId) {
        return friend.getUserA().getUserId().equals(userId)
            ? friend.getUserB()
            : friend.getUserA();
    }

    @Transactional
    public void deleteFriend(Long friendId, Long userId) {
        Friend friend = friendRepository.findById(friendId)
            .orElseThrow(() -> new BusinessException(ErrorCode.FRIEND_NOT_FOUND));

        // userA 또는 userB가 아니라면 삭제 권한 없음
        boolean isParticipant =
            friend.getUserA().getUserId().equals(userId) ||
                friend.getUserB().getUserId().equals(userId);

        if (!isParticipant) {
            throw new BusinessException(ErrorCode.FORBIDDEN_REQUEST);
        }

        friendRepository.delete(friend);
    }
}
