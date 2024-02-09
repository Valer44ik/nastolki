package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Match1 {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long match_id;

    private Date startDate, endDate;

    private String notes, name;

    private boolean isEnded;

    @ManyToOne
    @JoinColumn(name = "campaign")
    private Campaign campaign;

    public Date getStartDate() {
        return startDate;
    }

    public String getName() {
        return name;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public Match1(Long match_id, Date startDate, String name, Campaign campaign) {
        this.match_id = match_id;
        this.startDate = startDate;
        this.isEnded = false;
        this.name = name;
        this.campaign = campaign;
    }

    public Match1(){};

}
