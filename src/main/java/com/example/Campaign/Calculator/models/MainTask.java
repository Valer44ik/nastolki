package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

@Entity
public class MainTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mainTask_id;

    @ManyToOne
    @JoinColumn(name = "match1")
    private Match1 match1;

    @ManyToOne
    @JoinColumn(name = "player")
    private Player player;

    private String text;

    private Boolean isCompleted;

    public void setMatch1(Match1 match1) {
        this.match1 = match1;
    }

    public String getText() {
        return text;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public MainTask() {
    }

    public MainTask(String text, Match1 match, Player player) {
        this.text = text;
        this.isCompleted = false;
        this.match1 = match;
        this.player = player;
    }
}
