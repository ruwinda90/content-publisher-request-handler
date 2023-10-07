package com.example.contentpub.reqhandler.domain.constants;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum StatusCodes {

    SUCCESS("1000", "Success", HttpStatus.OK),
    CREATED("1000", "Created", HttpStatus.CREATED),
    EMAIL_ALREADY_IN_USE("2000", "Email already in use", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_FOUND("2001", "Email not found", HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR("5000", "Internal Error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String description;
    private final HttpStatus httpStatus;

    StatusCodes(String code, String description, HttpStatus httpStatus) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }

}
