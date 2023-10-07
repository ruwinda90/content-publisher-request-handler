package com.example.contentpub.reqhandler.domain.service.auth;

import com.example.contentpub.reqhandler.domain.dto.AuthRequestEntity;
import com.example.contentpub.reqhandler.domain.dto.AuthResponseEntity;
import com.example.contentpub.reqhandler.domain.dto.CommonResponseEntity2;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserAuthService extends UserDetailsService {

    CommonResponseEntity2<AuthResponseEntity> createToken(AuthRequestEntity authRequestEntity);

    CommonResponseEntity2<String> createUser(AuthRequestEntity userRegRequestEntity);

}
