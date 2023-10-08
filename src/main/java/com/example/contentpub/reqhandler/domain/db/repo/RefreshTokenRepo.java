package com.example.contentpub.reqhandler.domain.db.repo;

import com.example.contentpub.reqhandler.domain.db.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByUserId(Integer userId);

    @Query("SELECT rt.token FROM RefreshToken rt WHERE rt.userId=:userId")
    Optional<String> findTokenByUserId(Integer userId);

}
