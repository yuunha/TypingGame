package hello.typing_game_be.myLongText.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hello.typing_game_be.myLongText.entity.MyLongText;
import hello.typing_game_be.user.entity.User;

@Repository
public interface MyLongTextRepository extends JpaRepository<MyLongText, Long> {

    List<MyLongText> findByUser(User user);
    List<MyLongText> findByUser_UserId(Long userId);


}
