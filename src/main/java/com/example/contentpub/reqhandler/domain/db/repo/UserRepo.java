package com.example.contentpub.reqhandler.domain.db.repo;

import com.example.contentpub.reqhandler.domain.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String username);

    boolean existsByEmail(String email);

}
