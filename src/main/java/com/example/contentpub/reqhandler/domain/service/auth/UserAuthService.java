package com.example.contentpub.reqhandler.domain.service.auth;

import com.example.contentpub.reqhandler.domain.dto.AuthRequestEntity;
import com.example.contentpub.reqhandler.domain.dto.AuthResponseEntity;
import com.example.contentpub.reqhandler.domain.dto.CommonResponseEntity2;
import com.example.contentpub.reqhandler.domain.exception.DomainException;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserAuthService extends UserDetailsService {

    CommonResponseEntity2<AuthResponseEntity> loginUser(AuthRequestEntity authRequestEntity) throws DomainException;

    CommonResponseEntity2<AuthResponseEntity> refresh(String refreshToken) throws DomainException;

    CommonResponseEntity2<String> createUser(AuthRequestEntity userRegRequestEntity);

}
