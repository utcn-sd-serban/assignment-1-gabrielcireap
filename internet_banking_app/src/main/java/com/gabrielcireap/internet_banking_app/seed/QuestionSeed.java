package com.gabrielcireap.internet_banking_app.seed;

import com.gabrielcireap.internet_banking_app.entity.Question;
import com.gabrielcireap.internet_banking_app.entity.Tag;
import com.gabrielcireap.internet_banking_app.repository.QuestionRepository;
import com.gabrielcireap.internet_banking_app.repository.RepositoryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class QuestionSeed implements CommandLineRunner {

    private final RepositoryFactory repositoryFactory;

    @Override
    public void run(String... args) throws Exception {
        QuestionRepository questionRepository = repositoryFactory.createQuestionRepository();
        if(questionRepository.findAll().isEmpty()){
            questionRepository.save(new Question(null, repositoryFactory.createUserRepository().findById(1).get(),
                                                "Prima intrebare", "Acesta este primul text la prima intrebare",
                                                    new Timestamp(System.currentTimeMillis()), 0,
                                                    new ArrayList<Tag>()));

            questionRepository.save(new Question(null, repositoryFactory.createUserRepository().findById(2).get(),
                                                "Second question", "How to get a 10 at SD lab?",
                                                    new Timestamp(System.currentTimeMillis()), 0,
                                                    new ArrayList<Tag>()));

            questionRepository.save(new Question(null, repositoryFactory.createUserRepository().findById(1).get(),
                                                "Third question", "This is the third question",
                                                    new Timestamp(System.currentTimeMillis()), 0,
                                                    new ArrayList<Tag>()));
        }
    }
}
