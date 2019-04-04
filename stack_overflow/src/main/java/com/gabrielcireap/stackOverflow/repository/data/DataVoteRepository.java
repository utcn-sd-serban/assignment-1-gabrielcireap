package com.gabrielcireap.stackOverflow.repository.data;

import com.gabrielcireap.stackOverflow.entity.Vote;
import com.gabrielcireap.stackOverflow.repository.VoteRepository;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface DataVoteRepository extends Repository<Vote, Integer>, VoteRepository {

    void delete(Vote vote);
    @Override
    default void remove(Vote vote) {
        delete(vote);
    }

    Optional<Vote> findByAnswerIdAndUserId(int answerId, int userId);
    @Override
    default Optional<Vote> findByAnswerId(int answerId, int userId) {
        return findByAnswerIdAndUserId(answerId, userId);
    }

    Optional<Vote> findByQuestionIdAndUserId(int questionId, int userId);
    @Override
    default Optional<Vote> findByQuestionId(int questionId, int userId) {
        return findByQuestionIdAndUserId(questionId, userId);
    }
}
