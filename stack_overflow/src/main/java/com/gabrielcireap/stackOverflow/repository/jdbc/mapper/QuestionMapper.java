package com.gabrielcireap.stackOverflow.repository.jdbc.mapper;

import com.gabrielcireap.stackOverflow.entity.Question;
import com.gabrielcireap.stackOverflow.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@RequiredArgsConstructor
public class QuestionMapper implements RowMapper<Question> {

    @Override
    public Question mapRow(ResultSet resultSet, int i) throws SQLException {

        int id = resultSet.getInt("id");
        int userId = resultSet.getInt("user_id");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        String email = resultSet.getString("email");
        int score = resultSet.getInt("score");
        boolean is_admin = resultSet.getBoolean("is_admin");
        boolean is_banned = resultSet.getBoolean("is_banned");
        String title = resultSet.getString("title");
        String text = resultSet.getString("text");
        Timestamp creationDate = resultSet.getTimestamp("creation_date");
        int voteCount = resultSet.getInt("vote_count");

        User user = new User(userId, username, password, email, score, is_admin, is_banned);
        return new Question(id, user, title, text, creationDate, voteCount, null);
    }
}
