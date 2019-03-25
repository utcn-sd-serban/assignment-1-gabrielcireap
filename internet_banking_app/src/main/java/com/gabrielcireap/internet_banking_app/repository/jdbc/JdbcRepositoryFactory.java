package com.gabrielcireap.internet_banking_app.repository.jdbc;

import com.gabrielcireap.internet_banking_app.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@ConditionalOnProperty(name="internet_banking_app.repository-type", havingValue = "JDBC")
public class JdbcRepositoryFactory implements RepositoryFactory {

    private final JdbcTemplate template;

    @Override
    public UserRepository createUserRepository() {
        return new JdbcUserRepository(template);
    }

    @Override
    public QuestionRepository createQuestionRepository() {
        return new JdbcQuestionRepository(template);
    }

    @Override
    public AnswerRepository createAnswerRepository() {
        return new JdbcAnswerRepository(template);
    }

    @Override
    public TagRepository createTagRepository() {
        return new JdbcTagRepository(template);
    }

    @Override
    public VoteRepository createVoteRepository() {
        return new JdbcVoteRepository(template);
    }
}
