package hello.typing_game_be.longScore.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import hello.typing_game_be.longScore.dto.LongScoreRequest;
import hello.typing_game_be.longScore.repository.LongScoreRepository;
import hello.typing_game_be.longScore.service.LongScoreService;
import hello.typing_game_be.user.dto.UserRequest;
import hello.typing_game_be.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LongScoreContoller {
    private final LongScoreService longScoreService;

    @PreAuthorize("#userId == authentication.principal.userId")
    @PostMapping("/user/{userId}/long-score")
    public ResponseEntity<String> register(@PathVariable Long userId,@Valid @RequestBody LongScoreRequest request) {

        longScoreService.register(userId,request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
