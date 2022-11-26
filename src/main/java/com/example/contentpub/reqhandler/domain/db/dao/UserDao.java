package com.example.contentpub.reqhandler.domain.db.dao;

import com.example.contentpub.reqhandler.domain.constants.Role;
import com.example.contentpub.reqhandler.domain.db.entity.User;
import com.example.contentpub.reqhandler.domain.db.repo.UserRepo;
import com.example.contentpub.reqhandler.domain.dto.AuthRequestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDao {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder bcryptEncoder;


    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    public User findByEmail(String username) {

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

}
