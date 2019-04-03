package com.gabrielcireap.stackOverflow.repository;

public interface RepositoryFactory {
    UserRepository createUserRepository();
    QuestionRepository createQuestionRepository();
    AnswerRepository createAnswerRepository();
    TagRepository createTagRepository();
    VoteRepository createVoteRepository();
}
