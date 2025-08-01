package hello.typing_game_be.longTextScore.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import hello.typing_game_be.common.security.CustomUserDetails;
import hello.typing_game_be.longTextScore.dto.LongTextScoreRequest;
import hello.typing_game_be.longTextScore.dto.LongTextScoreResponse;
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


    @GetMapping("/long-text/score") // 유저의 긴글점수 목록 조회
    public ResponseEntity<List<LongTextScoreResponse>> getLongScore(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();

        return ResponseEntity.ok(longTextScoreService.getLongScoresByUserId(userId));
    }

    // @GetMapping("/ranking/long-score")
    // public ResponseEntity<List<LongTextScoreRankingResponse>> getLongScoreRankByTitle(@RequestParam String title) {
    //
    //     List<LongTextScoreRankingResponse> ranking = longTextScoreService.getLongScoreByTitle(title);
    //     return ResponseEntity.ok(ranking);
    // }


}
