package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class MechClass {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mechClass_id;

    @OneToMany(mappedBy = "mechClass_id")
    private Set<MechModel> mechModels = new HashSet<>();

    private String className;
    private int minWeight, maxWeight;
}
