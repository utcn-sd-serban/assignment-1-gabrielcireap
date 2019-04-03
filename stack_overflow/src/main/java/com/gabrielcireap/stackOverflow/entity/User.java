package com.gabrielcireap.stackOverflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private String email;
    private Integer score;
    private Boolean isAdmin;
    private Boolean isBanned;

    public User(String username, String password, String email){
        this.id = null;
        this.email = email;
        this.username = username;
        this.password = password;
        this.score = 0;
        this.isAdmin = false;
        this.isBanned = false;
    }

    public User(int id){
        this.id = id;
        this.email = null;
        this.username = null;
        this.password = null;
        this.score = 0;
        this.isAdmin = false;
        this.isBanned = false;
    }

    @Override
    public String toString(){
        return "User(" + username + ", " + score + ")";
    }
}
