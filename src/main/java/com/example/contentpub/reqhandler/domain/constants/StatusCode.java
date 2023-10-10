package com.example.contentpub.reqhandler.domain.constants;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum StatusCode {

    SUCCESS("1000", "Success", HttpStatus.OK),
    CREATED("1000", "Created", HttpStatus.CREATED),
    EMAIL_ALREADY_IN_USE("2000", "Email already in use", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_FOUND("2001", "Email not found", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS("2002", "Username and password does not match", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_NOT_FOUND("2001", "Refresh token not found", HttpStatus.UNAUTHORIZED),
    REFRESH_COOKIE_NOT_FOUND("2001", "Refresh cookie not found", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_EXPIRED("2002", "Refresh token is expired", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_INVALID("2003", "Refresh token is not valid", HttpStatus.UNAUTHORIZED),

    BACKEND_RESP_PARSE_FAILURE("3000", "Failed to parse backend response", HttpStatus.INTERNAL_SERVER_ERROR),
    BACKEND_TIMEOUT("3001", "Failed to parse backend response", HttpStatus.INTERNAL_SERVER_ERROR),
    INTERNAL_ERROR("5000", "Internal Error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String description;
    private final HttpStatus httpStatus;

    StatusCode(String code, String description, HttpStatus httpStatus) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }

}
