package com.gabrielcireap.stackOverflow.repository;

import com.gabrielcireap.stackOverflow.entity.Answer;
import com.gabrielcireap.stackOverflow.entity.Question;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository {

    Answer save(Answer answer);
    void remove(Answer answer);
    Optional<Answer> findById(int id);
    List<Answer> findByQuestion(Question question);
}
