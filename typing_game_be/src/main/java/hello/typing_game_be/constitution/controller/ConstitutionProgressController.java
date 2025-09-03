package hello.typing_game_be.constitution.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import hello.typing_game_be.common.security.CustomUserDetails;
import hello.typing_game_be.constitution.dto.ProgressRequest;
import hello.typing_game_be.constitution.service.ConstitutionProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ConstitutionProgressController {

    private final ConstitutionProgressService constitutionProgressService;

    @Operation( summary = "유저의 헌법 따라치기 진행상황(조 번호 + 조문 내 글자 인덱스) 저장", responses = {
        @ApiResponse(responseCode = "201", description = "회원가입 성공"),
        @ApiResponse(responseCode = "409", description = "loginId 중복 또는 username 중복")
    })
    @PostMapping("/constitution/progress")
    public ResponseEntity<Void> saveConstitutionProgress(
        @Valid @RequestBody ProgressRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();
        constitutionProgressService.saveProgress(userId,request.getArticleIndex(),request.getLastPosition());
        return ResponseEntity.ok().build();
    }
}
