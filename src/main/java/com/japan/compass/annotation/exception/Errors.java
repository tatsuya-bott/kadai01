package com.japan.compass.annotation.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum Errors {

    INVALID_REQUEST("E0001", "invalid request.", HttpStatus.BAD_REQUEST),
    DB_ERROR("E1000", "db error.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_IO_ERROR("E2000", "file io error.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_ALREADY_EXIST_ERROR("E2001", "file already exist error.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_UPLOAD_SIZE_EXCEED("E2002", "file upload size exceed.", HttpStatus.BAD_REQUEST),
    FILE_NOT_FOUND("E2003", "file not found.", HttpStatus.NOT_FOUND),
    LOGIN_DISABLE_ERROR("E3000", "login user is disabled.", HttpStatus.BAD_REQUEST),
    ILLEGAL_ARGUMENT_ERROR("E9000", "illegal argument error.", HttpStatus.INTERNAL_SERVER_ERROR),
    ILLEGAL_STATE_ERROR("E9001", "illegal state error.", HttpStatus.INTERNAL_SERVER_ERROR),
    INTERNAL_SERVER_ERROR("E9999", "internal server error.", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;

    @Override
    public String toString() {
        return "code:" + this.code + " message:" + this.message;
    }
}
