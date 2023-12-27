package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long campaign_id;

    private String campaignName, campaignType, formationOrder;
    private int battleValue, numOfPilots;
    private Date startDate;

    @OneToMany(mappedBy = "campaign_id")
    private Set<Game> games = new HashSet<>();

    public Campaign(String name, String campaignType, String formationOrder, int battleValue, int numOfPilots, Date startDate) {
        this.campaignName = name;
        this.campaignType = campaignType;
        this.formationOrder = formationOrder;
        this.battleValue = battleValue;
        this.numOfPilots = numOfPilots;
        this.startDate = startDate;
    }

    public Campaign() {

    }
}
