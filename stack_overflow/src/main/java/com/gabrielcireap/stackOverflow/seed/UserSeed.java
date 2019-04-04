package com.gabrielcireap.stackOverflow.seed;

import com.gabrielcireap.stackOverflow.entity.User;
import com.gabrielcireap.stackOverflow.repository.RepositoryFactory;
import com.gabrielcireap.stackOverflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "stack_overflow.repository-type", havingValue = "MEMORY")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserSeed implements CommandLineRunner {

    private final RepositoryFactory repositoryFactory;

    @Override
    public void run(String... args) throws Exception {
        UserRepository userRepository = repositoryFactory.createUserRepository();
        if(userRepository.findAll().isEmpty()){
            userRepository.save(new User(null, "user1", "pass1", "email1", 0, true, false));
            userRepository.save(new User("user2", "pass2", "email2"));
            userRepository.save(new User("user3", "pass3", "email3"));
            userRepository.save(new User("user4", "pass4", "email4"));
        }
    }
}
