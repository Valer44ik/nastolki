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

    public SecondaryTask(String text) {
        this.text = text;
        this.isCompleted = false;
    }

    public SecondaryTask() {
    }
}
