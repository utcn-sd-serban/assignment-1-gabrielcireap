package com.gabrielcireap.internet_banking_app;

import com.gabrielcireap.internet_banking_app.entity.User;
import com.gabrielcireap.internet_banking_app.exception.UserNotFoundException;
import com.gabrielcireap.internet_banking_app.repository.RepositoryFactory;
import com.gabrielcireap.internet_banking_app.repository.memory.InMemoryRepositoryFactory;
import com.gabrielcireap.internet_banking_app.service.UserManagementService;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class UserManagementServiceUnitTest {

    private static RepositoryFactory createMockedFactory(){
        RepositoryFactory repositoryFactory = new InMemoryRepositoryFactory();
        repositoryFactory.createUserRepository().save(new User("user1", "pass1", "email1"));
        repositoryFactory.createUserRepository().save(new User("user2", "pass2", "email2"));
        return repositoryFactory;
    }

    @Test
    public void testListUsers(){
        RepositoryFactory repositoryFactory = createMockedFactory();
        UserManagementService userManagementService = new UserManagementService(repositoryFactory);
        List<User> users = userManagementService.listUsers();

        Assert.assertEquals(new User(1, "user1", "pass1", "email1", 0, false, false), users.get(0));
        Assert.assertEquals(new User(2, "user2", "pass2", "email2", 0, false, false), users.get(1));
    }

    @Test
    public void testGetUserByLogin(){
        RepositoryFactory repositoryFactory = createMockedFactory();
        UserManagementService userManagementService = new UserManagementService(repositoryFactory);
        Optional<User> user = userManagementService.getUserByLogin("user1", "pass1");
        Assert.assertEquals(new User(1, "user1", "pass1", "email1", 0, false, false), user.get());
    }

    @Test
    public void testRemoveUser(){
        RepositoryFactory repositoryFactory = createMockedFactory();
        UserManagementService userManagementService = new UserManagementService(repositoryFactory);
        userManagementService.remove(1);
        Assert.assertEquals(1, userManagementService.listUsers().size());
    }

    @Test(expected = UserNotFoundException.class)
    public void testRemoveUserError(){
        RepositoryFactory repositoryFactory = createMockedFactory();
        UserManagementService userManagementService = new UserManagementService(repositoryFactory);
        userManagementService.remove(10);
        Assert.assertEquals(1, userManagementService.listUsers().size());
    }
}
