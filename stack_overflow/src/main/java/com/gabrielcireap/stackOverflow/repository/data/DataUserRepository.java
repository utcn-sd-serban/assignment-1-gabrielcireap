package com.gabrielcireap.stackOverflow.repository.data;

import com.gabrielcireap.stackOverflow.entity.User;
import com.gabrielcireap.stackOverflow.repository.UserRepository;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface DataUserRepository  extends Repository<User, Integer>, UserRepository {

    void delete(User user);
    @Override
    default void remove(User user) {
        delete(user);
    }

    Optional<User> findUserByUsernameAndPassword(String username, String password);
    @Override
    default Optional<User> findUserByLogin(String username, String password) {
        return findUserByUsernameAndPassword(username, password);
    }
}
