package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class MechClass {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mechClass_id;

    @OneToMany(mappedBy = "mechClass")
    private Set<Mech> mechs = new HashSet<>();

    private String className;
    private int minWeight, maxWeight;

    public MechClass(String className, int minWeight, int maxWeight) {
        this.className = className;
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
    }

    public MechClass() {
    }
}
