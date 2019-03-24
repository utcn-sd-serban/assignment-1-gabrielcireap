package com.gabrielcireap.internet_banking_app.repository;

import com.gabrielcireap.internet_banking_app.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {

    List<Tag> findAll();
    Tag save(Tag tag);
    void remove(Tag tag);
    Optional<Tag> findById(int id);
}
