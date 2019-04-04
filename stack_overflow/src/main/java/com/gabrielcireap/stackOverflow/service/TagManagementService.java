package com.gabrielcireap.stackOverflow.service;

import com.gabrielcireap.stackOverflow.entity.Tag;
import com.gabrielcireap.stackOverflow.repository.RepositoryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagManagementService {

    private final RepositoryFactory repositoryFactory;
    private final UserManagementService userManagementService;

    @Transactional
    public Tag save(Tag tag){
        return repositoryFactory.createTagRepository().save(tag);
    }

    @Transactional
    public List<Tag> listTags(){
        userManagementService.checkIfUserIsLogged();
        return repositoryFactory.createTagRepository().findAll();
    }
}
