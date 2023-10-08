package com.example.contentpub.reqhandler.domain.service.auth.impl;

import com.example.contentpub.reqhandler.domain.constants.StatusCodes;
import com.example.contentpub.reqhandler.domain.db.dao.AuthDao;
import com.example.contentpub.reqhandler.domain.db.entity.User;
import com.example.contentpub.reqhandler.domain.dto.AuthRequestEntity;
import com.example.contentpub.reqhandler.domain.dto.AuthResponseEntity;
import com.example.contentpub.reqhandler.domain.dto.CommonResponseEntity2;
import com.example.contentpub.reqhandler.domain.dto.TokenData;
import com.example.contentpub.reqhandler.domain.exception.DomainException;
import com.example.contentpub.reqhandler.domain.service.auth.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.example.contentpub.reqhandler.domain.constants.StatusCodes.*;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    @Autowired
    private AuthDao authDao;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenUtilService tokenUtilService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return authDao.findUserByEmail(username);
    }

    @Override
    public CommonResponseEntity2<AuthResponseEntity> loginUser(AuthRequestEntity authRequestEntity) throws DomainException {

        CommonResponseEntity2<AuthResponseEntity> domainResponse = new CommonResponseEntity2<>();

        if (!authDao.userExistsByEmail(authRequestEntity.getEmail())) {
            throw new DomainException(EMAIL_NOT_FOUND);
        }

        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authRequestEntity.getEmail(),
                            authRequestEntity.getPassword()));
        } catch (BadCredentialsException ex) {
            throw new DomainException(INVALID_CREDENTIALS);
        }

        String accessToken = tokenUtilService.generateAccessToken(authentication.getName(),
                ((User) authentication.getPrincipal()).getId(), authentication.getAuthorities()).getToken();

        TokenData refreshTokenData = tokenUtilService.generateRefreshToken(authentication.getName(),
                ((User) authentication.getPrincipal()).getId());
        String refreshToken = refreshTokenData.getToken();

        authDao.createRefreshToken(refreshTokenData.getUserId(), refreshToken);

        domainResponse.setHttpStatusCode(StatusCodes.SUCCESS.getHttpStatus().value());
        domainResponse.setCode(StatusCodes.SUCCESS.getCode());
        domainResponse.setDescription(StatusCodes.SUCCESS.getDescription());

        AuthResponseEntity authResponseEntity = new AuthResponseEntity();
        authResponseEntity.setAccessToken(accessToken);
        authResponseEntity.setRefreshToken(refreshToken);
        domainResponse.setData(authResponseEntity);

        return domainResponse;
    }

    @Override
    public CommonResponseEntity2<AuthResponseEntity> refresh(String refreshToken) throws DomainException {

        // is the token valid
        // - not tampered, user ID matches
        // is token expired
        // create access token

        if (refreshToken == null) {
            throw new DomainException(REFRESH_COOKIE_NOT_FOUND);
        }

        CommonResponseEntity2<AuthResponseEntity> domainResponse = new CommonResponseEntity2<>();

        String userName = tokenUtilService.validateRefreshToken(refreshToken);
        User userData = authDao.findUserByEmail(userName);
        String accessToken = tokenUtilService.generateAccessToken(userData.getUsername(), userData.getId(),
                userData.getAuthorities()).getToken();

        domainResponse.setHttpStatusCode(StatusCodes.SUCCESS.getHttpStatus().value());
        domainResponse.setCode(StatusCodes.SUCCESS.getCode());
        domainResponse.setDescription(StatusCodes.SUCCESS.getDescription());

        AuthResponseEntity authResponseEntity = new AuthResponseEntity();
        authResponseEntity.setAccessToken(accessToken);
        domainResponse.setData(authResponseEntity);

        return domainResponse;
    }

    @Override
    public CommonResponseEntity2<String> createUser(AuthRequestEntity userRegRequestEntity) {

        CommonResponseEntity2<String> domainResponse = new CommonResponseEntity2<>();

        try {
            if (authDao.userExistsByEmail(userRegRequestEntity.getEmail())) {
                throw new DomainException(EMAIL_ALREADY_IN_USE);
            }

            authDao.createUser(userRegRequestEntity);

            domainResponse.setHttpStatusCode(StatusCodes.CREATED.getHttpStatus().value());
            domainResponse.setCode(StatusCodes.CREATED.getCode());
            domainResponse.setDescription(StatusCodes.CREATED.getDescription());

        } catch (DomainException ex) {
            domainResponse.setHttpStatusCode(ex.getHttpStatus().value());
            domainResponse.setCode(ex.getCode());
            domainResponse.setDescription(ex.getMessage());
        } catch (Exception ex) {
            domainResponse.setHttpStatusCode(StatusCodes.INTERNAL_ERROR.getHttpStatus().value());
            domainResponse.setCode(StatusCodes.INTERNAL_ERROR.getCode());
            domainResponse.setDescription(ex.getMessage());
        }

        return domainResponse;
    }
}
