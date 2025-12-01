package hello.typing_game_be.friend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.typing_game_be.common.exception.BusinessException;
import hello.typing_game_be.common.exception.ErrorCode;
import hello.typing_game_be.friend.dto.FriendRequestListResponse;
import hello.typing_game_be.friend.entity.FriendRequest;
import hello.typing_game_be.friend.repository.FriendRequestRepository;
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
            .build();
        friendRequestRepository.save(fr);

        return fr.getFriendRequestId();
    }


    //친구 요청 삭제
    public void deleteRequest(FriendRequest friendRequest){
        // FriendRequest fr = friendRequestRepository.findByRequesterUserIdAndReceiverUserId(requesterId,receiverId)
        //     .orElseThrow(()->new BusinessException(ErrorCode.FRIEND_REQEUST_NOT_FOUND));
        friendRequestRepository.delete(friendRequest);
    }

    //보낸 친구요청 조회
    public List<FriendRequestListResponse> getSentFriendRequests(Long userId) {

        //최근요청이 배열의 맨앞에
        List<FriendRequest> list = friendRequestRepository.findByRequesterUserIdOrderByCreatedAtDesc(userId);

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

    public FriendRequest findById(Long friendRequestId) {
        return friendRequestRepository.findById(friendRequestId)
            .orElseThrow(()->new BusinessException(ErrorCode.FRIEND_REQEUST_NOT_FOUND));
    }
}
