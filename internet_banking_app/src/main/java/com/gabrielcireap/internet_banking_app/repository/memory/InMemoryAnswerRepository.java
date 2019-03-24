package com.gabrielcireap.internet_banking_app.repository.memory;

import com.gabrielcireap.internet_banking_app.entity.Answer;
import com.gabrielcireap.internet_banking_app.entity.Question;
import com.gabrielcireap.internet_banking_app.repository.AnswerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryAnswerRepository implements AnswerRepository {

    private final Map<Integer, Answer> data = new ConcurrentHashMap<>();
    private final AtomicInteger currentId = new AtomicInteger(0);

    @Override
    public Answer save(Answer answer){
        if(answer.getId() == null){
            answer.setId(currentId.incrementAndGet());
        }
        data.put(answer.getId(), answer);
        return answer;
    }

    @Override
    public void remove(Answer answer){
        data.remove(answer.getId());
    }

    @Override
    public Optional<Answer> findById(int id){
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Answer> findByQuestion(Question question) {
        return data.values().stream().filter(answer -> answer.getQuestion().equals(question)).collect(Collectors.toList());
    }
}