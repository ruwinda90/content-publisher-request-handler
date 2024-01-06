package com.example.contentpub.reqhandler.domain.db.repo;

import com.example.contentpub.reqhandler.domain.db.entity.Writer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WriterRepo extends JpaRepository<Writer, Long> {

    @Query("SELECT w.id FROM Writer w where w.user.email =:username")
    Optional<Long> findByUserName(@Param("username") String userName);

}
