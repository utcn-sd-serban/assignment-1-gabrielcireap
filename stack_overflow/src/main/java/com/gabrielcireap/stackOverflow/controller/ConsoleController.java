package com.gabrielcireap.stackOverflow.controller;

import com.gabrielcireap.stackOverflow.entity.*;
import com.gabrielcireap.stackOverflow.exception.*;
import com.gabrielcireap.stackOverflow.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

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
            } catch (TagNotFoundException tagNotFoundException){
                System.out.println("Tag was not found!");
            } catch (AnswerNotFoundException answerNotFoundException){
                System.out.println("Answer was not found!");
            } catch (DuplicateUserException duplicateUserException){
                System.out.println("User already exists!");
            } catch (BannedUserException bannedUserException){
                System.out.println("User is banned!");
            } catch (UserNotLoggedInException userNotLoggedInException){
                System.out.println("Please log in first!");
            } catch (NotEnoughPermissionsException notEnoughPermissionsException){
                System.out.println("You are not allowed to perform this operation!");
            } catch (UpvoteDuplicateException upvoteDuplicateException) {
                System.out.println("You can only upvote once!");
            } catch (DownvoteDuplicateException downvoteDuplicateException){
                System.out.println("You can only downvote once");
            } catch (VoteYourOwnException voteYourOwnException) {
                System.out.println("You cannot vote your own post!");
            } catch (NullPointerException nullPointerException) {
                System.out.println("No user is currently logged in!");
            }
        }
    }

    private boolean handleCommand(String command){
        switch(command){
            case "whoami":
                handleCurrentUser();
                return false;
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
            case "ban":
                handleBanUser();
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
            case "delete_question":
                handleDeleteQuestion();
                return false;
            case "edit_question":
                handleEditQuestion();
                return false;
            case "search_title":
                handleSearchQuestionTitle();
                return false;
            case "search_tag":
                handleSearchQuestionTag();
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

    private void handleCurrentUser(){
        User user = userManagementService.getLoggedUser();
        System.out.println(user + " is currently logged in!");
    }

    private void handleListUsers(){
        userManagementService.listUsers().forEach(System.out::println);
    }

    private void handleRegistration(){
        System.out.print("Enter username: ");
        String username = scanner.next().trim();
        System.out.print("Enter password: ");
        String password = scanner.next().trim();
        System.out.print("Enter email: ");
        String email = scanner.next().trim();

        User user = userManagementService.save(username, password, email);
        System.out.println(user + " created successfully");
    }

    private void handleLogin(){
        System.out.print("Enter username: ");
        String username = scanner.next().trim();
        System.out.print("Enter password: ");
        String password = scanner.next().trim();

        User user = userManagementService.login(username, password);
        System.out.println("Login successful");
    }

    private void handleLogout(){
        userManagementService.logout();
        System.out.println("Logged out!");
    }

    private void handleAskQuestion(){

        System.out.println("Question title: ");
        scanner.nextLine();
        String title = scanner.nextLine().trim();
        System.out.println("Question text: ");
        String text = scanner.nextLine();
        System.out.println("Add tags (separated by ',' ): ");
        String tagList = scanner.nextLine().trim();

        questionManagementService.save(title, text, tagList);
        System.out.println("Question was posted!");
    }

    private void handleListQuestions(){
        questionManagementService.listQuestions().forEach(System.out::println);
    }

    private void handleQuestionId(){
        System.out.println("Enter question id: ");
        int id = scanner.nextInt();
        Map<Question, List<Answer>> data = questionManagementService.findById(id);
        data.keySet().forEach(System.out::println);
        data.values().forEach(list -> list.forEach(System.out::println));
    }

    private void handleSearchQuestionTitle(){

        System.out.println("Enter search title: ");
        scanner.nextLine();
        String text = scanner.nextLine().trim();
        questionManagementService.findQuestionByTitle(text).forEach(System.out::println);
    }

    private void handleSearchQuestionTag(){

        System.out.println("Enter search tag: ");
        scanner.nextLine();
        String tag = scanner.nextLine().trim();
        questionManagementService.findQuestionByTag(tag).forEach(System.out::println);
    }

    private void handleListTags(){
        tagManagementService.listTags().forEach(System.out::println);
    }

    private void handleAnswerQuestion(){

        System.out.println("Enter question id you wish to asnwer to: ");
        int id = scanner.nextInt();
        System.out.println("Enter answer text: ");
        scanner.nextLine();
        String text = scanner.nextLine().trim();

        answerManagementService.save(id, text);
        System.out.println("Answer added!");
    }

    private void handleDeleteAnswer(){

        System.out.println("Enter answer id: ");
        int id = scanner.nextInt();
        answerManagementService.remove(id);
        System.out.println("Answer removed!");
    }

    private void handleEditAnswer(){
        System.out.println("Enter answer id: ");
        int id = scanner.nextInt();
        System.out.println("Enter text: ");
        scanner.nextLine();
        String text = scanner.nextLine().trim();
        answerManagementService.edit(id, text);
        System.out.println("Answer was edited!");
    }

    private void handleUpvoteAnswer(){

        System.out.println("Enter answer id: ");
        int id = scanner.nextInt();
        voteManagementService.upvoteAnswer(id);
        System.out.println("Upvoted!");
    }

    private void handleDownvoteAnswer(){

        System.out.println("Enter answer id: ");
        int id = scanner.nextInt();
        voteManagementService.downvoteAnswer(id);
        System.out.println("Downvoted!");
    }

    private void handleUpvoteQuestion(){
        System.out.println("Enter question id: ");
        int id = scanner.nextInt();
        voteManagementService.upvoteQuestion(id);
        System.out.println("Upvoted!");
    }

    private void handleDownvoteQuestion(){
        System.out.println("Enter question id: ");
        int id = scanner.nextInt();
        voteManagementService.downvoteQuestion(id);
        System.out.println("Downvoted!");
    }

    private void handleDeleteQuestion(){
        System.out.println("Enter question id: ");
        int id = scanner.nextInt();
        questionManagementService.remove(id);
        System.out.println("Question was deleted!");
    }

    private void handleEditQuestion(){

        System.out.println("Enter question id: ");
        int id = scanner.nextInt();
        System.out.println("Enter title: ");
        scanner.nextLine();
        String title = scanner.nextLine().trim();
        System.out.println("Enter text: ");
        String text = scanner.nextLine().trim();

        questionManagementService.edit(id, title, text);
        System.out.println("Question was edited!");
    }

    private void handleBanUser() {

        System.out.println("Enter user id: ");
        int id = scanner.nextInt();
        User user = userManagementService.ban(id);
        System.out.println(user + " was banned!");
    }
}
