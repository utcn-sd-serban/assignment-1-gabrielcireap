package com.gabrielcireap.stackOverflow.service;

import com.gabrielcireap.stackOverflow.entity.Answer;
import com.gabrielcireap.stackOverflow.entity.Question;
import com.gabrielcireap.stackOverflow.entity.Tag;
import com.gabrielcireap.stackOverflow.exception.NotEnoughPermissionsException;
import com.gabrielcireap.stackOverflow.exception.QuestionNotFoundException;
import com.gabrielcireap.stackOverflow.exception.TagNotFoundException;
import com.gabrielcireap.stackOverflow.repository.RepositoryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
public class QuestionManagementService {

    private final RepositoryFactory repositoryFactory;
    private final UserManagementService userManagementService;

    @Transactional
    public List<Question> listQuestions(){
        userManagementService.checkIfUserIsLogged();
        List<Question> questions = repositoryFactory.createQuestionRepository().findAll();
        questions.sort((q1,q2) ->  q1.getCreationDate().after(q2.getCreationDate()) ? -1 : 1);
        return questions;
    }

    @Transactional
    public Map<Question, List<Answer>> findById(int id){
        userManagementService.checkIfUserIsLogged();
        Question question = repositoryFactory.createQuestionRepository().findById(id).orElseThrow(QuestionNotFoundException::new);
        List<Answer> answers = repositoryFactory.createAnswerRepository().findByQuestion(question);
        answers.sort((a1, a2) -> a1.getVoteCount() > a2.getVoteCount() ? -1 : 1);
        Map<Question, List<Answer>> map = new HashMap<>();
        map.put(question, answers);
        return map;
    }

    @Transactional
    public Question save(String title, String text, String tagList){
        userManagementService.checkIfUserIsLogged();
        List<Tag> tags = stringToTags(tagList);

        List<Tag> allTags = repositoryFactory.createTagRepository().findAll();
        tags.stream().filter(tag -> !allTags.contains(tag)).forEach(tag -> repositoryFactory.createTagRepository().save(tag));

        return repositoryFactory.createQuestionRepository()
                                .save(  new Question(null, userManagementService.getLoggedUser(),
                                        title, text, new Timestamp(System.currentTimeMillis()), 0, tags));
    }

    @Transactional
    public Question save(Question question){
        return repositoryFactory.createQuestionRepository().save(question);
    }

    @Transactional
    public void remove(int questionId){

        userManagementService.checkIfUserIsLogged();
        if(!userManagementService.getLoggedUser().getIsAdmin()){
            throw new NotEnoughPermissionsException();
        }

        Question question = repositoryFactory.createQuestionRepository().findById(questionId).orElseThrow(QuestionNotFoundException::new);
        repositoryFactory.createQuestionRepository().remove(question);
    }

    @Transactional
    public void edit(int questionId, String title, String text){
        userManagementService.checkIfUserIsLogged();
        if(!userManagementService.getLoggedUser().getIsAdmin()){
            throw new NotEnoughPermissionsException();
        }

        Question question = (Question) findById(questionId).keySet().toArray()[0];
        question.setTitle(title);
        question.setText(text);
        save(question);
    }

    @Transactional
    public List<Question> findQuestionByTitle(String text){

        userManagementService.checkIfUserIsLogged();
        text = text.split("\n")[0];     //get rid of the \n read by scanner
        List<Question> questions = repositoryFactory.createQuestionRepository().findByTitle(text);
        questions.sort((q1, q2) -> {
            return q1.getCreationDate().after(q2.getCreationDate()) ? -1 : 1;
        });
        return questions;
    }

    @Transactional
    public List<Question> findQuestionByTag(String tag){

        userManagementService.checkIfUserIsLogged();
        tag = tag.split("\n")[0];
        Tag t = repositoryFactory.createTagRepository().findByName(tag).orElseThrow(TagNotFoundException::new);
        List<Question> questions = repositoryFactory.createQuestionRepository().findByTag(t);
        questions.sort((q1, q2) -> {
            return q1.getCreationDate().after(q2.getCreationDate()) ? -1 : 1;
        });
        return questions;
    }

    private List<Tag> stringToTags(String tag){
        List<Tag> tags = new ArrayList<Tag>();
        String[] splitTags = tag.split(",");

        for(int i=0; i< splitTags.length; i++){
            tags.add(new Tag(null, splitTags[i].toLowerCase().trim()));
        }
        return tags;
    }
}
