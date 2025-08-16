package hello.typing_game_be.user.controller;


import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hello.typing_game_be.user.dto.UserCreateRequest;
import hello.typing_game_be.user.dto.UserResponse;
import hello.typing_game_be.user.dto.UserUpdateRequest;
import hello.typing_game_be.user.entity.User;
import hello.typing_game_be.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {

    private final UserService userService;
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
    @PostMapping("/user")
    public ResponseEntity<Long> register(@Valid @RequestBody UserCreateRequest request) {
        Long userId = userService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(userId);
    }
    // @PostMapping("/login")
    // public ResponseEntity<String> login(@Valid @RequestBody UserRequest request) {
    //     userService.login(request);
    //     return ResponseEntity.status(HttpStatus.OK).build();
    // }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable("id") Long id,@Valid @RequestBody UserUpdateRequest request) {
        userService.update(id,request.getUsername());
        User user = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(
            new UserResponse(user.getUserId(),user.getLoginId(),user.getUsername()));
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponse> getUser(Authentication authentication) {
        String loginId = authentication.getName(); // 현재 로그인 사용자 ID
        UserResponse userResponse = userService.getUserByLoginId(loginId);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(Authentication authentication) {
        String loginId = authentication.getName();
        userService.deleteUserByLoginId(loginId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // username으로 유저 검색 : GET GET /users?username=홍길&page=0&size=5
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> searchUsers(
        @RequestParam String username,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("username").ascending());
        List<UserResponse> users = userService.searchUsersByUsername(username,pageable);
        return ResponseEntity.ok(users);
    }


}
