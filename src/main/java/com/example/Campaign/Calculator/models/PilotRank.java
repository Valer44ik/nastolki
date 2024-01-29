package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class PilotRank {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pilotRank_id;

    @OneToMany(mappedBy = "pilotRank_id")
    private Set<Pilot> pilots = new HashSet<>();

    private String name;

    public PilotRank(String name) {
        this.name = name;
    }

    public Long getPilotRank_id() {
        return pilotRank_id;
    }

    public String getName() {
        return name;
    }

    public PilotRank(Long pilotRank_id, String name) {
        this.pilotRank_id = pilotRank_id;
        this.name = name;
    }

    public PilotRank() {
    }
}
