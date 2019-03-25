package com.gabrielcireap.internet_banking_app.service;

import com.gabrielcireap.internet_banking_app.entity.Question;
import com.gabrielcireap.internet_banking_app.entity.Tag;
import com.gabrielcireap.internet_banking_app.exception.QuestionNotFoundException;
import com.gabrielcireap.internet_banking_app.repository.RepositoryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionManagementService {

    private final RepositoryFactory repositoryFactory;

    @Transactional
    public List<Question> listQuestions(){
        return repositoryFactory.createQuestionRepository().findAll();
    }

    @Transactional
    public Question findById(int id){
        return repositoryFactory.createQuestionRepository().findById(id).orElseThrow(QuestionNotFoundException::new);
    }

    @Transactional
    public Question save(Question question){
        return repositoryFactory.createQuestionRepository().save(question);
    }

    @Transactional
    public void remove(int questionId){
        Question question = repositoryFactory.createQuestionRepository().findById(questionId).orElseThrow(QuestionNotFoundException::new);
        repositoryFactory.createQuestionRepository().remove(question);
    }

    @Transactional
    public List<Question> findQuestionByTitle(String text){
        return repositoryFactory.createQuestionRepository().findByTitle(text);
    }

    @Transactional
    public List<Question> findQuestionByTag(String tag){
        return repositoryFactory.createQuestionRepository().findByTag(tag);
    }

    @Transactional
    public void addTag(Question question, Tag tag){ repositoryFactory.createQuestionRepository().addTag(question, tag);}
}
