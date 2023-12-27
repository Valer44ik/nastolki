package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "status_id")
    private Long status_id;

    @OneToMany(mappedBy = "status_id")
    private Set<Pilot> pilots = new HashSet<>();
}
