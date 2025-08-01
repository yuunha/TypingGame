package hello.typing_game_be.longTextScore.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hello.typing_game_be.longTextScore.dto.LongTextScoreRankingResponse;
import hello.typing_game_be.longTextScore.dto.LongTextScoreRequest;
import hello.typing_game_be.longTextScore.dto.LongTextScoreResponse;
import hello.typing_game_be.longTextScore.service.LongTextScoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LongTextScoreContoller {
    private final LongTextScoreService longTextScoreService;

    @PreAuthorize("#userId == authentication.principal.userId")
    @PostMapping("/user/{userId}/long-score")
    public ResponseEntity<String> register(@PathVariable Long userId,@Valid @RequestBody LongTextScoreRequest request) {

        longTextScoreService.register(userId,request);
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
