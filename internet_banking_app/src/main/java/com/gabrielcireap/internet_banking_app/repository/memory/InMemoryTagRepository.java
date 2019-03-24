package com.gabrielcireap.internet_banking_app.repository.memory;

import com.gabrielcireap.internet_banking_app.entity.Tag;
import com.gabrielcireap.internet_banking_app.repository.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryTagRepository implements TagRepository {
    private final Map<Integer, Tag> data = new ConcurrentHashMap<>();
    private final AtomicInteger currentId = new AtomicInteger(0);

    @Override
    public List<Tag> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Tag save(Tag tag) {

        if(!data.values().stream().map(tag1 -> tag1.getName()).collect(Collectors.toList()).contains(tag.getName())){
            if(tag.getId() == null){
                tag.setId(currentId.incrementAndGet());
            }
            data.put(tag.getId(), tag);
        }

        return tag;
    }

    @Override
    public void remove(Tag tag) { data.remove(tag.getId()); }

    @Override
    public Optional<Tag> findById(int id) {
        return Optional.ofNullable(data.get(id));
    }
}
