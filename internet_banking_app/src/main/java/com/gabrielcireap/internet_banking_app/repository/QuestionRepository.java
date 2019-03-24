package com.gabrielcireap.internet_banking_app.repository;

import com.gabrielcireap.internet_banking_app.entity.Question;
import com.gabrielcireap.internet_banking_app.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository {

    List<Question> findAll();
    Question save(Question question);
    void remove(Question question);
    Optional<Question> findById(int id);
    List<Question> findByTitle(String text);
    List<Question> findByTag(String tag);
    void addTag(Question question, Tag tag);
}
