package hello.typing_game_be.friend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hello.typing_game_be.friend.entity.Friend;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
}
