package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long player_id;
    private int gamesTotal, gamesWon, campaignsTotal, campaignsWon;
    private String nickname, firstName, lastName;

    @ManyToMany(mappedBy = "players")
    private Set<Campaign> campaigns = new HashSet<>();

    @OneToMany(mappedBy = "player")
    private Set<Pilot> pilots = new HashSet<>();

    @OneToMany(mappedBy = "player")
    private Set<Mech> mechs = new HashSet<>();

    @OneToMany(mappedBy = "player")
    private Set<MainTask> mainTasks = new HashSet<>();

    @OneToMany(mappedBy = "player")
    private Set<SecondaryTask> secondaryTasks = new HashSet<>();

    public Long getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(Long player_id) {
        this.player_id = player_id;
    }

    public String getNickname() {
        return nickname;
    }

    public int getGamesTotal() {
        return gamesTotal;
    }

    public void setGamesTotal(int gamesTotal) {
        this.gamesTotal = gamesTotal;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getCampaignsTotal() {
        return campaignsTotal;
    }

    public void setCampaignsTotal(int campaignsTotal) {
        this.campaignsTotal = campaignsTotal;
    }

    public int getCampaignsWon() {
        return campaignsWon;
    }

    public Set<Campaign> getCampaigns() {
        return campaigns;
    }

    public void setCampaignsWon(int campaignsWon) {
        this.campaignsWon = campaignsWon;
    }

    public void setCampaigns(Set<Campaign> campaigns) {
        this.campaigns = campaigns;
    }

    public Player(String nickname, String firstName, String lastName) {
        this.nickname = nickname;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gamesTotal = 0;
        this.gamesWon = 0;
        this.campaignsTotal = 0;
        this.campaignsWon = 0;
    }

    public Player() {
    }

    public void removeCampaign(Campaign campaign) {
        this.campaigns.remove(campaign);
        campaign.getPlayers().remove(this);
    }
}
