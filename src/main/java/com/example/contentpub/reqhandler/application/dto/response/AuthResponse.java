package com.example.contentpub.reqhandler.application.dto.response;

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
public class AuthResponse {

    private String token;

    private Long writerId;

    private List<String> roles = new ArrayList<>();

}
