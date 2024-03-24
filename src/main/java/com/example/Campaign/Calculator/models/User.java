package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long user_id;

    private String nickname, email, login, password;

    @OneToOne(mappedBy = "user_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserStatistics userStatistics;

    @ManyToMany(mappedBy = "users")
    private Set<Campaign> campaigns = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Pilot> pilots = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Mech> mechs = new HashSet<>();

    public Long getUser_id() {
        return user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public User(String nickname, String email, String login, String password) {
        this.nickname = nickname;
        this.email = email;
        this.login = login;
        this.password = password;
    }

    public User() {
    }
}
