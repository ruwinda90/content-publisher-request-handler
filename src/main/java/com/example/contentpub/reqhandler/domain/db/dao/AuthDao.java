package com.example.contentpub.reqhandler.domain.db.dao;

import com.example.contentpub.reqhandler.domain.constants.Role;
import com.example.contentpub.reqhandler.domain.db.entity.RefreshToken;
import com.example.contentpub.reqhandler.domain.db.entity.User;
import com.example.contentpub.reqhandler.domain.db.repo.RefreshTokenRepo;
import com.example.contentpub.reqhandler.domain.db.repo.UserRepo;
import com.example.contentpub.reqhandler.domain.dto.AuthRequestEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthDao {
    private final UserRepo userRepo;
    private final RefreshTokenRepo refreshTokenRepo;
    private final PasswordEncoder bcryptEncoder;

    public AuthDao(UserRepo userRepo, RefreshTokenRepo refreshTokenRepo, PasswordEncoder bcryptEncoder) {
        this.userRepo = userRepo;
        this.refreshTokenRepo = refreshTokenRepo;
        this.bcryptEncoder = bcryptEncoder;
    }

    public boolean userExistsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    public User findUserByEmail(String username) {

        return userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));
    }

    public void createUser(AuthRequestEntity user) {

        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        newUser.setRole(Role.USER_READER);  // Save with the default role.

        userRepo.save(newUser);
    }

    public Integer createRefreshToken(Integer userId, String token) {

        RefreshToken refreshToken;
        Optional<RefreshToken> entityOptional = refreshTokenRepo.findByUserId(userId);

        if (entityOptional.isPresent()) {
            refreshToken = entityOptional.get();
        } else {
            refreshToken = new RefreshToken();
            refreshToken.setUserId(userId);
        }

        refreshToken.setToken(token);
        return refreshTokenRepo.save(refreshToken).getId();
    }

    public Integer removeRefreshToken(Integer userId) {

        return refreshTokenRepo.deleteByUserId(userId);
    }

}
