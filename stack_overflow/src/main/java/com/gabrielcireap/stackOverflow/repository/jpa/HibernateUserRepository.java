package com.gabrielcireap.stackOverflow.repository.jpa;

import com.gabrielcireap.stackOverflow.entity.User;
import com.gabrielcireap.stackOverflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HibernateUserRepository implements UserRepository {

    private final EntityManager entityManager;

    @Override
    public List<User> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        query.select(query.from(User.class));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public User save(User user) {
        if(user.getId() == null){
            entityManager.persist(user);
            return user;
        } else {
            return entityManager.merge(user);
        }
    }

    @Override
    public void remove(User user) {
        entityManager.remove(user);
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public Optional<User> findUserByLogin(String username, String password) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> userRoot = query.from(User.class);

        query.select(userRoot).where(
                builder.and(
                    builder.equal(userRoot.get("username"), username),
                    builder.equal(userRoot.get("password"), password)
               )
        );

        List<User> users = entityManager.createQuery(query).getResultList();
        return users.isEmpty() ? Optional.empty() : Optional.ofNullable(users.get(0));
    }
}
