package hello.typing_game_be.friend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hello.typing_game_be.friend.entity.Friend;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    Optional<Friend> findByUserAUserIdAndUserBUserId(Long userAId, Long userBId);


    @Query("""
        select f from Friend f
        where f.userA.userId = :userId or f.userB.userId = :userId
    """)
    List<Friend> findAllByUserId(Long userId);

}
