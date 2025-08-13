package hello.typing_game_be.myLongTextScore.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import hello.typing_game_be.common.security.CustomUserDetails;
import hello.typing_game_be.longTextScore.dto.LongTextScoreRequest;
import hello.typing_game_be.myLongTextScore.service.MyLongTextScoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MyLongTextScoreController {
    private final MyLongTextScoreService myLongTextScoreService;

    @PostMapping("/my-long-text/{myLongTextId}/score")
    public ResponseEntity<Long> register(@PathVariable Long myLongTextId,@Valid @RequestBody LongTextScoreRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long id = myLongTextScoreService.register(myLongTextId,request,userDetails.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }
}
