package hello.typing_game_be.quote.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.typing_game_be.quote.dto.QuoteResponse;
import hello.typing_game_be.quote.service.QuoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class QuoteController {
    private final QuoteService quoteService;

    @Operation( summary = "오늘의 명언 조회", responses = {
        @ApiResponse(responseCode = "200", description = "오늘의 명언 조회 성공")})
    @GetMapping("/quote/today")
    public ResponseEntity<QuoteResponse> getMyLongTexts() {

        return ResponseEntity.status(HttpStatus.OK)
            .body(quoteService.getTodayQuote());
    }
}
