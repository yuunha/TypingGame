package hello.typing_game_be.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
    //user
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-001", "존재하지 않는 아이디입니다."),
    DUPLICATE_LOGINID(HttpStatus.CONFLICT, "USER-002", "이미 존재하는 아아디입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED,"USER-003", "비밀번호가 일치하지 않습니다."),

    //DB
    DB_CONSTRAINT_VIOLATION(HttpStatus.BAD_REQUEST, "COMMON-001", "데이터 무결성 제약조건 위반"),

    //기타
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND,"COMMON-002","요청하신 리소스를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }
}