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
import hello.typing_game_be.longTextScore.dto.LongTextScoreSimpleResponse;
import hello.typing_game_be.longTextScore.dto.LongTextScoreSummuryResponse;
import hello.typing_game_be.longTextScore.dto.LongTextScoreRequest;
import hello.typing_game_be.longTextScore.service.LongTextScoreService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LongTextScoreContoller {
    private final LongTextScoreService longTextScoreService;


    @PostMapping("/long-text/{longTextId}/score")
    public ResponseEntity<Long> register(@PathVariable Long longTextId,@Valid @RequestBody LongTextScoreRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long id = longTextScoreService.register(longTextId,request,userDetails.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }


    @GetMapping("/long-text/scores") // 유저의 점수 목록 전체조회
    public ResponseEntity<Result> getLongScore(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        List<LongTextScoreSummuryResponse> list = longTextScoreService.getLongScoresByUserId(userId);
        return ResponseEntity.ok(new Result(list));
    }

    @GetMapping("/long-text/{longTextId}/scores") // 유저의 특정 긴글에 대한 점수 목록 조회
    public ResponseEntity<Result> getLongScoreByLongText(@PathVariable Long longTextId,@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        List<LongTextScoreSimpleResponse> list = longTextScoreService.getScoresWithUsernamesByLongTextIdAndUserId(userId,longTextId);
        return ResponseEntity.ok(new Result(list));
    }

    @GetMapping("/long-text/{longTextId}/score")
    public ResponseEntity<ScoreResponse> getTopScore(@PathVariable Long longTextId,@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        int score = longTextScoreService.getTopScoreByLongText(userId,longTextId);
        return ResponseEntity.ok(new ScoreResponse(score));
    }
    @Data
    @AllArgsConstructor
    static class Result<T> {
        //private int count; //필드 추가 가능
        private T data;
    }
    @Data
    @AllArgsConstructor
    static class ScoreResponse {
        //private int count; //필드 추가 가능
        private int score;
    }
}
