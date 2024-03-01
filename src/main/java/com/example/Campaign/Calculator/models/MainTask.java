package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

@Entity
public class MainTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mainTask_id;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match1 match_id;

    private String text;

    public void setMatch_id(Match1 match_id) {
        this.match_id = match_id;
    }

    public MainTask(String text) {
        this.text = text;
    }

    public MainTask() {
    }

}