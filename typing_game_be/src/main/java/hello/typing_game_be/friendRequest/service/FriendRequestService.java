package hello.typing_game_be.friendRequest.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.typing_game_be.common.exception.BusinessException;
import hello.typing_game_be.common.exception.ErrorCode;
import hello.typing_game_be.friendRequest.dto.FriendRequestResponse;
import hello.typing_game_be.friendRequest.entity.FriendRequest;
import hello.typing_game_be.friendRequest.entity.FriendRequestStatus;
import hello.typing_game_be.friendRequest.repository.FriendRequestRepository;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendRequestService {
    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;

    public FriendRequestResponse sendFriendRequest(Long requesterId, Long receiverId) {
        User requester = userRepository.findById(requesterId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND,"요청자 없음"));
        User receiver = userRepository.findById(receiverId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND,"수신자 없음"));

        // 중복 검증 (양방향)
        boolean exists = friendRequestRepository.existsByRequesterAndReceiver(requester, receiver)
            || friendRequestRepository.existsByRequesterAndReceiver(receiver, requester);


        if (exists) {
            throw new IllegalStateException("이미 친구 요청이 존재합니다.");
        }

        FriendRequest fr = FriendRequest.builder()
            .requester(requester)
            .receiver(receiver)
            .status(FriendRequestStatus.PENDING)
            .build();
        friendRequestRepository.save(fr);
        return FriendRequestResponse.fromEntity(fr);
    }
    @Transactional
    public void respondToFriendRequest(Long friendRequestId, Long userId, String action) {
        FriendRequest fr = friendRequestRepository.findById(friendRequestId)
            .orElseThrow(() -> new BusinessException(ErrorCode.FRIEND_REQEUST_NOT_FOUND));

        // 요청 수신자가 맞는지 확인
        if (!fr.getReceiver().getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_REQUEST); // 403 Forbidden
        }
        FriendRequest updatedFr;

        // action 처리
        if ("ACCEPT".equalsIgnoreCase(action)) {
            // 수락: 상태 변경
            updatedFr = fr.accept(); // status를 accep로 변경
            friendRequestRepository.save(updatedFr);
        } else if ("DECLINE".equalsIgnoreCase(action)) {
            // 거부: 레코드 삭제
            friendRequestRepository.delete(fr);
        } else {
            throw new BusinessException(ErrorCode.INVALID_ACTION);
        }
    }


}
