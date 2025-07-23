package hello.typing_game_be.user.mapper;

import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.dto.UserRequest;

public class UserMapper {
    public static User toEntity(UserRequest userRequest) {
        return User.builder()
            .username(userRequest.getUsername())
            .loginId(userRequest.getLoginId())
            .password(userRequest.getPassword())
            .build();
    }
}
