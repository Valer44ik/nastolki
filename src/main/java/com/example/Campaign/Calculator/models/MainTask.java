package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

@Entity
public class MainTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mainTask_id;

    @ManyToOne
    @JoinColumn(name = "match")
    private Match1 match;

    private String text;

    private Boolean isCompleted;

    public void setMatch(Match1 match) {
        this.match = match;
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

    public MainTask(String text) {
        this.text = text;
        this.isCompleted = false;
    }

    public MainTask() {
    }

}
