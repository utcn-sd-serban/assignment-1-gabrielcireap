package com.gabrielcireap.stackOverflow.service;

import com.gabrielcireap.stackOverflow.entity.User;
import com.gabrielcireap.stackOverflow.exception.*;
import com.gabrielcireap.stackOverflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.gabrielcireap.stackOverflow.repository.RepositoryFactory;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final RepositoryFactory repositoryFactory;
    private User currentUser;

    @Transactional
    public List<User> listUsers(){
        return repositoryFactory.createUserRepository().findAll();
    }

    @Transactional
    public void remove(int userId){
        UserRepository userRepository = repositoryFactory.createUserRepository();
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userRepository.remove(user);
    }

    @Transactional
    public User save(String username, String password, String email){

        Optional<User> user = listUsers().stream().filter(user1 -> user1.getEmail().equals(email) || user1.getUsername().equals(username)).findFirst();
        if(user.isPresent()){
            throw new DuplicateUserException();
        }
        return repositoryFactory.createUserRepository().save(new User(username, password, email));
    }

    @Transactional
    public User save(User user) { return repositoryFactory.createUserRepository().save(user); }

    @Transactional
    public User findById(int id){
        return repositoryFactory.createUserRepository().findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public User login(String username, String password){

        User user = repositoryFactory.createUserRepository().findUserByLogin(username, password).orElseThrow(UserNotFoundException::new);
        if(user.getIsBanned()){
            currentUser = null;
            throw new BannedUserException();
        } else {
            currentUser = user;
        }
        return user;
    }

    @Transactional
    public void logout(){ currentUser = null; }

    public User ban(int userId){
        checkIfUserIsLogged();
        if(!currentUser.getIsAdmin()){
            throw new NotEnoughPermissionsException();
        }

        User user = findById(userId);
        user.setIsBanned(true);
        save(user);
        return user;
    }

    @Transactional
    public void checkIfUserIsLogged(){
        if(currentUser == null){
            throw new UserNotLoggedInException();
        }
    }

    @Transactional
    public User getLoggedUser() { return currentUser; }
}
