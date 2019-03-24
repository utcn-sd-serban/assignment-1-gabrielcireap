package com.gabrielcireap.internet_banking_app.service;

import com.gabrielcireap.internet_banking_app.entity.Tag;
import com.gabrielcireap.internet_banking_app.repository.RepositoryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagManagementService {

    private final RepositoryFactory repositoryFactory;

    @Transactional
    public Tag save(Tag tag){
        return repositoryFactory.createTagRepository().save(tag);
    }

    @Transactional
    public List<Tag> listTags(){
        return repositoryFactory.createTagRepository().findAll();
    }
}
