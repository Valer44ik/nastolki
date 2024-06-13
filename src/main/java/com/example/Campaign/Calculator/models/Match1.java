package com.example.Campaign.Calculator.models;

import com.example.Campaign.Calculator.repo.MatchRepository;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Match1 {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long match_id;

    private Long winningPlayer_id;

    private LocalDate startDate, endDate;

    private String notes, name;

    private boolean isEnded;

    @ManyToOne
    @JoinColumn(name = "campaign")
    private Campaign campaign;

    @OneToMany(mappedBy = "match1")
    private Set<MainTask> mainTasks = new HashSet<>();

    @OneToMany(mappedBy = "match1")
    private Set<SecondaryTask> secondaryTasks = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "match_pilot_mech",
            joinColumns = @JoinColumn(name = "match_id"),
            inverseJoinColumns = @JoinColumn(name = "pilot_id"))
    private Set<Pilot> pilots = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "match_pilot_mech",
            joinColumns = @JoinColumn(name = "match_id"),
            inverseJoinColumns = @JoinColumn(name = "mech_id"))
    private Set<Mech> mechs = new HashSet<>();

    public LocalDate getStartDate() {
        return startDate;
    }

    public String getName() {
        return name;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public Long getMatch_id() {
        return match_id;
    }

    public void setPilots(Set<Pilot> pilots) {
        this.pilots = pilots;
    }

    public void setMechs(Set<Mech> mechs) {
        this.mechs = mechs;
    }

    public Long getWinningPlayer_id() {
        return winningPlayer_id;
    }

    public void setWinningPlayer_id(Long winningPlayer_id) {
        this.winningPlayer_id = winningPlayer_id;
    }

    public void setEnded(boolean ended) {
        isEnded = ended;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public Set<Pilot> getPilots() {
        return pilots;
    }

    public Set<Mech> getMechs() {
        return mechs;
    }

    public Match1(LocalDate startDate, String name, boolean isEnded, Campaign campaign) {
        this.startDate = startDate;
        this.name = name;
        this.isEnded = isEnded;
        this.campaign = campaign;
        winningPlayer_id = null;
    }

    public Match1(Campaign campaign) {
        this.campaign = campaign;
        winningPlayer_id = null;
    }

    public Match1(){};

    public void removeMainTasks(MainTask mainTask){
        mainTasks.remove(mainTask);
        mainTask.setMatch1(null);
    }

    public void removeSecondaryTasks(SecondaryTask secondaryTask) {
        secondaryTasks.remove(secondaryTask);
        secondaryTask.setMatch1(null);
    }

    public void removePilot(Pilot pilot) {
        this.pilots.remove(pilot);
        pilot.getMatches().remove(this);
    }

    public void removeMech(Mech mech) {
        this.mechs.remove(mech);
        mech.getMatches().remove(this);
    }
}
