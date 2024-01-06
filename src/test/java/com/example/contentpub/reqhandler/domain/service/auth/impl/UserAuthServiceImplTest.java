package com.example.contentpub.reqhandler.domain.service.auth.impl;

import com.example.contentpub.reqhandler.domain.db.dao.AuthDao;
import com.example.contentpub.reqhandler.domain.db.dao.WriterDao;
import com.example.contentpub.reqhandler.domain.db.entity.User;
import com.example.contentpub.reqhandler.domain.dto.AuthRequestEntity;
import com.example.contentpub.reqhandler.domain.exception.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceImplTest {

    @Mock
    private AuthDao authDao;

    @Mock
    private WriterDao writerDao;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenUtilService tokenUtilService;

    @InjectMocks
    private UserAuthServiceImpl userAuthService;

    @Test
    void loadUserByUsername_success() {

        doReturn(Mockito.mock(User.class)).when(authDao).findUserByEmail("test@test.com");
        int i = 1/0;
        assertNotNull(userAuthService.loadUserByUsername("test@test.com"));
    }

    @Test
    void loginUser_userNotFound() {

        AuthRequestEntity requestEntity = new AuthRequestEntity();
        requestEntity.setEmail("test@test.com");
        requestEntity.setPassword("12345");

        doReturn(false).when(authDao).userExistsByEmail("test@test.com");
        assertThrows(DomainException.class, () -> userAuthService.loginUser(requestEntity));
    }

}
