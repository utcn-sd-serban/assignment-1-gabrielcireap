package com.gabrielcireap.stackOverflow.repository.memory;


import com.gabrielcireap.stackOverflow.entity.Vote;
import com.gabrielcireap.stackOverflow.repository.VoteRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryVoteRepository implements VoteRepository {

    private final Map<Integer, Vote> data = new ConcurrentHashMap<>();
    private final AtomicInteger currentId = new AtomicInteger(0);

    @Override
    public Vote save(Vote vote) {
        if(vote.getId() == null){
            vote.setId(currentId.incrementAndGet());
        }
        data.put(vote.getId(), vote);
        return vote;
    }

    @Override
    public void remove(Vote vote) {
        data.remove(vote.getId());
    }

    @Override
    public Optional<Vote> findById(int id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<Vote> findByAnswerId(int answerId, int userId) {
        for(Vote v : data.values()){
            if(v.getAnswer() != null){
                if(v.getAnswer().getId() == answerId && v.getUser().getId() == userId)
                    return Optional.ofNullable(v);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Vote> findByQuestionId(int questionId, int userId) {
        for(Vote v : data.values()){
            if(v.getQuestion() != null){
                if(v.getQuestion().getId() == questionId && v.getUser().getId() == userId){
                    return Optional.ofNullable(v);
                }
            }
        }
        return Optional.empty();
    }
}
