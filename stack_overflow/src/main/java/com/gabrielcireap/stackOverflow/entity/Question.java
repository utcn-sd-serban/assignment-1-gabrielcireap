package com.gabrielcireap.stackOverflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn
    private User user;
    private String title;
    private String text;
    private Timestamp creationDate;
    private Integer voteCount;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "questiontotags", joinColumns = @JoinColumn(name = "question_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
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

    public Question(String title, String text, User user){
        this.id = null;
        this.user = user;
        this.title = title;
        this.text = text;
        this.creationDate = null;
        this.voteCount = 0;
        this.tags = null;
    }
}
