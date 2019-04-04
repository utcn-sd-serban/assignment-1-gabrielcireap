package com.gabrielcireap.stackOverflow;

import com.gabrielcireap.stackOverflow.entity.Tag;
import com.gabrielcireap.stackOverflow.repository.RepositoryFactory;
import com.gabrielcireap.stackOverflow.repository.memory.InMemoryRepositoryFactory;

public class TagManagementServiceUnitTest {

    private static RepositoryFactory createMockedFactory(){
        RepositoryFactory repositoryFactory = new InMemoryRepositoryFactory();
        repositoryFactory.createTagRepository().save(new Tag(null, "tag1"));
        repositoryFactory.createTagRepository().save(new Tag(null, "tag2"));
        return repositoryFactory;
    }
}
