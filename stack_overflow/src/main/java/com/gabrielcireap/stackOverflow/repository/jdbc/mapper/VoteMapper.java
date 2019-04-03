package com.gabrielcireap.stackOverflow.repository.jdbc.mapper;

import com.gabrielcireap.stackOverflow.entity.Answer;
import com.gabrielcireap.stackOverflow.entity.Question;
import com.gabrielcireap.stackOverflow.entity.User;
import com.gabrielcireap.stackOverflow.entity.Vote;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VoteMapper implements RowMapper<Vote> {

    @Override
    public Vote mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Vote(resultSet.getInt("id"),
                        new Question(resultSet.getInt("question_id")),
                        new Answer(resultSet.getInt("answer_id")),
                        new User(resultSet.getInt("user_id")),
                        resultSet.getBoolean("is_upvote"));
    }
}
