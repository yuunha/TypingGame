package hello.typing_game_be.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
    //user
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-001", "존재하지 않는 유저입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "USER-002", "이미 존재하는 nickname입니다."),

    //long-text
    LONG_TEXT_NOT_FOUND(HttpStatus.NOT_FOUND,"LONG-TEXT-001","존재하지 않는 긴글입니다."),
    //Long-score
    LONG_SCORE_TITLE_NOT_FOUND(HttpStatus.NOT_FOUND,"LONG-SCORE-001","존재하지 않는 title입니다."),
    //my-long-text
    MY_LONG_TEXT_NOT_FOUND(HttpStatus.NOT_FOUND,"MY-LONG-TEXT-001","존재하지 않는 '나의 긴글'입니다."),
    //friend-request
    FRIEND_REQEUST_NOT_FOUND(HttpStatus.NOT_FOUND,"FRIEND-REQEUST-001","존재하지 않는 친구 요청입니다."),
    INVALID_ACTION(HttpStatus.BAD_REQUEST,"FRIEND-REQEUST-002","유효하지 않은 action입니다." ),
    FRIEND_REQUEST_ALREADY_SENT(HttpStatus.BAD_REQUEST,"FRIEND-REQEUST-003","이미 보낸 친구 요청이 존재합니다."),
    FRIEND_REQUEST_ALREADY_RECEIVED(HttpStatus.BAD_REQUEST,"FRIEND-REQEUST-004", "상대방이 이미 친구 요청을 보냈습니다."),
    //friend
    FRIEND_NOT_FOUND(HttpStatus.NOT_FOUND,"FRIEND-001","존재하지 않는 친구관계입니다." ),

    //quote
    QUOTE_NOT_FOUND(HttpStatus.NOT_FOUND,"QUOTE-001","존재하지 않는 명언입니다."),
    //constitution
    CONSTITUTION_NOT_FOUND(HttpStatus.NOT_FOUND,"CONSTITUTION-001","존재하지 않는 헌법 조문입니다."),
    CONSTITUTION_PROGRESS_NOT_FOUND(HttpStatus.NOT_FOUND,"CONSTITUTION-002","헌법따라치기 진행상황이 존재하지 않습니다"),

    //DB
    DB_CONSTRAINT_VIOLATION(HttpStatus.BAD_REQUEST, "COMMON-001", "데이터 무결성 제약조건 위반"),

    //기타
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND,"COMMON-002","요청하신 리소스를 찾을 수 없습니다."),

    //IO
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FILE-001", "파일 업로드 중 오류가 발생했습니다."),

    //권한
    FORBIDDEN_REQUEST(HttpStatus.FORBIDDEN, "COMMON-003","권한이 없습니다." );


    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }
}