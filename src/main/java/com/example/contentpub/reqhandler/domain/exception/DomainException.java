package com.example.contentpub.reqhandler.domain.exception;

import com.example.contentpub.reqhandler.domain.constants.StatusCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DomainException extends Exception {

    private final String code;
    private final HttpStatus httpStatus;

    public DomainException(String code, String description, HttpStatus httpStatus) {
        super(description);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public DomainException(StatusCode statusCode) {
        super(statusCode.getDescription());
        this.code = statusCode.getCode();
        this.httpStatus = statusCode.getHttpStatus();
    }

}
