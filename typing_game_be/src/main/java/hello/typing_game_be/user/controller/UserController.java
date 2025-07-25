package hello.typing_game_be.user.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.typing_game_be.user.dto.UserRequest;
import hello.typing_game_be.user.dto.UserResponse;
import hello.typing_game_be.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    @PostMapping
    public ResponseEntity<String> register(@Valid @RequestBody UserRequest request) {
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    // @PostMapping("/login")
    // public ResponseEntity<String> login(@Valid @RequestBody UserRequest request) {
    //     userService.login(request);
    //     return ResponseEntity.status(HttpStatus.OK).build();
    // }

    @GetMapping
    public ResponseEntity<UserResponse> getUser(Authentication authentication) {
        String loginId = authentication.getName(); // 현재 로그인 사용자 ID
        UserResponse userResponse = userService.getUserByLoginId(loginId);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(Authentication authentication) {
        String loginId = authentication.getName();
        userService.deleteUserByLoginId(loginId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
