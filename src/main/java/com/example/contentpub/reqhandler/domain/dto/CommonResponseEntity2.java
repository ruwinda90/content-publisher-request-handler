package com.example.contentpub.reqhandler.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponseEntity2<T> { // todo - will be renamed soon

    private Integer httpStatusCode; // HTTP status code.

    private String code; // The domain status code.

    private String description; // Optional description.

    private T data; // Response from inner service.

}
