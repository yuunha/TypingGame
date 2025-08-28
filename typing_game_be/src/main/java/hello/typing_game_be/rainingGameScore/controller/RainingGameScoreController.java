package hello.typing_game_be.rainingGameScore.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.core.Context;
import hello.typing_game_be.common.security.CustomUserDetails;
import hello.typing_game_be.rainingGameScore.dto.RainingGameScoreRequest;
import hello.typing_game_be.rainingGameScore.dto.RainingGameScoreResponse;
import hello.typing_game_be.rainingGameScore.service.RainingGameScoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RainingGameScoreController {

    private final RainingGameScoreService rainingGameScoreService;

    @PostMapping("/raining-game-score")
    public ResponseEntity<Long> register(@Valid @RequestBody RainingGameScoreRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long id = rainingGameScoreService.register(request,userDetails.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/raining-game-score")
    public ResponseEntity<List<RainingGameScoreResponse>> getScores(@AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.status(HttpStatus.OK).body(
            rainingGameScoreService.getScores(userDetails.getUserId())
        );
    }

}
