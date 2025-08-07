package hello.typing_game_be.user.mapper;

import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.dto.UserCreateRequest;

public class UserMapper {
    public static User toEntity(UserCreateRequest userCreateRequest) {
        return User.builder()
            .username(userCreateRequest.getUsername())
            .loginId(userCreateRequest.getLoginId())
            .password(userCreateRequest.getPassword())
            .build();
    }
}
