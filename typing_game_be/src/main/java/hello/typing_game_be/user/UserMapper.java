package hello.typing_game_be.user;

public class UserMapper {
    public static User toEntity(UserRequest userRequest) {
        return User.builder()
            .username(userRequest.getUsername())
            .loginId(userRequest.getLoginId())
            .password(userRequest.getPassword())
            .build();
    }
}
