package com.example.Campaign.Calculator.models;

import com.example.Campaign.Calculator.repo.PilotRepository;
import com.example.Campaign.Calculator.repo.PilotService;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Entity
@Table(name = "pilot")
public class Pilot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pilot_id")
    private Long pilot_id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "rank_id")
    private Rank rank_id;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status_id;

    public Long getPilot_id() {
        return pilot_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Rank getRank_id() {
        return rank_id;
    }

    public void setRank_id(Rank rank_id) {
        this.rank_id = rank_id;
    }

    public Status getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Status status_id) {
        this.status_id = status_id;
    }

    public Pilot(String name, Rank rank_id, Status status_id) {
        this.name = name;
        this.rank_id = rank_id;
        this.status_id = status_id;
    }

    public Pilot() {
    }
}
