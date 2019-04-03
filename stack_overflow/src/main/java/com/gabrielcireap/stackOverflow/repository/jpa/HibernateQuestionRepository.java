package com.gabrielcireap.stackOverflow.repository.jpa;

import com.gabrielcireap.stackOverflow.entity.Question;
import com.gabrielcireap.stackOverflow.entity.Tag;
import com.gabrielcireap.stackOverflow.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HibernateQuestionRepository implements QuestionRepository {

    private final EntityManager entityManager;

    @Override
    public List<Question> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Question> query = builder.createQuery(Question.class);
        query.select(query.from(Question.class));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Question save(Question question) {
        if(question.getId() == null){
            entityManager.persist(question);
            return question;
        } else {
            return entityManager.merge(question);
        }
    }

    @Override
    public void remove(Question question) {
        entityManager.remove(question);
    }

    @Override
    public Optional<Question> findById(int id) {
        return Optional.ofNullable(entityManager.find(Question.class, id));
    }

    @Override
    public List<Question> findByTitle(String text) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Question> query = builder.createQuery(Question.class);
        Root<Question> questionRoot = query.from(Question.class);

        query.select(questionRoot).where(builder.like(questionRoot.get("title"), "%" + text + "%"));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Question> findByTag(Tag tag) {
        List<Question> questions = findAll();
        List<Question> result = new ArrayList<>();
        for(Question q : questions){
            for(Tag t : q.getTags()){
                if(t.getName().equals(tag)){
                    result.add(q);
                }
            }
        }
        return result;
    }
}
