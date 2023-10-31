package com.example.contentpub.reqhandler.domain.service.auth;

import com.example.contentpub.reqhandler.domain.dto.AuthRequestEntity;
import com.example.contentpub.reqhandler.domain.dto.AuthResponseEntity;
import com.example.contentpub.reqhandler.domain.dto.CommonResponseEntity;
import com.example.contentpub.reqhandler.domain.exception.DomainException;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserAuthService extends UserDetailsService {

    CommonResponseEntity<AuthResponseEntity> loginUser(AuthRequestEntity authRequestEntity) throws DomainException;

    CommonResponseEntity<AuthResponseEntity> refresh(String refreshToken) throws DomainException;

    CommonResponseEntity<String> createUser(AuthRequestEntity userRegRequestEntity) throws DomainException;

    CommonResponseEntity<String> logoutUser(Integer userId, String authHeader) throws DomainException;

}
