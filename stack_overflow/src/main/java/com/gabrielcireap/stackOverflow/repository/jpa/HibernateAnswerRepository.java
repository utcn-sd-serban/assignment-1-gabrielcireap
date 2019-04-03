package com.gabrielcireap.stackOverflow.repository.jpa;

import com.gabrielcireap.stackOverflow.entity.Answer;
import com.gabrielcireap.stackOverflow.entity.Question;
import com.gabrielcireap.stackOverflow.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HibernateAnswerRepository implements AnswerRepository {

    private final EntityManager entityManager;

    @Override
    public Answer save(Answer answer) {
        if(answer.getId() == null){
            entityManager.persist(answer);
            return answer;
        } else {
            return entityManager.merge(answer);
        }
    }

    @Override
    public void remove(Answer answer) {
        entityManager.remove(answer);
    }

    @Override
    public Optional<Answer> findById(int id) {
        return Optional.ofNullable(entityManager.find(Answer.class, id));
    }

    @Override
    public List<Answer> findByQuestion(Question question) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Answer> query = builder.createQuery(Answer.class);
        Root<Answer> answerRoot = query.from(Answer.class);

        query.select(answerRoot).where(builder.equal(answerRoot.get("question"), question));
        return entityManager.createQuery(query).getResultList();
    }
}
