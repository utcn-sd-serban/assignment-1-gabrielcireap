package com.gabrielcireap.internet_banking_app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
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
    private Boolean is_admin;
    private Boolean is_banned;

    public User(String username, String password, String email){
        this.id = null;
        this.email = email;
        this.username = username;
        this.password = password;
        this.score = 0;
        this.is_admin = false;
        this.is_banned = false;
    }

    @Override
    public String toString(){
        return "User(" + username + ", " + score + ")";
    }
}
