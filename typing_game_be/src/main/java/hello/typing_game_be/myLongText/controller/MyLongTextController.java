package hello.typing_game_be.myLongText.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import hello.typing_game_be.common.security.CustomUserDetails;
import hello.typing_game_be.myLongText.dto.MyLongTextRequest;
import hello.typing_game_be.myLongText.dto.MyLongTextResponse;
import hello.typing_game_be.myLongText.service.MyLongTextService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MyLongTextController {
    private final MyLongTextService myLongTextService;
    //나의 긴글 저장
    @PostMapping("/user/my-long-text")
    public ResponseEntity<Long> register(@Valid @RequestBody MyLongTextRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long id = myLongTextService.register(request,userDetails.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }
    //나의 긴글 삭제
    @DeleteMapping("/user/my-long-text/{myLongTextId}")
    public ResponseEntity<Void> delete(@PathVariable Long myLongTextId ,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        myLongTextService.deleteById(myLongTextId, userDetails.getUserId());

        return ResponseEntity.ok().build();
    }

    //유저의 긴글 목록 조회
    @GetMapping("/user/my-long-text")
    public ResponseEntity<List<MyLongTextResponse>> getLongTexts(@AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.status(HttpStatus.OK)
            .body( myLongTextService.getByUserId(userDetails.getUserId() ));
    }
}
