package com.example.Campaign.Calculator.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "campaign")
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "campaign_id")
    private Long campaign_id;

    private String campaignName;
    private int battleValue, numOfPilots;
    private LocalDate startDate;
    private boolean isEnded;

    @OneToMany(mappedBy = "campaign")
    private Set<Match1> matches = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "campaign_user",
            joinColumns = @JoinColumn(name = "campaign_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private FormationOrder formationOrder;

    @Enumerated(EnumType.STRING)
    private CampaignType campaignType;

    public String getCampaignName() {
        return campaignName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public Long getCampaign_id() {
        return campaign_id;
    }

    public int getNumOfPilots() {
        return numOfPilots;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Campaign(String name, CampaignType campaignType, FormationOrder formationOrder, int battleValue,
                    LocalDate startDate) {
        this.campaignName = name;
        this.campaignType = campaignType;
        this.formationOrder = formationOrder;
        this.battleValue = battleValue;
        this.startDate = startDate;
        isEnded = false;
        if(formationOrder == FormationOrder.lance){
            numOfPilots = 4;
        }
        else{
            numOfPilots = 5;
        }
    }

    public Campaign() {

    }

}

