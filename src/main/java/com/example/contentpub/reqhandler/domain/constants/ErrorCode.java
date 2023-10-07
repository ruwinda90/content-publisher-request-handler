package com.example.contentpub.reqhandler.domain.constants;

import lombok.Getter;

@Getter
public enum ErrorCode {

    EMAIL_ALREADY_IN_USE("Email already in use", 400),
    EMAIL_NOT_FOUND("Email not found", 400);

    private final String description;
    private final Integer httpStatusCode;

    ErrorCode(String description, Integer httpStatusCode) {
        this.description = description;
        this.httpStatusCode = httpStatusCode;
    }

}
