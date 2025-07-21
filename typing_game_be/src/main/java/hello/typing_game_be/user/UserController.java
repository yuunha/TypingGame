package hello.typing_game_be.user;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    @PostMapping("/signup")
    public String register(@RequestParam String loginId, @RequestParam String username, @RequestParam String password) {
        userService.register(loginId, username, password);
        return "회원가입 성공!";
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String loginId, @RequestParam String password) {
        boolean result = userService.login(loginId, password);
        if (result) {
            return ResponseEntity.ok("로그인 성공");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
        }
    }


}
