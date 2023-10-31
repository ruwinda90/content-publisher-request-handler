package com.example.contentpub.reqhandler.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseEntity {

    private String accessToken;

    private String refreshToken;

    private Long writerId;

    private List<String> roles = new ArrayList<>();

}
