package com.gabrielcireap.internet_banking_app.controller;

import com.gabrielcireap.internet_banking_app.entity.Answer;
import com.gabrielcireap.internet_banking_app.entity.Question;
import com.gabrielcireap.internet_banking_app.entity.Tag;
import com.gabrielcireap.internet_banking_app.entity.User;
import com.gabrielcireap.internet_banking_app.service.AnswerManagementService;
import com.gabrielcireap.internet_banking_app.service.QuestionManagementService;
import com.gabrielcireap.internet_banking_app.service.TagManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.gabrielcireap.internet_banking_app.service.UserManagementService;

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
            }catch (RuntimeException e){
                //System.out.println("Question not found");
                e.printStackTrace();
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

        Optional<User> user = userManagementService.getUserByLogin(username, password);
        if(user.isPresent()){
            currentUser = user.get();
            System.out.println("Login successful!");
        } else {
            System.out.println("Error logging in!");
        }
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

            Optional<Question> question = questionManagementService.findById(id);
            if(question.isPresent()){
                System.out.println(question);

                List<Answer> answers = answerManagementService.findByQuestion(question.get());
                if(answers.isEmpty()){
                    System.out.println("This question has no answers!");
                } else {
                    System.out.println("Answers\n");
                    answers.forEach(answer -> System.out.println(answer));
                }
            } else {
                System.out.println("No question with id " + id + " was found!");
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

            Optional<Question> question = questionManagementService.findById(id);
            if(question.isPresent()){

                System.out.println("Enter list of tags (separated by ',': ");
                String text = scanner.nextLine().trim();

                //save tags and remove duplicates
                List<Tag> tag = stringToTags(text);
                tag.forEach(tagManagementService::save);
                tag.forEach(tag1 -> questionManagementService.addTag(question.get(), tag1));
            } else {
                System.out.println("No question with id " + id + " was found!");
            }
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

            Optional<Question> question = questionManagementService.findById(id);
            if(question.isPresent()){

                System.out.println("Enter answer text: ");
                String text = scanner.nextLine().trim();
                Answer answer = new Answer(null, question.get(), currentUser, text, new Timestamp(System.currentTimeMillis()), 0);
                answerManagementService.save(answer);

            } else {
                System.out.println("No question with id " + id + " was found!");
            }
        }
    }

    private void handleDeleteAnswer(){
        if(currentUser == null){
            System.out.println("Please login or register!");
        } else {
            System.out.println("Enter answer id: ");
            int id = scanner.nextInt();

            Optional<Answer> answer = answerManagementService.findById(id);
            if(answer.isPresent()){
                if(answer.get().getUser().equals(currentUser)){
                    answerManagementService.remove(answer.get());
                    System.out.println("Answer was deleted!");
                } else {
                    System.out.println("Answers can only be deleted by their author!");
                }
            } else {
                System.out.println("Could not find answer with given id!");
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

            Optional<Answer> answer = answerManagementService.findById(id);
            if(answer.isPresent()){
                if(answer.get().getUser().equals(currentUser)){
                    System.out.println("Enter text: ");
                    String text = scanner.nextLine().trim();

                    answer.get().setText(text);
                    answer.get().setCreationDate(new Timestamp(System.currentTimeMillis()));
                    answerManagementService.save(answer.get());
                    System.out.println("Answer was edited!");
                } else {
                    System.out.println("Answers can only be edited by their author!");
                }
            } else {
                System.out.println("Could not find answer with given id!");
            }
        }
    }
}
