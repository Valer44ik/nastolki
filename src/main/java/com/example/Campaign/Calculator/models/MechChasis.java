package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

@Entity
public class MechChasis {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mechChasis_id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mechModel_id")
    private MechModel mechModel_id;

    private String chasisName, description;

    private int chasisWeight;

    public String getChasisName() {
        return chasisName;
    }

    public Long getMechChasis_id() {
        return mechChasis_id;
    }

    public MechChasis(String chasisName, int chasisWeight) {
        this.chasisName = chasisName;
        this.chasisWeight = chasisWeight;
    }

    public MechChasis() {

    }
}
