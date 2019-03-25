package com.gabrielcireap.internet_banking_app.service;

import com.gabrielcireap.internet_banking_app.entity.User;
import com.gabrielcireap.internet_banking_app.exception.UserNotFoundException;
import com.gabrielcireap.internet_banking_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.gabrielcireap.internet_banking_app.repository.RepositoryFactory;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final RepositoryFactory repositoryFactory;

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
    public User addUser(String username, String password, String email){
        return repositoryFactory.createUserRepository().save(new User(username, password, email));
    }

    @Transactional
    public User save(User user) { return repositoryFactory.createUserRepository().save(user); }

    @Transactional
    public User getUserByLogin(String username, String password){
        return repositoryFactory.createUserRepository().findUserByLogin(username, password).orElseThrow(UserNotFoundException::new);
    }
}
