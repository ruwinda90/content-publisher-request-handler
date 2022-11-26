package com.example.contentpub.reqhandler.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseEntity {

    private Integer statusCode;

    private String status;

    private String description;

}
