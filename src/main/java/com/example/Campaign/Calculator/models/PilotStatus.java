package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class PilotStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pilotStatus_id")
    private Long pilotStatus_id;

    @OneToMany(mappedBy = "pilotStatus")
    private Set<Pilot> pilots = new HashSet<>();

    private String name;
    private String health;

    public PilotStatus() {
    }

    public PilotStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getPilotStatus_id() {
        return pilotStatus_id;
    }

}
