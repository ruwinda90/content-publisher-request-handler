package com.example.contentpub.reqhandler.domain.service.auth;

import com.example.contentpub.reqhandler.domain.dto.AuthRequestEntity;
import com.example.contentpub.reqhandler.domain.dto.AuthResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserAuthService extends UserDetailsService {

    AuthResponseEntity createToken(AuthRequestEntity authRequestEntity);

    AuthResponseEntity createUser(AuthRequestEntity userRegRequestEntity);

}
