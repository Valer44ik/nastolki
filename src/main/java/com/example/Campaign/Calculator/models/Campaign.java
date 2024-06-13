package com.example.Campaign.Calculator.models;

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

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToMany
    @JoinTable(name = "campaign_player",
            joinColumns = @JoinColumn(name = "campaign_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id"))
    private Set<Player> players = new HashSet<>();

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

    public void setEnded(boolean ended) {
        isEnded = ended;
    }

    public Long getCampaign_id() {
        return campaign_id;
    }

    public int getNumOfPilots() {
        return numOfPilots;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Campaign(String name, CampaignType campaignType, FormationOrder formationOrder, int battleValue,
                    LocalDate startDate, User user) {
        this.campaignName = name;
        this.campaignType = campaignType;
        this.formationOrder = formationOrder;
        this.battleValue = battleValue;
        this.startDate = startDate;
        this.user = user;
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

    public void removeMatch(Match1 match) {
        matches.remove(match);
        match.setCampaign(null);
    }

    public void removePlayer(Player player) {
        this.players.remove(player);
        player.getCampaigns().remove(this);
    }
}

