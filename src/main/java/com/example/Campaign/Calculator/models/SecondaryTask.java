package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

@Entity
public class SecondaryTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long secondaryTask_id;

    @ManyToOne
    @JoinColumn(name = "match")
    private Match1 match;

    private String text;

    private boolean isCompleted;

    public void setMatch(Match1 match) {
        this.match = match;
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

    public SecondaryTask(String text) {
        this.text = text;
        this.isCompleted = false;
    }

    public SecondaryTask() {
    }
}
