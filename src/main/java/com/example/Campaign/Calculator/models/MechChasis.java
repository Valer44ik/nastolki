package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

@Entity
public class MechChasis {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mechChasis_id;

    @OneToOne(mappedBy = "mechChasis_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MechModel mechModel;

    private String chasisName, description;

    private int chasisWeight;

    public String getChasisName() {
        return chasisName;
    }

    public MechChasis(String chasisName, int chasisWeight) {
        this.chasisName = chasisName;
        this.chasisWeight = chasisWeight;
    }

    public MechChasis() {

    }
}
