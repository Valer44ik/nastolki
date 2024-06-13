package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pilot")
public class Pilot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pilot_id")
    private Long pilot_id;

    private short inactiveCount;

    private boolean currentlyInCampaign;

    private String name, nickname, surname;

    @ManyToOne
    @JoinColumn(name = "pilotRank")
    private PilotRank pilotRank;

    @ManyToOne
    @JoinColumn(name = "pilotStatus")
    private PilotStatus pilotStatus;

    @ManyToOne
    @JoinColumn(name = "player")
    private Player player;

    @ManyToMany(mappedBy = "pilots")
    private Set<Match1> matches = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getPlayer() {
        return player;
    }

    public Long getPilot_id() {
        return pilot_id;
    }

    public PilotRank getPilotRank() {
        return pilotRank;
    }

    public void setPilotRank(PilotRank pilotRank) {
        this.pilotRank = pilotRank;
    }

    public PilotStatus getPilotStatus() {
        return pilotStatus;
    }

    public void setPilotStatus(PilotStatus pilotStatus) {
        this.pilotStatus = pilotStatus;
    }

    public short getInactiveCount() {
        return inactiveCount;
    }

    public void setInactiveCount(short inactiveCount) {
        this.inactiveCount = inactiveCount;
    }

    public boolean isCurrentlyInCampaign() {
        return currentlyInCampaign;
    }

    public void setCurrentlyInCampaign(boolean currentlyInCampaign) {
        this.currentlyInCampaign = currentlyInCampaign;
    }

    public Set<Match1> getMatches() {
        return matches;
    }

    public Pilot(String name, String pilotSurname, String pilotNickname , PilotRank pilotRank_id,
                 PilotStatus pilotStatus_id, Player player) {
        this.name = name;
        this.surname = pilotSurname;
        this.nickname = pilotNickname;
        this.pilotRank = pilotRank_id;
        this.pilotStatus = pilotStatus_id;
        this.player = player;
        this.inactiveCount = 0;
        this.currentlyInCampaign = false;
    }

    public Pilot() {
    }

    public void removeMatch(Match1 match) {
        this.matches.remove(match);
        match.getMechs().remove(this);
    }
}
