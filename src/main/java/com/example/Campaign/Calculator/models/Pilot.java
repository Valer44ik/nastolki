package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

@Entity
@Table(name = "pilot")
public class Pilot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pilot_id")
    private Long pilot_id;

    private String name, nickname, surname;

    @ManyToOne
    @JoinColumn(name = "pilotRank_id")
    private PilotRank pilotRank_id;

    @ManyToOne
    @JoinColumn(name = "pilotStatus_id")
    private PilotStatus pilotStatus_id;

    public Long getPilot_id() {
        return pilot_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PilotRank getRank_id() {
        return pilotRank_id;
    }

    public void setRank_id(PilotRank rank_id) {
        this.pilotRank_id = rank_id;
    }

    public PilotStatus getStatus_id() {
        return pilotStatus_id;
    }

    public void setStatus_id(PilotStatus status_id) {
        this.pilotStatus_id = status_id;
    }

    public Pilot(String name, String pilotSurname, String pilotNickname ,PilotRank pilotRank_id,
                 PilotStatus pilotStatus_id) {
        this.name = name;
        this.surname = pilotSurname;
        this.nickname = pilotNickname;
        this.pilotRank_id = pilotRank_id;
        this.pilotStatus_id = pilotStatus_id;
    }

    public Pilot() {
    }
}
