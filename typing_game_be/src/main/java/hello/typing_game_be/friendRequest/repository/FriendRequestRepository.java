package hello.typing_game_be.friendRequest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import hello.typing_game_be.friendRequest.entity.FriendRequest;
import hello.typing_game_be.user.entity.User;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    boolean existsByRequesterAndReceiver(User requester, User receiver);

    Optional<FriendRequest> findByRequesterUserIdAndReceiverUserId(Long requesterId, Long receiverId);

    @Query("SELECT fr FROM FriendRequest fr " +
        "WHERE fr.status = 'ACCEPTED' " +
        "AND (fr.requester.userId = :userId OR fr.receiver.userId = :userId)")
    List<FriendRequest> findAcceptedFriends(@Param("userId") Long userId);

    //최근요청이 배열의 맨앞에
    //보낸 요청 목록(요청한 userid + 요청상태(pending) )
    List<FriendRequest> findByRequesterUserIdOrderByCreatedAtDesc(Long userId);

    List<FriendRequest> findByReceiverUserIdOrderByCreatedAtDesc(Long userId);
}
