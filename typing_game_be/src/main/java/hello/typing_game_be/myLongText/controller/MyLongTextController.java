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
import hello.typing_game_be.myLongText.dto.MyLongTextListResponse;
import hello.typing_game_be.myLongText.dto.MyLongTextResponse;
import hello.typing_game_be.myLongText.service.MyLongTextService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MyLongTextController {
    private final MyLongTextService myLongTextService;
    //'나의긴글' 저장
    @PostMapping("/my-long-text")
    public ResponseEntity<Long> register(@Valid @RequestBody MyLongTextRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long id = myLongTextService.register(request,userDetails.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }
    //'나의긴글' 삭제 //권한체크(서비스코드)
    @DeleteMapping("/my-long-text/{myLongTextId}")
    public ResponseEntity<Void> delete(@PathVariable Long myLongTextId ,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        myLongTextService.deleteById(myLongTextId, userDetails.getUserId());

        return ResponseEntity.ok().build();
    }

    //'나의긴글' 목록 조회
    @GetMapping("/my-long-text")
    public ResponseEntity<List<MyLongTextListResponse>> getMyLongTexts(@AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.status(HttpStatus.OK)
            .body( myLongTextService.getByUserId(userDetails.getUserId()));
    }
    //'나의긴글' 단건 조회  //권한체크(서비스코드)
    @GetMapping("/my-long-text/{myLongTextId}")
    public ResponseEntity<MyLongTextResponse> getMyLongText(@PathVariable Long myLongTextId ,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.status(HttpStatus.OK)
            .body( myLongTextService.getMyLongText(myLongTextId, userDetails.getUserId()) );
    }
}
