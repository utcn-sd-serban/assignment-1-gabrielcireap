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

    public Question(int id){
        this.id = id;
        this.user = null;
        this.title = null;
        this.text = null;
        this.creationDate = null;
        this.voteCount = 0;
        this.tags = null;
    }
}
