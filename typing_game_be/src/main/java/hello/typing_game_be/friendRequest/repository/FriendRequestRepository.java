package hello.typing_game_be.friendRequest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hello.typing_game_be.friendRequest.entity.FriendRequest;
import hello.typing_game_be.user.entity.User;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    boolean existsByRequesterAndReceiver(User requester, User receiver);

    Optional<FriendRequest> findByRequesterAndReceiver(User requester, User receiver);
}
