package com.example.contentpub.reqhandler.domain.exception;

import lombok.Getter;

@Getter
public class DataNotFoundException extends BaseException {

    public DataNotFoundException(String code, String description) {
        super(code, description);
    }

}
