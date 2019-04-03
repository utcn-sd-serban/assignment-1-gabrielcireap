package com.gabrielcireap.stackOverflow.repository.jpa;

import com.gabrielcireap.stackOverflow.entity.Vote;
import com.gabrielcireap.stackOverflow.repository.VoteRepository;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HibernateVoteRepository implements VoteRepository {

    private final EntityManager entityManager;

    @Override
    public Vote save(Vote vote) {
        if(vote.getId() == null){
            entityManager.persist(vote);
            return vote;
        } else {
            return entityManager.merge(vote);
        }
    }

    @Override
    public void remove(Vote vote) {
        entityManager.remove(vote);
    }

    @Override
    public Optional<Vote> findById(int id) {
        return Optional.ofNullable(entityManager.find(Vote.class, id));
    }

    @Override
    public Optional<Vote> findByAnswerId(int answerId, int userId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Vote> query = builder.createQuery(Vote.class);
        Root<Vote> voteRoot = query.from(Vote.class);

        query.select(voteRoot).where(
            builder.and(
                builder.equal(voteRoot.get("answer"), answerId),
                builder.equal(voteRoot.get("user"), userId)
            )
        );

        List<Vote> votes = entityManager.createQuery(query).getResultList();
        return votes.isEmpty() ? Optional.empty() : Optional.ofNullable(votes.get(0));
    }

    @Override
    public Optional<Vote> findByQuestionId(int questionId, int userId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Vote> query = builder.createQuery(Vote.class);
        Root<Vote> voteRoot = query.from(Vote.class);

        query.select(voteRoot).where(
            builder.and(
                builder.equal(voteRoot.get("question").get("id"), questionId),
                builder.equal(voteRoot.get("user").get("id"), userId)
            )
        );

        List<Vote> votes = entityManager.createQuery(query).getResultList();
        return votes.isEmpty() ? Optional.empty() : Optional.ofNullable(votes.get(0));
    }
}
