package hello.typing_game_be.friendRequest.service;

import org.springframework.stereotype.Service;

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
}
