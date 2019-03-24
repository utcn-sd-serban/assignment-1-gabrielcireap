package com.gabrielcireap.internet_banking_app.repository.jdbc;

import com.gabrielcireap.internet_banking_app.entity.Answer;
import com.gabrielcireap.internet_banking_app.entity.Question;
import com.gabrielcireap.internet_banking_app.entity.User;
import com.gabrielcireap.internet_banking_app.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class JdbcAnswerRepository implements AnswerRepository {

    private final JdbcTemplate template;

    @Override
    public Answer save(Answer answer) {
        if(answer.getId() == null){
            answer.setId(insert(answer));
        } else {
            update(answer);
        }
        return answer;
    }

    @Override
    public void remove(Answer answer) {
        template.update("DELETE FROM answer WHERE id = ?", answer.getId());
    }

    @Override
    public Optional<Answer> findById(int id) {
        List<Answer> answers = template.query("SELECT * FROM answer WHERE id = ?", new AnswerMapper(), id);

        List<User> users = template.query("SELECT user.id, user.username, user.password, user.email, user.score, user.is_admin, user.is_banned " +
                "FROM user JOIN answer ON answer.user_id = user.id WHERE answer.id = ?", new UserMapper(), id);

        List<Question> questions = template.query("SELECT " +
                "question.id, user.id as \"user_id\", user.username, user.password, user.email, user.score, user.is_admin, user.is_banned, " +
                "question.title, question.text, question.creation_date, question.vote_count " +
                "FROM question JOIN user ON question.user_id = user.id " +
                "JOIN answer ON answer.question_id = question.id WHERE answer.id = ?", new QuestionMapper(), id);

        for(int i=0; i<answers.size(); i++){
            answers.get(i).setUser(users.get(i));
            answers.get(i).setQuestion(questions.get(i));
        }

        return answers.isEmpty() ? Optional.empty(): Optional.ofNullable(answers.get(0)) ;
    }

    @Override
    public List<Answer> findByQuestion(Question question) {

        List<User> users = template.query("SELECT user.id, user.username, user.password, user.email, user.score, user.is_admin, user.is_banned " +
                "FROM user JOIN answer ON answer.user_id = user.id WHERE answer.question_id = ?", new UserMapper(), question.getId());

        List<Answer> answers = template.query("SELECT * FROM answer WHERE question_id = ?", new AnswerMapper(), question.getId());
        for(int i=0; i<answers.size(); i++){
            answers.get(i).setUser(users.get(i));
        }

        return answers;
    }

    private int insert(Answer answer){
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("answer");
        insert.usingGeneratedKeyColumns("id");
        Map<String, Object> map = new HashMap<>();
        map.put("question_id", answer.getQuestion().getId());
        map.put("user_id", answer.getUser().getId());
        map.put("text", answer.getText());
        map.put("creation_date", answer.getCreationDate());
        map.put("vote_count", answer.getVoteCount());
        return insert.executeAndReturnKey(map).intValue();
    }

    private void update(Answer answer){
        template.update("UPDATE answer SET question_id = ?, user_id = ?, text = ?, creation_date = ?, vote_count = ? where id = ?",
                answer.getQuestion().getId(), answer.getUser().getId(), answer.getText(), answer.getCreationDate(), answer.getVoteCount(), answer.getId());
    }
}
