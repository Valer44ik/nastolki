package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long game_id;

    private Date startDate, endDate;

    private String notes, status, name;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign_id;

}
