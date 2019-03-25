package com.gabrielcireap.internet_banking_app.controller;

import com.gabrielcireap.internet_banking_app.entity.*;
import com.gabrielcireap.internet_banking_app.exception.*;
import com.gabrielcireap.internet_banking_app.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
@RequiredArgsConstructor
public class ConsoleController implements CommandLineRunner {

    private final UserManagementService userManagementService;
    private final QuestionManagementService questionManagementService;
    private final TagManagementService tagManagementService;
    private final AnswerManagementService answerManagementService;
    private final VoteManagementService voteManagementService;
    private final Scanner scanner = new Scanner(System.in);
    private User currentUser;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Welcome to Stack Overflow");
        boolean done = false;
        while(!done){
            System.out.print("Enter a command: ");
            String command = scanner.next().trim();
            try{
                done = handleCommand(command);
            }catch (UserNotFoundException userNotFoundException){
                System.out.println("User was not found!");
            } catch (QuestionNotFoundException questionNotFoundException){
                System.out.println("Question was not found!");
            } catch (VoteNotFoundException voteNotFoundException){
                System.out.println("Vote was not found!");
            } catch (TagNotFoundException tagNotFoundException){
                System.out.println("Tag was not found!");
            } catch (AnswerNotFoundException answerNotFoundException){
                System.out.println("Answer was not found!");
            }
        }
    }

    private boolean handleCommand(String command){
        switch(command){
            case "users":
                handleListUsers();
                return false;
            case "register":
                handleRegistration();
                return false;
            case "login":
                handleLogin();
                return false;
            case "logout":
                handleLogout();
                return false;
            case "ask":
                handleAskQuestion();
                return false;
            case "questions":
                handleListQuestions();
                return false;
            case "question_id":
                handleQuestionId();
                return false;
            case "search_title":
                handleSearchQuestionTitle();
                return false;
            case "search_tag":
                handleSearchQuestionTag();
                return false;
            case "add_tag":
                handleAddTag();
                return false;
            case "tags":
                handleListTags();
                return false;
            case "answer_question":
                handleAnswerQuestion();
                return false;
            case "delete_answer":
                handleDeleteAnswer();
                return false;
            case "edit_answer":
                handleEditAnswer();
                return false;
            case "upvote_answer":
                handleUpvoteAnswer();
                return false;
            case "upvote_question":
                handleUpvoteQuestion();
                return false;
            case "downvote_answer":
                handleDownvoteAnswer();
                return false;
            case "downvote_question":
                handleDownvoteQuestion();
                return false;

            case "exit":
                return true;
            default:
                System.out.println("Command not found! Try again!");
                return false;
        }
    }

    private void handleListUsers(){
        List<User> users = userManagementService.listUsers();
        if(users.isEmpty()){
            System.out.println("No users registered!");
        } else{
            for(User user : users){
                System.out.println(user);
            }
        }
    }

    private void handleRegistration(){
        System.out.print("Enter username: ");
        String username = scanner.next().trim();
        System.out.print("Enter password: ");
        String password = scanner.next().trim();
        System.out.print("Enter email: ");
        String email = scanner.next().trim();

        boolean registered = false;
        for(User u : userManagementService.listUsers()){
            if(u.getUsername().equals(username) || u.getEmail().equals(email)){
                System.out.println("User already registered. Please login!");
                registered = true;
                break;
            }
        }

        if(!registered){
            userManagementService.addUser(username, password, email);
            System.out.println("Account created successfully!");
        }
    }

    private void handleLogin(){
        System.out.print("Enter username: ");
        String username = scanner.next().trim();
        System.out.print("Enter password: ");
        String password = scanner.next().trim();

        currentUser = userManagementService.getUserByLogin(username, password);
        System.out.println("Login successful!");
    }

    private void handleLogout(){
        currentUser = null;
    }

    private void handleAskQuestion(){

        if(currentUser == null) {
            System.out.println("Please login or register!");
        } else {
            System.out.println("Question title: ");
            scanner.nextLine();
            String title = scanner.nextLine().trim();
            System.out.println("Question text: ");
            String text = scanner.nextLine();
            System.out.println("Add tags (separated by ',' ): ");
            String tagList = scanner.nextLine().trim();

            //parse string and return a list of tags
            List<Tag> tags = stringToTags(tagList);
            tags.forEach(tagManagementService::save);

            Question question = new Question(null, currentUser, title, text, new Timestamp(System.currentTimeMillis()),0, tags);
            questionManagementService.save(question);
        }
    }

    private void handleListQuestions(){
        if(currentUser == null) {
            System.out.println("Please login or register!");
        } else {
            List<Question> questions = questionManagementService.listQuestions();
            questions.sort((q1, q2) -> {
                return q1.getCreationDate().after(q2.getCreationDate()) ? -1 : 1;
            });
            questions.forEach(question -> System.out.println(question));
        }
    }

    private void handleQuestionId(){
        if(currentUser == null){
            System.out.println("Please login or register!");
        } else {
            System.out.println("Enter question id: ");
            scanner.nextLine();
            int id = scanner.nextInt();

            Question question = questionManagementService.findById(id);
            System.out.println(question);

            List<Answer> answers = answerManagementService.findByQuestion(question);
            answers.sort((a1, a2) -> a1.getVoteCount() >= a2.getVoteCount() ? -1 : 1);
            if(answers.isEmpty()){
                System.out.println("This question has no answers!");
            } else {
                System.out.println("Answers\n");
                answers.forEach(answer -> System.out.println(answer));
            }
        }
    }

    private void handleSearchQuestionTitle(){
        if(currentUser == null){
            System.out.println("Please login or register!");
        } else {
            System.out.println("Enter search title: ");
            scanner.nextLine();
            String text = scanner.nextLine().trim().split("\n")[0];     //get rid of the \n read by scanner

            //sort questions by most recent date
            List<Question> questions = questionManagementService.findQuestionByTitle(text);
            questions.sort((q1, q2) -> {
                return q1.getCreationDate().after(q2.getCreationDate()) ? -1 : 1;
            });


            if(questions.isEmpty()) {
                System.out.println("No questions matched your criteria!");
            } else {
                questions.forEach(question -> System.out.println(question));
            }
        }
    }

    private void handleSearchQuestionTag(){
        if(currentUser == null){
            System.out.println("Please login or register!");
        } else {
            System.out.println("Enter search tag: ");
            scanner.nextLine();
            String text = scanner.nextLine().trim().split("\n")[0];     //get rid of the \n read by scanner

            //sort questions by most recent date
            List<Question> questions = questionManagementService.findQuestionByTag(text);
            questions.sort((q1, q2) -> {
                return q1.getCreationDate().after(q2.getCreationDate()) ? -1 : 1;
            });


            if(questions.isEmpty()) {
                System.out.println("No questions matched your criteria!");
            } else {
                questions.forEach(question -> System.out.println(question));
            }
        }
    }

    private void handleAddTag(){

        if(currentUser == null){
            System.out.println("Please login or register!");
        } else {
            System.out.println("Enter question id you wish to add a tag to: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            Question question = questionManagementService.findById(id);

            System.out.println("Enter list of tags (separated by ',': ");
            String text = scanner.nextLine().trim();

            //save tags and remove duplicates
            List<Tag> tag = stringToTags(text);
            tag.forEach(tagManagementService::save);
            tag.forEach(tag1 -> questionManagementService.addTag(question, tag1));
            }
        }

    private void handleListTags(){
        if(currentUser == null){
            System.out.println("Please login or register!");
        } else {
            List<Tag> tags = tagManagementService.listTags();
            if(tags.isEmpty()){
                System.out.println("No tags found!");
            } else {
                tags.forEach(System.out::println);
            }
        }
    }

    private List<Tag> stringToTags(String tag){
        List<Tag> tags = new ArrayList<Tag>();
        String[] splitTags = tag.split(",");

        for(int i=0; i< splitTags.length; i++){
            tags.add(new Tag(null, splitTags[i].toLowerCase().trim()));
        }
        return tags;
    }

    private void handleAnswerQuestion(){
        if(currentUser == null){
            System.out.println("Please login or register!");
        } else {
            System.out.println("Enter question id you wish to asnwer to: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            Question question = questionManagementService.findById(id);

            System.out.println("Enter answer text: ");
            String text = scanner.nextLine().trim();
            Answer answer = new Answer(null, question, currentUser, text, new Timestamp(System.currentTimeMillis()), 0);
            answerManagementService.save(answer);
        }
    }

    private void handleDeleteAnswer(){
        if(currentUser == null){
            System.out.println("Please login or register!");
        } else {
            System.out.println("Enter answer id: ");
            int id = scanner.nextInt();

            Answer answer = answerManagementService.findById(id);
            if(answer.getUser().equals(currentUser)){
                answerManagementService.remove(answer);
                System.out.println("Answer was deleted!");
            } else {
                System.out.println("Answers can only be deleted by their author!");
            }
        }
    }

    private void handleEditAnswer(){
        if(currentUser == null){
            System.out.println("Please login or register!");
        } else {
            System.out.println("Enter answer id: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            Answer answer = answerManagementService.findById(id);
            if(answer.getUser().equals(currentUser)){
                System.out.println("Enter text: ");
                String text = scanner.nextLine().trim();

                answer.setText(text);
                answer.setCreationDate(new Timestamp(System.currentTimeMillis()));
                answerManagementService.save(answer);
                System.out.println("Answer was edited!");
            } else {
                System.out.println("Answers can only be edited by their author!");
            }
        }
    }

    private void handleUpvoteAnswer(){

        if(currentUser == null){
            System.out.println("Please login or register!");
        } else {
            System.out.println("Enter answer id: ");
            int id = scanner.nextInt();

            Answer answer = answerManagementService.findById(id);
            if(answer.getUser().equals(currentUser)){
                System.out.println("You cannot upvote your own answer!");
            } else {

                try {
                    Vote vote = voteManagementService.findByAnswerId(answer.getId(), currentUser.getId());
                    if(vote.getIs_upvote()){
                        System.out.println("You can only upvote once!");
                    } else {
                        vote.setIs_upvote(true);
                        voteManagementService.save(vote);

                        answer.setVoteCount(answer.getVoteCount() + 2);
                        answerManagementService.save(answer);

                        answer.getUser().setScore(answer.getUser().getScore() + 12);
                        userManagementService.save(answer.getUser());

                        currentUser.setScore(currentUser.getScore() + 1);
                        userManagementService.save(currentUser);
                    }
                } catch(VoteNotFoundException v){
                    Vote vote = new Vote(null, null, answer, currentUser, true);
                    voteManagementService.save(vote);

                    answer.setVoteCount(answer.getVoteCount() + 1);
                    answerManagementService.save(answer);

                    answer.getUser().setScore(answer.getUser().getScore() + 10);
                    userManagementService.save(answer.getUser());
                }
            }
        }
    }

    private void handleDownvoteAnswer(){

        if(currentUser == null){
            System.out.println("Please login or register!");
        } else {
            System.out.println("Enter answer id: ");
            int id = scanner.nextInt();

            Answer answer = answerManagementService.findById(id);
            if(answer.getUser().equals(currentUser)){
                System.out.println("You cannot upvote your own answer!");
            } else {

                try {
                    Vote vote = voteManagementService.findByAnswerId(answer.getId(), currentUser.getId());
                    if(!vote.getIs_upvote()){
                        System.out.println("You can only downvote once!");
                    } else {
                        vote.setIs_upvote(false);
                        voteManagementService.save(vote);

                        answer.setVoteCount(answer.getVoteCount() - 2);
                        answerManagementService.save(answer);

                        answer.getUser().setScore(answer.getUser().getScore() - 12);
                        userManagementService.save(answer.getUser());

                        currentUser.setScore(currentUser.getScore() - 1);
                        userManagementService.save(currentUser);
                    }
                } catch(VoteNotFoundException v){
                    Vote vote = new Vote(null, null, answer, currentUser, false);
                    voteManagementService.save(vote);

                    answer.setVoteCount(answer.getVoteCount() - 1);
                    answerManagementService.save(answer);

                    answer.getUser().setScore(answer.getUser().getScore() - 2);
                    userManagementService.save(answer.getUser());

                    currentUser.setScore(currentUser.getScore() - 1);
                    userManagementService.save(currentUser);
                }
            }
        }
    }

    private void handleUpvoteQuestion(){

        if(currentUser == null){
            System.out.println("Please login or register!");
        } else {
            System.out.println("Enter question id: ");
            int id = scanner.nextInt();

            Question question = questionManagementService.findById(id);
            if(question.getUser().equals(currentUser)){
                System.out.println("You cannot upvote your own answer!");
            } else {

                try {
                    Vote vote = voteManagementService.findByQuestionId(question.getId(), currentUser.getId());
                    if(vote.getIs_upvote()){
                        System.out.println("You can only upvote once!");
                    } else {
                        vote.setIs_upvote(true);
                        voteManagementService.save(vote);

                        question.setVoteCount(question.getVoteCount() + 2);
                        questionManagementService.save(question);

                        question.getUser().setScore(question.getUser().getScore() + 7);
                        userManagementService.save(question.getUser());
                    }
                } catch(VoteNotFoundException v){
                    Vote vote = new Vote(null, question, null, currentUser, true);
                    voteManagementService.save(vote);

                    question.setVoteCount(question.getVoteCount() + 1);
                    questionManagementService.save(question);

                    question.getUser().setScore(question.getUser().getScore() + 5);
                    userManagementService.save(question.getUser());
                }
            }
        }
    }

    private void handleDownvoteQuestion(){

        if(currentUser == null){
            System.out.println("Please login or register!");
        } else {
            System.out.println("Enter question id: ");
            int id = scanner.nextInt();

            Question question = questionManagementService.findById(id);
            if(question.getUser().equals(currentUser)){
                System.out.println("You cannot downvote your own answer!");
            } else {

                try {
                    Vote vote = voteManagementService.findByQuestionId(question.getId(), currentUser.getId());
                    if(!vote.getIs_upvote()){
                        System.out.println("You can only downvote once!");
                    } else {
                        vote.setIs_upvote(true);
                        voteManagementService.save(vote);

                        question.setVoteCount(question.getVoteCount() - 2);
                        questionManagementService.save(question);

                        question.getUser().setScore(question.getUser().getScore() - 7);
                        userManagementService.save(question.getUser());
                    }
                } catch(VoteNotFoundException v){
                    Vote vote = new Vote(null, question, null, currentUser, false);
                    voteManagementService.save(vote);

                    question.setVoteCount(question.getVoteCount() - 1);
                    questionManagementService.save(question);

                    question.getUser().setScore(question.getUser().getScore() - 2);
                    userManagementService.save(question.getUser());
                }
            }
        }
    }
}
