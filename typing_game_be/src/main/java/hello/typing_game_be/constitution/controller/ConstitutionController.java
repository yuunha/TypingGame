package hello.typing_game_be.constitution.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import hello.typing_game_be.constitution.dto.ConstitutionResponse;
import hello.typing_game_be.constitution.service.ConstitutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ConstitutionController {
    private final ConstitutionService constitutionService;


    @Operation( summary = "조 번호로 헌법 조회", responses = {
        @ApiResponse(responseCode = "200", description = "헌법 조회 성공")
    })
    @GetMapping("/constitution/{articleIndex}")
    public ResponseEntity<ConstitutionResponse> getConstitution(@PathVariable int articleIndex) {

        return ResponseEntity.ok()
            .body(constitutionService.getConstitutionByArticleIndex(articleIndex));
    }
}
