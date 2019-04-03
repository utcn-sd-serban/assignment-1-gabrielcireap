package com.gabrielcireap.stackOverflow;

import com.gabrielcireap.stackOverflow.entity.User;
import com.gabrielcireap.stackOverflow.exception.UserNotFoundException;
import com.gabrielcireap.stackOverflow.repository.RepositoryFactory;
import com.gabrielcireap.stackOverflow.repository.memory.InMemoryRepositoryFactory;
import com.gabrielcireap.stackOverflow.service.UserManagementService;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

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
        User user = userManagementService.login("user1", "pass1");
        Assert.assertEquals(new User(1, "user1", "pass1", "email1", 0, false, false), user);
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
