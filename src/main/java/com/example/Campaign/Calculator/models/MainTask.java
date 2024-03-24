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

    public MainTask(String text) {
        this.text = text;
        this.isCompleted = false;
    }

    public MainTask() {
    }

}
