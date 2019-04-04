package com.gabrielcireap.stackOverflow.service;

import com.gabrielcireap.stackOverflow.entity.Answer;
import com.gabrielcireap.stackOverflow.entity.Question;
import com.gabrielcireap.stackOverflow.entity.Vote;
import com.gabrielcireap.stackOverflow.exception.*;
import com.gabrielcireap.stackOverflow.repository.RepositoryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class VoteManagementService {

    private final RepositoryFactory repositoryFactory;
    private final UserManagementService userManagementService;
    private final AnswerManagementService answerManagementService;
    private final QuestionManagementService questionManagementService;

    @Transactional
    public Vote save(Vote vote){
        return repositoryFactory.createVoteRepository().save(vote);
    }

    @Transactional
    public void remove(int id){
        Vote vote = repositoryFactory.createVoteRepository().findById(id).orElseThrow(VoteNotFoundException::new);
        repositoryFactory.createVoteRepository().remove(vote);
    }

    @Transactional
    public Vote findByAnswerId(int answerId, int userId) {
        return repositoryFactory.createVoteRepository().findByAnswerId(answerId, userId).orElseThrow(VoteNotFoundException::new);
    }

    @Transactional
    public Vote findByQuestionId(int questionId, int userId){
        return repositoryFactory.createVoteRepository().findByQuestionId(questionId, userId).orElseThrow(VoteNotFoundException::new);
    }

    @Transactional
    public void upvoteAnswer(int answerId){
        userManagementService.checkIfUserIsLogged();
        Answer answer = answerManagementService.findById(answerId);
        if(answer.getUser().equals(userManagementService.getLoggedUser())){
            throw new VoteYourOwnException();
        }

        try{
            Vote vote = findByAnswerId(answerId, userManagementService.getLoggedUser().getId());
            if(vote.getIs_upvote()){
                throw new UpvoteDuplicateException();
            } else {
                vote.setIs_upvote(true);
                save(vote);

                answer.setVoteCount(answer.getVoteCount() + 2);
                answerManagementService.save(answer);

                answer.getUser().setScore(answer.getUser().getScore() + 12);
                userManagementService.save(answer.getUser());

                userManagementService.getLoggedUser().setScore(userManagementService.getLoggedUser().getScore() + 1);
                userManagementService.save(userManagementService.getLoggedUser());
            }

        } catch (VoteNotFoundException voteNotFoundException){
            save(new Vote(null, null, answer, userManagementService.getLoggedUser(), true));

            answer.setVoteCount(answer.getVoteCount() + 1);
            answerManagementService.save(answer);

            answer.getUser().setScore(answer.getUser().getScore() + 10);
            userManagementService.save(answer.getUser());
        }
    }

    @Transactional
    public void downvoteAnswer(int answerId){
        userManagementService.checkIfUserIsLogged();
        Answer answer = answerManagementService.findById(answerId);
        if(answer.getUser().equals(userManagementService.getLoggedUser())){
            throw new VoteYourOwnException();
        }

        try{
            Vote vote = findByAnswerId(answerId, userManagementService.getLoggedUser().getId());
            if(!vote.getIs_upvote()){
                throw new DownvoteDuplicateException();
            } else {
                vote.setIs_upvote(false);
                save(vote);

                answer.setVoteCount(answer.getVoteCount() - 2);
                answerManagementService.save(answer);

                answer.getUser().setScore(answer.getUser().getScore() - 12);
                userManagementService.save(answer.getUser());

                userManagementService.getLoggedUser().setScore(userManagementService.getLoggedUser().getScore() - 1);
                userManagementService.save(userManagementService.getLoggedUser());
            }

        } catch (VoteNotFoundException voteNotFoundException){
            save(new Vote(null, null, answer, userManagementService.getLoggedUser(), false));

            answer.setVoteCount(answer.getVoteCount() - 1);
            answerManagementService.save(answer);

            answer.getUser().setScore(answer.getUser().getScore() - 2);
            userManagementService.save(answer.getUser());

            userManagementService.getLoggedUser().setScore(userManagementService.getLoggedUser().getScore() - 1);
            userManagementService.save(userManagementService.getLoggedUser());
        }
    }

    @Transactional
    public void upvoteQuestion(int questionId){
        userManagementService.checkIfUserIsLogged();
        Question question = (Question) questionManagementService.findById(questionId).keySet().toArray()[0];
        if(question.getUser().equals(userManagementService.getLoggedUser())){
            throw new VoteYourOwnException();
        }

        try{
            Vote vote = findByQuestionId(questionId, userManagementService.getLoggedUser().getId());
            if(vote.getIs_upvote()){
                throw new UpvoteDuplicateException();
            } else {
                vote.setIs_upvote(true);
                save(vote);

                question.setVoteCount(question.getVoteCount() + 2);
                questionManagementService.save(question);

                question.getUser().setScore(question.getUser().getScore() + 7);
                userManagementService.save(question.getUser());
            }

        } catch (VoteNotFoundException voteNotFoundException){
            save(new Vote(null, question, null, userManagementService.getLoggedUser(), true));

            question.setVoteCount(question.getVoteCount() + 1);
            questionManagementService.save(question);

            question.getUser().setScore(question.getUser().getScore() + 5);
            userManagementService.save(question.getUser());
        }
    }

    @Transactional
    public void downvoteQuestion(int questionId){
        userManagementService.checkIfUserIsLogged();
        Question question = (Question) questionManagementService.findById(questionId).keySet().toArray()[0];
        if(question.getUser().equals(userManagementService.getLoggedUser())){
            throw new VoteYourOwnException();
        }

        try{
            Vote vote = findByQuestionId(questionId, userManagementService.getLoggedUser().getId());
            if(!vote.getIs_upvote()){
                throw new DownvoteDuplicateException();
            } else {
                vote.setIs_upvote(true);
                save(vote);

                question.setVoteCount(question.getVoteCount() - 2);
                questionManagementService.save(question);

                question.getUser().setScore(question.getUser().getScore() - 7);
                userManagementService.save(question.getUser());
            }

        } catch (VoteNotFoundException voteNotFoundException){
            save(new Vote(null, question, null, userManagementService.getLoggedUser(), false));

            question.setVoteCount(question.getVoteCount() - 1);
            questionManagementService.save(question);

            question.getUser().setScore(question.getUser().getScore() - 2);
            userManagementService.save(question.getUser());
        }
    }
}
