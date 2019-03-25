package com.gabrielcireap.internet_banking_app.repository;

public interface RepositoryFactory {
    UserRepository createUserRepository();
    QuestionRepository createQuestionRepository();
    AnswerRepository createAnswerRepository();
    TagRepository createTagRepository();
    VoteRepository createVoteRepository();
}
