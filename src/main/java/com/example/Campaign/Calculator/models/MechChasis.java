package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class MechChasis {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mechChasis_id;

    @OneToMany(mappedBy = "mechChasis")
    private Set<Mech> mechs = new HashSet<>();

    private String chasisName, description;

    private int chasisWeight;

    public Long getMechChasis_id() {
        return mechChasis_id;
    }

    public String getChasisName() {
        return chasisName;
    }

    public int getChasisWeight() {
        return chasisWeight;
    }

    public void setChasisWeight(int chasisWeight) {
        this.chasisWeight = chasisWeight;
    }

    public MechChasis(String chasisName, int chasisWeight) {
        this.chasisName = chasisName;
        this.chasisWeight = chasisWeight;
    }

    public MechChasis() {

    }
}
