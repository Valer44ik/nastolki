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

    private String name, nickname, surname;

    @ManyToOne
    @JoinColumn(name = "pilotRank")
    private PilotRank pilotRank;

    @ManyToOne
    @JoinColumn(name = "pilotStatus")
    private PilotStatus pilotStatus;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToMany(mappedBy = "pilots")
    private Set<Match1> matches = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public Long getPilot_id() {
        return pilot_id;
    }

    public Pilot(String name, String pilotSurname, String pilotNickname , PilotRank pilotRank_id,
                 PilotStatus pilotStatus_id, User user) {
        this.name = name;
        this.surname = pilotSurname;
        this.nickname = pilotNickname;
        this.pilotRank = pilotRank_id;
        this.pilotStatus = pilotStatus_id;
        this.user = user;
    }

    public Pilot() {
    }
}
