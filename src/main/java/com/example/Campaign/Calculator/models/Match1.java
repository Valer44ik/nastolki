package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "match_id")
    private Set<Pilot> pilots = new HashSet<>();

    @OneToMany(mappedBy = "match_id")
    private Set<MainTask> mainTasks = new HashSet<>();

    @OneToMany(mappedBy = "match_id")
    private Set<SecondaryTask> secondaryTasks = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "matchPlayer",
            joinColumns = @JoinColumn(name = "match_id", referencedColumnName = "match_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"))
    private Set<User> users = new HashSet<>();

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
