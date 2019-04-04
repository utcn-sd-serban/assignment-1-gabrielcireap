package com.gabrielcireap.stackOverflow.repository.data;

import com.gabrielcireap.stackOverflow.entity.Answer;
import com.gabrielcireap.stackOverflow.repository.AnswerRepository;
import org.springframework.data.repository.Repository;

public interface DataAnswerRepository extends Repository<Answer, Integer>, AnswerRepository {

    void delete(Answer answer);
    @Override
    default void remove(Answer answer) {
        delete(answer);
    }
}
