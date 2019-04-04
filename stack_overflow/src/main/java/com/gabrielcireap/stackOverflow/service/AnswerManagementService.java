package com.gabrielcireap.stackOverflow.service;

import com.gabrielcireap.stackOverflow.entity.Answer;
import com.gabrielcireap.stackOverflow.entity.Question;
import com.gabrielcireap.stackOverflow.exception.AnswerNotFoundException;
import com.gabrielcireap.stackOverflow.exception.NotEnoughPermissionsException;
import com.gabrielcireap.stackOverflow.exception.QuestionNotFoundException;
import com.gabrielcireap.stackOverflow.repository.RepositoryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class AnswerManagementService {

    private final RepositoryFactory repositoryFactory;
    private final UserManagementService userManagementService;

    @Transactional
    public Answer save(Answer answer){
        return repositoryFactory.createAnswerRepository().save(answer);
    }

    @Transactional
    public Answer save(int questionId, String text){
        userManagementService.checkIfUserIsLogged();
        Question question = repositoryFactory.createQuestionRepository().findById(questionId).orElseThrow(QuestionNotFoundException::new);
        return repositoryFactory.createAnswerRepository()
                                .save(new Answer(
                                null, question, userManagementService.getLoggedUser(), text, new Timestamp(System.currentTimeMillis()), 0));
    }

    @Transactional
    public void remove(int id){
        userManagementService.checkIfUserIsLogged();
        Answer answer = findById(id);
        if(answer.getUser().equals(userManagementService.getLoggedUser()) || userManagementService.getLoggedUser().getIsAdmin()){
            repositoryFactory.createAnswerRepository().remove(answer);
        } else {
            throw new NotEnoughPermissionsException();
        }
    }

    @Transactional
    public void edit(int id, String text){
        userManagementService.checkIfUserIsLogged();
        Answer answer = findById(id);
        if(answer.getUser().equals(userManagementService.getLoggedUser()) || userManagementService.getLoggedUser().getIsAdmin()) {
            answer.setText(text);
            save(answer);
        } else {
            throw new NotEnoughPermissionsException();
        }
    }

    @Transactional
    public Answer findById(int id){
        return repositoryFactory.createAnswerRepository().findById(id).orElseThrow(AnswerNotFoundException::new);
    }
}
