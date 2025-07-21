package hello.typing_game_be.common.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDetails> handleBusinessException(BusinessException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ErrorDetails.of(e));
    }

    // 입력값 Validation 위반 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(
        MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            //fieldName : 유효성 검증에 실패한 DTO의 필드 이름
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    //데이터 무결성 등 db관련 예외

    // 공통 응답 생성 메서드
    private ResponseEntity<ErrorDetails> buildErrorResponse(ErrorCode code, String customMessage) {
        return ResponseEntity.status(code.getHttpStatus())
            .body(new ErrorDetails(code.getHttpStatus(), code.getErrorCode(), customMessage));
    }
}