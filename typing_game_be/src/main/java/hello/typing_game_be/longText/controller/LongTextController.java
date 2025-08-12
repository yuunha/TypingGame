package hello.typing_game_be.longText.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import hello.typing_game_be.longText.dto.LongTextListResponse;
import hello.typing_game_be.longText.dto.LongTextResponse;
import hello.typing_game_be.longText.service.LongTextService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LongTextController {
    private final LongTextService longTextService;

    @GetMapping("/long-text")
    public ResponseEntity<Result> getLongTextList() {
        List<LongTextListResponse> longTextList = longTextService.getLongTextList();

        return ResponseEntity.ok(new Result(longTextList));
    }


    @GetMapping("/long-text/{longTextId}")
    public ResponseEntity<LongTextResponse> getLongText(@PathVariable Long longTextId) {

        return ResponseEntity.ok(longTextService.getLongTextById(longTextId));
    }
    @Data
    @AllArgsConstructor
    static class Result<T> {
        //private int count; //필드 추가 가능
        private T data;
    }

}
