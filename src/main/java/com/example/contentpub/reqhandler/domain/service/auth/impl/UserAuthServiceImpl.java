package com.example.contentpub.reqhandler.domain.service.auth.impl;

import com.example.contentpub.reqhandler.domain.constants.StatusCodes;
import com.example.contentpub.reqhandler.domain.db.dao.UserDao;
import com.example.contentpub.reqhandler.domain.dto.AuthRequestEntity;
import com.example.contentpub.reqhandler.domain.dto.AuthResponseEntity;
import com.example.contentpub.reqhandler.domain.dto.CommonResponseEntity2;
import com.example.contentpub.reqhandler.domain.exception.DomainException;
import com.example.contentpub.reqhandler.domain.service.auth.TokenProvider;
import com.example.contentpub.reqhandler.domain.service.auth.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.example.contentpub.reqhandler.domain.constants.StatusCodes.EMAIL_ALREADY_IN_USE;
import static com.example.contentpub.reqhandler.domain.constants.StatusCodes.EMAIL_NOT_FOUND;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    @Autowired
    private UserDao userDao;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider tokenProvider;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userDao.findByEmail(username);

    }

    @Override
    public CommonResponseEntity2<AuthResponseEntity> createToken(AuthRequestEntity authRequestEntity) {

        CommonResponseEntity2<AuthResponseEntity> domainResponse = new CommonResponseEntity2<>();

        try {
            if (!userDao.existsByEmail(authRequestEntity.getEmail())) {
                throw new DomainException(EMAIL_NOT_FOUND);
            }

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestEntity.getEmail(), authRequestEntity.getPassword()));
            String token = tokenProvider.generateToken(authentication);

            domainResponse.setHttpStatusCode(StatusCodes.SUCCESS.getHttpStatus().value());
            domainResponse.setCode(StatusCodes.SUCCESS.getCode());
            domainResponse.setDescription(StatusCodes.SUCCESS.getDescription());
            domainResponse.setData(new AuthResponseEntity(token));

        } catch (DomainException ex) {
            domainResponse.setHttpStatusCode(ex.getHttpStatus().value());
            domainResponse.setCode(ex.getCode());
            domainResponse.setDescription(ex.getMessage());
        }

        return domainResponse;
    }

    @Override
    public CommonResponseEntity2<String> createUser(AuthRequestEntity userRegRequestEntity) {

        CommonResponseEntity2<String> domainResponse = new CommonResponseEntity2<>();

        try {
            if (userDao.existsByEmail(userRegRequestEntity.getEmail())) {
                throw new DomainException(EMAIL_ALREADY_IN_USE);
            }

            userDao.createUser(userRegRequestEntity);

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
