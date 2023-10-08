package com.example.contentpub.reqhandler.domain.exception;

import lombok.Getter;

@Getter
public class BaseException extends Exception {

    private final String code;

    public BaseException(String code, String description) {
        super(description);
        this.code = code;
    }

}
