package hello.typing_game_be.friendRequest.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.typing_game_be.common.exception.BusinessException;
import hello.typing_game_be.common.exception.ErrorCode;
import hello.typing_game_be.friendRequest.dto.FriendRequestListResponse;
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

    // 친구 요청 생성
    public Long sendFriendRequest(Long requesterId, Long receiverId) {
        User requester = userRepository.findById(requesterId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND,"요청자 없음"));
        User receiver = userRepository.findById(receiverId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND,"수신자 없음"));

        // 중복 검증 (양방향)
        if(friendRequestRepository.existsByRequesterAndReceiver(requester, receiver)){
            throw new BusinessException(ErrorCode.FRIEND_REQUEST_ALREADY_SENT);
        }
        if(friendRequestRepository.existsByRequesterAndReceiver(receiver, requester)){
            throw new BusinessException(ErrorCode.FRIEND_REQUEST_ALREADY_RECEIVED);
        }


        FriendRequest fr = FriendRequest.builder()
            .requester(requester)
            .receiver(receiver)
            .status(FriendRequestStatus.PENDING)
            .build();
        friendRequestRepository.save(fr);

        return fr.getFriendRequestId();
    }

    // 친구 요청에 대한 수락/거부
    @Transactional
    public void respondToFriendRequest(Long friendRequestId, Long userId, String action) {
        FriendRequest fr = friendRequestRepository.findById(friendRequestId)
            .orElseThrow(() -> new BusinessException(ErrorCode.FRIEND_REQEUST_NOT_FOUND));

        // 요청 수신자가 맞는지 확인
        if (!fr.getReceiver().getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_REQUEST); // 403 Forbidden
        }
        // action 처리
        if ("ACCEPT".equalsIgnoreCase(action)) {
            // 수락: 상태 변경
            fr.accept(); // status를 accep로 변경
        } else if ("DECLINE".equalsIgnoreCase(action)) {
            // 거부: 레코드 삭제
            friendRequestRepository.delete(fr);
        } else {
            throw new BusinessException(ErrorCode.INVALID_ACTION);
        }
    }

    //보낸 친구요청 조회
    public List<FriendRequestListResponse> getSentFriendRequests(Long userId) {

        //최근요청이 배열의 맨앞에
        List<FriendRequest> list = friendRequestRepository.findByRequesterUserIdAndStatusOrderByCreatedAtDesc(userId,FriendRequestStatus.PENDING);

        return list.stream()
            .map(FriendRequestListResponse::fromEntity)
            .collect(Collectors.toList());

    }


    // 받은 친구 요청 조회
    public List<FriendRequestListResponse> getReceivedFriendRequests(Long userId) {
        // 최근 요청이 배열의 맨 앞에 오도록 정렬
        List<FriendRequest> list = friendRequestRepository.findByReceiverUserIdOrderByCreatedAtDesc(userId);

        return list.stream()
            .map(FriendRequestListResponse::fromEntity)
            .collect(Collectors.toList());
    }
}
