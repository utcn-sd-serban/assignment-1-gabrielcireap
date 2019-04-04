package com.gabrielcireap.stackOverflow.repository;

import com.gabrielcireap.stackOverflow.entity.Question;
import com.gabrielcireap.stackOverflow.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository {

    List<Question> findAll();
    Question save(Question question);
    void remove(Question question);
    Optional<Question> findById(int id);
    List<Question> findByTitle(String text);
    List<Question> findByTag(Tag tag);
}
