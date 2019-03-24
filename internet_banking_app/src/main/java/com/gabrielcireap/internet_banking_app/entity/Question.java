package com.gabrielcireap.internet_banking_app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private User user;
    private String title;
    private String text;
    private Timestamp creationDate;
    private Integer voteCount;
    private List<Tag> tags;
}
