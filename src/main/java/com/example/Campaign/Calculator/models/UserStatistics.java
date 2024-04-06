package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

@Entity
public class UserStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long uStat_id;

    private int gamesTotal, gamesWon, goalsAchieved, rating;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user_id;

    public int getGamesWon() {
        return gamesWon;
    }
}
