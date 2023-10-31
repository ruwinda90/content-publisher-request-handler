package com.example.contentpub.reqhandler.domain.service.auth.impl;

import com.example.contentpub.reqhandler.domain.constants.StatusCode;
import com.example.contentpub.reqhandler.domain.db.dao.AuthDao;
import com.example.contentpub.reqhandler.domain.db.dao.WriterDao;
import com.example.contentpub.reqhandler.domain.db.entity.User;
import com.example.contentpub.reqhandler.domain.dto.*;
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

import java.util.List;
import java.util.stream.Collectors;

import static com.example.contentpub.reqhandler.domain.constants.StatusCode.*;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    @Autowired
    private AuthDao authDao;

    @Autowired
    private WriterDao writerDao;

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
    public CommonResponseEntity<AuthResponseEntity> loginUser(AuthRequestEntity authRequestEntity) throws DomainException {

        CommonResponseEntity<AuthResponseEntity> domainResponse = new CommonResponseEntity<>();

        if (!authDao.userExistsByEmail(authRequestEntity.getEmail())) {
            throw new DomainException(EMAIL_NOT_FOUND);
        }

        Authentication authentication;
        List<String> roles;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestEntity.getEmail(), authRequestEntity.getPassword()));
            roles = authentication.getAuthorities().stream().map(Object::toString).collect(Collectors.toList());
        } catch (BadCredentialsException ex) {
            throw new DomainException(INVALID_CREDENTIALS);
        }

        Long writerId = writerDao.findWriterId(authRequestEntity.getEmail());

        String accessToken = tokenUtilService.generateAccessToken(authentication.getName(), ((User) authentication.getPrincipal()).getId(), authentication.getAuthorities()).getToken();

        TokenData refreshTokenData = tokenUtilService.generateRefreshToken(authentication.getName(), ((User) authentication.getPrincipal()).getId());
        String refreshToken = refreshTokenData.getToken();

        authDao.createRefreshToken(refreshTokenData.getUserId(), refreshToken);

        domainResponse.setHttpStatusCode(StatusCode.SUCCESS.getHttpStatus().value());
        domainResponse.setCode(StatusCode.SUCCESS.getCode());
        domainResponse.setDescription(StatusCode.SUCCESS.getDescription());

        AuthResponseEntity authResponseEntity = new AuthResponseEntity();
        authResponseEntity.setAccessToken(accessToken);
        authResponseEntity.setRefreshToken(refreshToken);
        authResponseEntity.setWriterId(writerId);
        authResponseEntity.getRoles().addAll(roles);
        domainResponse.setData(authResponseEntity);

        return domainResponse;
    }

    @Override
    public CommonResponseEntity<AuthResponseEntity> refresh(String refreshToken) throws DomainException {

        // is the token valid
        // - not tampered, user ID matches
        // is token expired
        // create access token

        if (refreshToken == null) {
            throw new DomainException(REFRESH_COOKIE_NOT_FOUND);
        }

        CommonResponseEntity<AuthResponseEntity> domainResponse = new CommonResponseEntity<>();

        String userName = tokenUtilService.validateRefreshToken(refreshToken);
        User userData = authDao.findUserByEmail(userName);
        String accessToken = tokenUtilService.generateAccessToken(userData.getUsername(), userData.getId(), userData.getAuthorities()).getToken();

        domainResponse.setHttpStatusCode(StatusCode.SUCCESS.getHttpStatus().value());
        domainResponse.setCode(StatusCode.SUCCESS.getCode());
        domainResponse.setDescription(StatusCode.SUCCESS.getDescription());

        AuthResponseEntity authResponseEntity = new AuthResponseEntity();
        authResponseEntity.setAccessToken(accessToken);
        domainResponse.setData(authResponseEntity);

        return domainResponse;
    }

    @Override
    public CommonResponseEntity<String> createUser(AuthRequestEntity userRegRequestEntity) throws DomainException {

        CommonResponseEntity<String> domainResponse = new CommonResponseEntity<>();

        if (authDao.userExistsByEmail(userRegRequestEntity.getEmail())) {
            throw new DomainException(EMAIL_ALREADY_IN_USE);
        }

        authDao.createUser(userRegRequestEntity);

        domainResponse.setHttpStatusCode(StatusCode.CREATED.getHttpStatus().value());
        domainResponse.setCode(StatusCode.CREATED.getCode());
        domainResponse.setDescription(StatusCode.CREATED.getDescription());

        return domainResponse;
    }

    @Override
    public CommonResponseEntity<String> logoutUser(Integer userId, String authHeader) throws DomainException {

        CommonResponseEntity<String> domainResponse = new CommonResponseEntity<>();

        Integer userIdInHeader = tokenUtilService.getUserIdFromAccessToken(authHeader.substring(7));

        if (!userId.equals(userIdInHeader)) {
            throw new DomainException(USER_ID_MISMATCH);
        }

        int deletedRowCount = authDao.removeRefreshToken(userId);
        if (deletedRowCount == 0) {
            throw new DomainException(REFRESH_TOKEN_NOT_FOUND);
        }

        domainResponse.setHttpStatusCode(StatusCode.SUCCESS.getHttpStatus().value());
        domainResponse.setCode(StatusCode.SUCCESS.getCode());
        domainResponse.setDescription(StatusCode.SUCCESS.getDescription());

        return domainResponse;
    }
}
