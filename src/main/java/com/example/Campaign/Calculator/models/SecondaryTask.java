package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

@Entity
public class SecondaryTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long secondaryTask_id;

    @ManyToOne
    @JoinColumn(name = "match1")
    private Match1 match1;

    @ManyToOne
    @JoinColumn(name = "player")
    private Player player;

    private String text;

    private boolean isCompleted;

    public void setMatch1(Match1 match1) {
        this.match1 = match1;
    }

    public String getText() {
        return text;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public SecondaryTask(String text, Match1 match, Player player) {
        this.text = text;
        this.isCompleted = false;
        this.match1 = match;
        this.player = player;
    }

    public SecondaryTask() {
    }
}
