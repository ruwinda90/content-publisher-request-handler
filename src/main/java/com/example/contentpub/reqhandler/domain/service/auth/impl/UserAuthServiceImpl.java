package com.example.contentpub.reqhandler.domain.service.auth.impl;

import com.example.contentpub.reqhandler.domain.db.dao.UserDao;
import com.example.contentpub.reqhandler.domain.dto.AuthRequestEntity;
import com.example.contentpub.reqhandler.domain.dto.AuthResponseEntity;
import com.example.contentpub.reqhandler.domain.exception.DomainException;
import com.example.contentpub.reqhandler.domain.service.auth.TokenProvider;
import com.example.contentpub.reqhandler.domain.service.auth.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.example.contentpub.reqhandler.domain.constants.ErrorCode.EMAIL_ALREADY_IN_USE;
import static com.example.contentpub.reqhandler.domain.constants.CommonConstants.FAILURE;
import static com.example.contentpub.reqhandler.domain.constants.CommonConstants.SUCCESS;

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
    public AuthResponseEntity createToken(AuthRequestEntity authRequestEntity) {

        AuthResponseEntity domainResponse = new AuthResponseEntity();

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequestEntity.getEmail(),
                                                                      authRequestEntity.getPassword()));

        String token = tokenProvider.generateToken(authentication);

        domainResponse.setDescription(token);
        domainResponse.setStatusCode(HttpStatus.OK.value());
        domainResponse.setStatus(SUCCESS);

        return domainResponse;
    }

    @Override
    public AuthResponseEntity createUser(AuthRequestEntity userRegRequestEntity) {

        AuthResponseEntity domainResponse = new AuthResponseEntity();

        try {
            if (userDao.existsByEmail(userRegRequestEntity.getEmail())) {
                throw new DomainException(EMAIL_ALREADY_IN_USE);
            }

            userDao.createUser(userRegRequestEntity);

            domainResponse.setStatusCode(HttpStatus.CREATED.value());
            domainResponse.setStatus(SUCCESS);

        } catch (DomainException ex) {
            domainResponse.setStatusCode(ex.getHttpStatusCode());
            domainResponse.setStatus(FAILURE);
            domainResponse.setDescription(ex.getMessage());
        } catch (Exception ex) {
            domainResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            domainResponse.setStatus(FAILURE);
        }

        return domainResponse;
    }
}
