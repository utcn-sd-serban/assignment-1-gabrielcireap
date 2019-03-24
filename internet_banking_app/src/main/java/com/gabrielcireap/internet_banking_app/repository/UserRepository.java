package com.gabrielcireap.internet_banking_app.repository;

import com.gabrielcireap.internet_banking_app.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> findAll();
    User save(User user);
    void remove(User user);
    Optional<User> findById(int id);
    Optional<User> findUserByLogin(String username, String password);
}
