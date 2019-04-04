package com.gabrielcireap.stackOverflow.repository.data;

import com.gabrielcireap.stackOverflow.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "stack_overflow.repository-type", havingValue = "DATA")
public class DataRepositoryFactory implements RepositoryFactory {

    private final DataUserRepository userRepository;
    private final DataQuestionRepository questionRepository;
    private final DataAnswerRepository answerRepository;
    private final DataTagRepository tagRepository;
    private final DataVoteRepository voteRepository;

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
    public TagRepository createTagRepository() {
        return tagRepository;
    }

    @Override
    public VoteRepository createVoteRepository() {
        return voteRepository;
    }
}
