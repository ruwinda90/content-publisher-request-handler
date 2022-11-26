package com.example.contentpub.reqhandler.domain.constants;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
public class Role implements GrantedAuthority {

    public static final String USER_READER = "USER_READER";

    public static final String USER_WRITER = "USER_WRITER";

    private String authority;

}
