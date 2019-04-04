package com.gabrielcireap.stackOverflow.repository.data;

import com.gabrielcireap.stackOverflow.entity.Question;
import com.gabrielcireap.stackOverflow.entity.Tag;
import com.gabrielcireap.stackOverflow.repository.QuestionRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface DataQuestionRepository extends Repository<Question, Integer>, QuestionRepository {

    void delete(Question question);
    @Override
    default void remove(Question question) {
        delete(question);
    }

    List<Question> findByTitleContaining(String title);
    @Override
    default List<Question> findByTitle(String text) {
        return findByTitleContaining(text);
    }

    List<Question> findByTagsContaining(Tag tag);
    @Override
    default List<Question> findByTag(Tag tag) {
        return findByTagsContaining(tag);
    }
}
