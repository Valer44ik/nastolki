package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long user_id;

    private String nickname, email, login, password;

    @OneToOne(mappedBy = "user_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserStatistics userStatistics;
}
