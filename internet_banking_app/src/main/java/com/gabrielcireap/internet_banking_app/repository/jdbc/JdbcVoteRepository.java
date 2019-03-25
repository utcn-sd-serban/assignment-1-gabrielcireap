package com.gabrielcireap.internet_banking_app.repository.jdbc;

import com.gabrielcireap.internet_banking_app.entity.Vote;
import com.gabrielcireap.internet_banking_app.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class JdbcVoteRepository implements VoteRepository {

    private final JdbcTemplate template;

    @Override
    public Vote save(Vote vote) {
        if(vote.getId() == null){
            vote.setId(insert(vote));
        } else {
            update(vote);
        }
        return vote;
    }

    @Override
    public void remove(Vote vote) {
        template.update("DELETE FROM vote WHERE id = ?", vote.getId());
    }

    @Override
    public Optional<Vote> findById(int id) {
        List<Vote> votes = template.query("SELECT * FROM vote WHERE id = ?", new VoteMapper(), id);
        return votes.isEmpty() ? Optional.empty() : Optional.ofNullable(updateVote(votes.get(0)));
    }


    @Override
    public Optional<Vote> findByAnswerId(int answerId, int userId) {
        List<Vote> votes = template.query("SELECT * FROM vote WHERE answer_id = ? AND user_id = ?", new VoteMapper(), answerId, userId);
        return votes.isEmpty() ? Optional.empty() : Optional.ofNullable(updateVote(votes.get(0)));
    }

    @Override
    public Optional<Vote> findByQuestionId(int questionId, int userId) {
        List<Vote> votes = template.query("SELECT * FROM vote WHERE question_id = ? AND user_id = ?", new VoteMapper(), questionId, userId);
        return votes.isEmpty() ? Optional.empty() : Optional.ofNullable(updateVote(votes.get(0)));
    }

    private int insert(Vote vote){
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("vote");
        insert.usingGeneratedKeyColumns("id");
        Map<String, Object> map = new HashMap<>();

        if(vote.getQuestion() != null){
            map.put("question_id", vote.getQuestion().getId());
        } else {
            map.put("question_id", null);
        }

        if(vote.getAnswer() != null){
            map.put("answer_id", vote.getAnswer().getId());
        } else {
            map.put("answer_id", null);
        }

        map.put("user_id", vote.getUser().getId());
        map.put("is_upvote", vote.getIs_upvote());
        return insert.executeAndReturnKey(map).intValue();
    }

    private void update(Vote vote){
        template.update("UPDATE user SET question_id = ?, answer_id = ?, user_id = ?, is_upvote = ? where id = ?",
                            vote.getQuestion() != null ? vote.getQuestion().getId() : null,
                            vote.getAnswer() != null ? vote.getAnswer().getId() : null,
                            vote.getUser().getId(), vote.getIs_upvote(), vote.getId());
    }

    private Vote updateVote(Vote vote){
        JdbcQuestionRepository questionRepository = new JdbcQuestionRepository(template);
        JdbcAnswerRepository answerRepository = new JdbcAnswerRepository(template);
        JdbcUserRepository userRepository = new JdbcUserRepository(template);

        vote.setUser(userRepository.findById(vote.getUser().getId()).get());
        vote.setAnswer(answerRepository.findById(vote.getUser().getId()).get());
        vote.setQuestion(questionRepository.findById(vote.getUser().getId()).get());

        return vote;
    }
}
