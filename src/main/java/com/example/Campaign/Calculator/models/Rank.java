package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Rank {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long rank_id;

    @OneToMany(mappedBy = "rank_id")
    private Set<Pilot> pilots = new HashSet<>();
}
