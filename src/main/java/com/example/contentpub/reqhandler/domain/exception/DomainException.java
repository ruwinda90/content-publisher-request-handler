package com.example.contentpub.reqhandler.domain.exception;

import com.example.contentpub.reqhandler.domain.constants.ErrorCode;
import lombok.Getter;

@Getter
public class DomainException extends Exception {

    private final Integer httpStatusCode;

    public DomainException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.httpStatusCode = errorCode.getHttpStatusCode();
    }

}
