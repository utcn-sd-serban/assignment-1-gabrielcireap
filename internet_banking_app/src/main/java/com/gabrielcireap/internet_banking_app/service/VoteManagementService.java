package com.gabrielcireap.internet_banking_app.service;

import com.gabrielcireap.internet_banking_app.entity.Vote;
import com.gabrielcireap.internet_banking_app.exception.AnswerNotFoundException;
import com.gabrielcireap.internet_banking_app.exception.QuestionNotFoundException;
import com.gabrielcireap.internet_banking_app.exception.VoteNotFoundException;
import com.gabrielcireap.internet_banking_app.repository.RepositoryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class VoteManagementService {

    private final RepositoryFactory repositoryFactory;

    @Transactional
    public Vote save(Vote vote){
        return repositoryFactory.createVoteRepository().save(vote);
    }

    @Transactional
    public void remove(int id){
        Vote vote = repositoryFactory.createVoteRepository().findById(id).orElseThrow(VoteNotFoundException::new);
        repositoryFactory.createVoteRepository().remove(vote);
    }

    @Transactional
    public Vote findByAnswerId(int answerId, int userId) {
        return repositoryFactory.createVoteRepository().findByAnswerId(answerId, userId).orElseThrow(VoteNotFoundException::new);
    }

    @Transactional
    public Vote findByQuestionId(int questionId, int userId){
        return repositoryFactory.createVoteRepository().findByQuestionId(questionId, userId).orElseThrow(VoteNotFoundException::new);
    }
}
