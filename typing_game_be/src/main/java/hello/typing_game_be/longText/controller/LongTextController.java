package hello.typing_game_be.longText.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.typing_game_be.longText.dto.LongTextResponse;
import hello.typing_game_be.longText.service.LongTextService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LongTextController {
    private final LongTextService longTextService;

    @GetMapping("/long-text")
    public ResponseEntity<List<LongTextResponse>> getLongTextList() {
        List<LongTextResponse> longTextList = longTextService.getLongTextList();

        return ResponseEntity.ok(longTextList);
    }

}
