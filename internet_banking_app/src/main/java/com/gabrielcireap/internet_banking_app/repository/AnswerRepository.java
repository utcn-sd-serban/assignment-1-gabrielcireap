package com.gabrielcireap.internet_banking_app.repository;

import com.gabrielcireap.internet_banking_app.entity.Answer;
import com.gabrielcireap.internet_banking_app.entity.Question;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository {

    Answer save(Answer answer);
    void remove(Answer answer);
    Optional<Answer> findById(int id);
    List<Answer> findByQuestion(Question question);
}
