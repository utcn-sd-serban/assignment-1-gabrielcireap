package com.gabrielcireap.stackOverflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn
    private Question question;

    @ManyToOne
    @JoinColumn
    private User user;
    private String text;
    private Timestamp creationDate;
    private Integer voteCount;

    public Answer(int id){
        this.id = id;
        this.question = null;
        this.user = null;
        this.text = null;
        this.creationDate = null;
        this.voteCount = 0;
    }

    public Answer(String text, Question question, User user){
        this.id = null;
        this.text = text;
        this.user = user;
        this.question = question;
        this.creationDate = null;
        this.voteCount = 0;
    }
}
