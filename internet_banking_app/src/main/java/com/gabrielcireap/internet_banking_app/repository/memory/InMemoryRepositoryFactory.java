package com.gabrielcireap.internet_banking_app.repository.memory;

import com.gabrielcireap.internet_banking_app.repository.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name="internet_banking_app.repository-type", havingValue = "MEMORY")
public class InMemoryRepositoryFactory implements RepositoryFactory {

    private final InMemoryAnswerRepository answerRepository = new InMemoryAnswerRepository();
    private final InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository();
    private final InMemoryUserRepository userRepository = new InMemoryUserRepository();
    private final InMemoryTagRepository tagRepository = new InMemoryTagRepository();
    private final InMemoryVoteRepository voteRepository = new InMemoryVoteRepository();

    @Override
    public UserRepository createUserRepository() {
        return userRepository;
    }

    @Override
    public QuestionRepository createQuestionRepository() {
        return questionRepository;
    }

    @Override
    public AnswerRepository createAnswerRepository() {
        return answerRepository;
    }

    @Override
    public TagRepository createTagRepository() { return tagRepository; }

    @Override
    public VoteRepository createVoteRepository() {
        return voteRepository;
    }
}
