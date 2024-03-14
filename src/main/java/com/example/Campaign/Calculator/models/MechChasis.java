package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

@Entity
public class MechChasis {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mechChasis_id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mech")
    private Mech mech;

    private String chasisName, description;

    private int chasisWeight;

    public Long getMechChasis_id() {
        return mechChasis_id;
    }

    public String getChasisName() {
        return chasisName;
    }

    public void setMech(Mech mech) {
        this.mech = mech;
    }

    public Mech getMech() {
        return mech;
    }

    public MechChasis(String chasisName, int chasisWeight) {
        this.chasisName = chasisName;
        this.chasisWeight = chasisWeight;
    }

    public MechChasis() {

    }
}
