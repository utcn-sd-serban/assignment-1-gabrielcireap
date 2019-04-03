package com.gabrielcireap.stackOverflow.repository.data;

import com.gabrielcireap.stackOverflow.entity.Tag;
import com.gabrielcireap.stackOverflow.repository.TagRepository;
import org.springframework.data.repository.Repository;

public interface DataTagRepository extends Repository<Tag, Integer>, TagRepository {

    void delete(Tag tag);

    @Override
    default void remove(Tag tag) {
        delete(tag);
    }
}
