package com.gabrielcireap.internet_banking_app.repository;

import com.gabrielcireap.internet_banking_app.entity.Vote;

import java.util.Optional;

public interface VoteRepository {

    Vote save(Vote vote);
    void remove(Vote vote);
    Optional<Vote> findById(int id);
    Optional<Vote> findByAnswerId(int answerId, int userId);
    Optional<Vote> findByQuestionId(int questionId, int userId);
}
