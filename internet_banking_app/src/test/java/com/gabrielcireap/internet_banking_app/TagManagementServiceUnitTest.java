package com.gabrielcireap.internet_banking_app;

import com.gabrielcireap.internet_banking_app.entity.Tag;
import com.gabrielcireap.internet_banking_app.repository.RepositoryFactory;
import com.gabrielcireap.internet_banking_app.repository.memory.InMemoryRepositoryFactory;
import com.gabrielcireap.internet_banking_app.service.TagManagementService;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class TagManagementServiceUnitTest {

    private static RepositoryFactory createMockedFactory(){
        RepositoryFactory repositoryFactory = new InMemoryRepositoryFactory();
        repositoryFactory.createTagRepository().save(new Tag(null, "tag1"));
        repositoryFactory.createTagRepository().save(new Tag(null, "tag2"));
        return repositoryFactory;
    }

    @Test
    public void testListUsers(){
        RepositoryFactory repositoryFactory = createMockedFactory();
        TagManagementService tagManagementService = new TagManagementService(repositoryFactory);
        List<Tag> tags = tagManagementService.listTags();
        Assert.assertEquals(new Tag(1, "tag1"), tags.get(0));
        Assert.assertEquals(new Tag(2, "tag2"), tags.get(1));
    }
}
