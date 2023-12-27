package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

@Entity
public class MechStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mechStatus_id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "mechClass_id")
    private MechClass mechClass;
}
