package hello.typing_game_be.longTextScore.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hello.typing_game_be.common.security.CustomUserDetails;
import hello.typing_game_be.longTextScore.dto.LongTextScoreWithUsernameResponse;
import hello.typing_game_be.longTextScore.dto.LongTextScoreRequest;
import hello.typing_game_be.longTextScore.dto.LongTextScoreWithTitleResponse;
import hello.typing_game_be.longTextScore.entity.LongTextScore;
import hello.typing_game_be.longTextScore.service.LongTextScoreService;
import hello.typing_game_be.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LongTextScoreContoller {
    private final LongTextScoreService longTextScoreService;

    //TODO : 권한 체크(서비스단에서)
    @PostMapping("/long-text/{longTextId}/score")
    public ResponseEntity<Long> register(@PathVariable Long longTextId,@Valid @RequestBody LongTextScoreRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long id = longTextScoreService.register(longTextId,request,userDetails.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }


    @GetMapping("/user/long-text/scores") // 유저의 점수 목록 전체조회
    public ResponseEntity<Result> getLongScore(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        List<LongTextScoreWithTitleResponse> list = longTextScoreService.getLongScoresByUserId(userId);
        return ResponseEntity.ok(new Result(list));
    }
    //TODO : 권한 체크(서비스단에서)
    @GetMapping("/user/long-text/{longTextId}/scores") // 유저의 특정 긴글에 대한 점수 목록 조회
    public ResponseEntity<Result> getLongScoreRankByLongText(@PathVariable Long longTextId,@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        List<LongTextScoreWithUsernameResponse> list = longTextScoreService.getScoresWithUsernamesByLongTextIdAndUserId(userId,longTextId);
        return ResponseEntity.ok(new Result(list));
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        //private int count; //필드 추가 가능
        private T data;
    }
}
