package hello.typing_game_be.longTextScore.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import hello.typing_game_be.common.security.CustomUserDetails;
import hello.typing_game_be.longTextScore.dto.LongTextScoreRequest;
import hello.typing_game_be.longTextScore.service.LongTextScoreService;
import hello.typing_game_be.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LongTextScoreContoller {
    private final LongTextScoreService longTextScoreService;
    private final UserService userService;

    @PostMapping("/long-text/{longTextId}/score")
    public ResponseEntity<Void> register(@PathVariable Long longTextId,@Valid @RequestBody LongTextScoreRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        longTextScoreService.register(longTextId,request,userDetails.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // @PreAuthorize("#userId == authentication.principal.userId")
    // @GetMapping("/user/{userId}/long-score")
    // public ResponseEntity<List<LongTextScoreResponse>> getLongScore(@PathVariable Long userId) {
    //
    //     List<LongTextScoreResponse> response = longTextScoreService.getLongScoreByUserId(userId);
    //     return ResponseEntity.ok(response);
    // }

    // @GetMapping("/ranking/long-score")
    // public ResponseEntity<List<LongTextScoreRankingResponse>> getLongScoreRankByTitle(@RequestParam String title) {
    //
    //     List<LongTextScoreRankingResponse> ranking = longTextScoreService.getLongScoreByTitle(title);
    //     return ResponseEntity.ok(ranking);
    // }


}
