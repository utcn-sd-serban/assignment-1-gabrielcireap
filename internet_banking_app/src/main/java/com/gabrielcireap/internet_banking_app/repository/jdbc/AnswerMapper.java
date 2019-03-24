package com.gabrielcireap.internet_banking_app.repository.jdbc;

import com.gabrielcireap.internet_banking_app.entity.Answer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AnswerMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        int id = resultSet.getInt("id");
        //int questionId = resultSet.getInt("question_id");
        //int userId = resultSet.getInt("user_id");
        String text = resultSet.getString("text");
        Timestamp creationDate = resultSet.getTimestamp("creation_date");
        int voteCount = resultSet.getInt("vote_count");

        return new Answer(id, null, null, text, creationDate, voteCount);
    }
}
