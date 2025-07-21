package hello.typing_game_be.common.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorDetails {
    private HttpStatus httpStatus;
    private String errorCode;
    private String message;

    public static ErrorDetails of(BusinessException e) {
        return new ErrorDetails(e.getErrorCode().getHttpStatus(), e.getErrorCode().getErrorCode(), e.getMessage());
    }
}