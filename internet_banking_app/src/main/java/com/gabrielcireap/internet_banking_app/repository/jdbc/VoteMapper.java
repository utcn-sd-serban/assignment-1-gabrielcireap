package com.gabrielcireap.internet_banking_app.repository.jdbc;

import com.gabrielcireap.internet_banking_app.entity.Answer;
import com.gabrielcireap.internet_banking_app.entity.Question;
import com.gabrielcireap.internet_banking_app.entity.User;
import com.gabrielcireap.internet_banking_app.entity.Vote;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VoteMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Vote(resultSet.getInt("id"),
                        new Question(resultSet.getInt("question_id")),
                        new Answer(resultSet.getInt("answer_id")),
                        new User(resultSet.getInt("user_id")),
                        resultSet.getBoolean("is_upvote"));
    }
}
