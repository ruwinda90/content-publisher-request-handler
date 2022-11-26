package com.example.contentpub.reqhandler.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequestEntity {

    private String email;

    private String password;

}
