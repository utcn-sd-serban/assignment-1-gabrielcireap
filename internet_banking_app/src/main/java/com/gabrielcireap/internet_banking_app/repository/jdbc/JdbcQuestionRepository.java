package com.gabrielcireap.internet_banking_app.repository.jdbc;

import com.gabrielcireap.internet_banking_app.entity.Question;
import com.gabrielcireap.internet_banking_app.entity.Tag;
import com.gabrielcireap.internet_banking_app.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.stat.internal.QueryStatisticsImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class JdbcQuestionRepository implements QuestionRepository {

    private final JdbcTemplate template;

    @Override
    public List<Question> findAll() {

        List<Question> questions = template.query("SELECT " +
        "question.id, user.id as \"user_id\", user.username, user.password, user.email, user.score, user.is_admin, user.is_banned, " +
        "question.title, question.text, question.creation_date, question.vote_count " +
        "FROM question JOIN user on question.user_id = user.id", new QuestionMapper());

        questions.forEach(question -> question.setTags(findTagsById(question.getId())));
        return questions;
    }

    @Override
    public Question save(Question question) {
        if(question.getId() == null){
            question.setId(insert(question));
            insert(question.getId(), question.getTags());
        } else {
            update(question);
        }
        return question;
    }

    @Override
    public void remove(Question question) {
        template.update("DELETE FROM question WHERE id = ?", question.getId());
    }

    @Override
    public Optional<Question> findById(int id) {
        List<Question> question = template.query("SELECT " +
                "question.id, user.id as \"user_id\", user.username, user.password, user.email, user.score, user.is_admin, user.is_banned, " +
                "question.title, question.text, question.creation_date, question.vote_count " +
                "FROM question JOIN user on question.user_id = user.id WHERE question.id = ?", new QuestionMapper(), id);

        if(question.isEmpty()){
            return Optional.empty();
        } else {
            question.get(0).setTags(findTagsById(question.get(0).getId()));
            return Optional.ofNullable(question.get(0));
        }
    }

    @Override
    public List<Question> findByTitle(String text) {

        String format = "'%"+text+"%'";
        List<Question> questions = template.query("SELECT " +
                "question.id, user.id as \"user_id\", user.username, user.password, user.email, user.score, user.is_admin, user.is_banned, " +
                "question.title, question.text, question.creation_date, question.vote_count " +
                "FROM question JOIN user ON question.user_id = user.id WHERE question.title LIKE " + format, new QuestionMapper());
        questions.forEach(question -> question.setTags(findTagsById(question.getId())));

        return questions;
    }

    @Override
    public List<Question> findByTag(String tag) {
        List<Question> questions =  template.query(
        "SELECT question.id, question.user_id, question.title, question.text, question.creation_date, question.vote_count, tag.id as \"tag_id\", tag.name as \"tag_name\", " +
            "user.id, user.username, user.password, user.email, user.score, user.is_admin, user.is_banned" +
            " FROM question JOIN questiontotags ON question.id = questiontotags.question_id " +
            "JOIN tag ON tag.id = questiontotags.tag_id " +
            "JOIN user ON user.id = question.user_id WHERE tag.name = '" + tag + "'", new QuestionMapper());
        questions.forEach(question -> question.setTags(findTagsById(question.getId())));
        return questions;
    }

    @Override
    public void addTag(Question question, Tag tag) {
        template.update("INSERT INTO questiontotags(question_id, tag_id) VALUES (?, ?)", question.getId(), tag.getId());
    }

    private int insert(Question question){
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("question");
        insert.usingGeneratedKeyColumns("id");
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", question.getUser().getId());
        map.put("title", question.getTitle());
        map.put("text", question.getText());
        map.put("creation_date", question.getCreationDate());
        map.put("vote_count", question.getVoteCount());

        return insert.executeAndReturnKey(map).intValue();
    }

    private void insert(int questionId, List<Tag> tags){
        tags.forEach(tag -> {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
            insert.setTableName("questiontotags");
            Map<String, Object> map = new HashMap<>();
            map.put("question_id", questionId);
            map.put("tag_id", tag.getId());
            insert.execute(map);
        });
    }

    private void update(Question question){
        template.update("UPDATE question SET user_id = ?, title = ?, text = ?, creation_date = ?, vote_count = ? where id = ?",
                question.getUser().getId(), question.getTitle(), question.getText(), question.getCreationDate(), question.getVoteCount(), question.getId());
    }

    private List<Tag> findTagsById(int questionId){
        return template.query("SELECT tag.name as \"tag_name\", tag.id as \"tag_id\" FROM questiontotags JOIN tag ON questiontotags.tag_id = tag.id WHERE questiontotags.question_id = ?",
                (resultSet, i) -> new Tag(resultSet.getInt("tag_id"), resultSet.getString("tag_name")), questionId);
    }
}