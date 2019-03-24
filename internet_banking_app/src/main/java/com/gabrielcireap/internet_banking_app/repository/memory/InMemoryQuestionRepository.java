package com.gabrielcireap.internet_banking_app.repository.memory;
import com.gabrielcireap.internet_banking_app.entity.Question;
import com.gabrielcireap.internet_banking_app.entity.Tag;
import com.gabrielcireap.internet_banking_app.repository.QuestionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryQuestionRepository implements QuestionRepository {

    private final Map<Integer, Question> data = new ConcurrentHashMap<>();
    private final AtomicInteger currentId = new AtomicInteger(0);

    @Override
    public Question save(Question question){
        if(question.getId() == null){
            question.setId(currentId.incrementAndGet());
        }
        data.put(question.getId(), question);
        return question;
    }

    @Override
    public void remove(Question question){
        data.remove(question.getId());
    }

    @Override
    public Optional<Question> findById(int id){
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Question> findAll(){
        return new ArrayList<>(data.values());
    }

    @Override
    public List<Question> findByTitle(String text) {
        return data.values().stream().filter(q -> q.getTitle().contains(text)).collect(Collectors.toList());
    }

    @Override
    public List<Question> findByTag(String tag) {
        List<Question> qs = new ArrayList<>();

        for(Question q : data.values()){
            for(Tag t : q.getTags()){
                if(t.getName().equals(tag)){
                    qs.add(q);
                }
            }
        }
        return qs;
    }

    @Override
    public void addTag(Question question, Tag tag) {
        data.get(question).getTags().add(tag);
    }
}