package com.example.contentpub.reqhandler.domain.db.dao;

import com.example.contentpub.reqhandler.domain.db.repo.WriterRepo;
import org.springframework.stereotype.Service;

@Service
public class WriterDao {

    private final WriterRepo writerRepo;

    public WriterDao(WriterRepo writerRepo) {
        this.writerRepo = writerRepo;
    }

    public Long findWriterId(String userName) {

        return writerRepo.findByUserName(userName).orElse(null);
    }

}
