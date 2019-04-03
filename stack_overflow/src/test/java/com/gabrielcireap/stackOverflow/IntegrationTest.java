package com.gabrielcireap.stackOverflow;

import com.gabrielcireap.stackOverflow.entity.Answer;
import com.gabrielcireap.stackOverflow.entity.Question;
import com.gabrielcireap.stackOverflow.entity.User;
import com.gabrielcireap.stackOverflow.exception.UserNotFoundException;
import com.gabrielcireap.stackOverflow.repository.RepositoryFactory;
import com.gabrielcireap.stackOverflow.repository.memory.InMemoryRepositoryFactory;
import com.gabrielcireap.stackOverflow.service.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IntegrationTest {

    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private QuestionManagementService questionManagementService;

    @Autowired
    private AnswerManagementService answerManagementService;

    @Autowired
    private TagManagementService tagManagementService;

    @Autowired
    private VoteManagementService voteManagementService;

    private RepositoryFactory repositoryFactory = new InMemoryRepositoryFactory();

    private void createMockupData() {

        if (repositoryFactory.createUserRepository().findAll().isEmpty()) {
            repositoryFactory.createUserRepository().save(new User("user1", "pass1", "email1"));
            repositoryFactory.createUserRepository().save(new User("user2", "pass2", "email2"));
            repositoryFactory.createUserRepository().save(new User("user3", "pass3", "email3"));
            repositoryFactory.createUserRepository().save(new User("user4", "pass4", "email4"));
        }

        if (repositoryFactory.createQuestionRepository().findAll().isEmpty()) {
            repositoryFactory.createQuestionRepository().save(new Question("question 1", "text1", userManagementService.findById(1)));
            repositoryFactory.createQuestionRepository().save(new Question("question 2", "text2", userManagementService.findById(2)));
            repositoryFactory.createQuestionRepository().save(new Question("question 3", "text3", userManagementService.findById(3)));
            repositoryFactory.createQuestionRepository().save(new Question("question 4", "text4", userManagementService.findById(4)));
        }

        repositoryFactory.createAnswerRepository().save(new Answer("answer 1", (Question) questionManagementService.findById(1).keySet().toArray()[0], userManagementService.findById(1)));
        repositoryFactory.createAnswerRepository().save(new Answer("answer 2", (Question) questionManagementService.findById(2).keySet().toArray()[0], userManagementService.findById(2)));
        repositoryFactory.createAnswerRepository().save(new Answer("answer 3", (Question) questionManagementService.findById(3).keySet().toArray()[0], userManagementService.findById(3)));
        repositoryFactory.createAnswerRepository().save(new Answer("answer 4", (Question) questionManagementService.findById(4).keySet().toArray()[0], userManagementService.findById(4)));
    }

    @Test
    public void login() {
        createMockupData();
        User user = userManagementService.login("user1", "pass1");
        Assert.assertEquals("user1", user.getUsername());
        Assert.assertEquals("pass1", user.getPassword());
    }

    @Test(expected = UserNotFoundException.class)
    public void loginError() {
        createMockupData();
        User user = userManagementService.login("user2", "pass1");
    }
}
