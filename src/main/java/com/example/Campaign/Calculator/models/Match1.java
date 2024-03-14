package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

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
    private Set<Pilot> pilots = new HashSet<>();

    @OneToMany(mappedBy = "match")
    private Set<MainTask> mainTasks = new HashSet<>();

    @OneToMany(mappedBy = "match")
    private Set<SecondaryTask> secondaryTasks = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "matchPlayer",
            joinColumns = @JoinColumn(name = "match_id", referencedColumnName = "match_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"))
    private Set<User> users = new HashSet<>();

    public LocalDate getStartDate() {
        return startDate;
    }

    public String getName() {
        return name;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public Match1(LocalDate startDate, String name, boolean isEnded, Campaign campaign) {
        this.startDate = startDate;
        this.name = name;
        this.isEnded = isEnded;
        this.campaign = campaign;
    }

    public Match1(){};

}
