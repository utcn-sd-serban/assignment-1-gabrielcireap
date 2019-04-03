package com.gabrielcireap.stackOverflow.repository.jdbc.mapper;

import com.gabrielcireap.stackOverflow.entity.Answer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AnswerMapper implements RowMapper<Answer> {

    @Override
    public Answer mapRow(ResultSet resultSet, int i) throws SQLException {

        int id = resultSet.getInt("id");
        String text = resultSet.getString("text");
        Timestamp creationDate = resultSet.getTimestamp("creation_date");
        int voteCount = resultSet.getInt("vote_count");

        return new Answer(id, null, null, text, creationDate, voteCount);
    }
}
