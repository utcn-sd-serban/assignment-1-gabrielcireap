package com.gabrielcireap.internet_banking_app.service;

import com.gabrielcireap.internet_banking_app.entity.Answer;
import com.gabrielcireap.internet_banking_app.entity.Question;
import com.gabrielcireap.internet_banking_app.repository.RepositoryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerManagementService {

    private final RepositoryFactory repositoryFactory;

    @Transactional
    public Answer save(Answer answer){
        return repositoryFactory.createAnswerRepository().save(answer);
    }

    @Transactional
    public List<Answer> findByQuestion(Question question) {
        return repositoryFactory.createAnswerRepository().findByQuestion(question);
    }

    @Transactional
    public void remove(Answer answer){ repositoryFactory.createAnswerRepository().remove(answer); }

    @Transactional
    public Optional<Answer> findById(int id){
        return repositoryFactory.createAnswerRepository().findById(id);
    }
}
