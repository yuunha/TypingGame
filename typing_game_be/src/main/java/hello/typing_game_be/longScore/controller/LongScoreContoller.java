package hello.typing_game_be.longScore.controller;

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

import hello.typing_game_be.longScore.dto.LongScoreRankingResponse;
import hello.typing_game_be.longScore.dto.LongScoreRequest;
import hello.typing_game_be.longScore.dto.LongScoreResponse;
import hello.typing_game_be.longScore.service.LongScoreService;
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

    @PreAuthorize("#userId == authentication.principal.userId")
    @GetMapping("/user/{userId}/long-score")
    public ResponseEntity<List<LongScoreResponse>> getLongScore(@PathVariable Long userId) {

        List<LongScoreResponse> response = longScoreService.getLongScoreByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ranking/long-score")
    public ResponseEntity<List<LongScoreRankingResponse>> getLongScoreRankByTitle(@RequestParam String title) {

        List<LongScoreRankingResponse> ranking = longScoreService.getLongScoreByTitle(title);
        return ResponseEntity.ok(ranking);
    }


}
