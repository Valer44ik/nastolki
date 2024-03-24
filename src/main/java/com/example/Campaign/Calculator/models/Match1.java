package com.example.Campaign.Calculator.models;

import com.example.Campaign.Calculator.repo.MatchRepository;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Match1 {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long match_id;

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

    public Match1(LocalDate startDate, String name, boolean isEnded, Campaign campaign) {
        this.startDate = startDate;
        this.name = name;
        this.isEnded = isEnded;
        this.campaign = campaign;
    }

    public Match1(){};

}
