package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class MechStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mechStatus_id;

    private String name;

    @OneToMany(mappedBy = "mechStatus")
    private Set<Mech> mechs = new HashSet<>();
    public MechStatus(String name) {
        this.name = name;
    }

    public MechStatus() {

    }

    public String getName() {
        return name;
    }

    public Long getMechStatus_id() {
        return mechStatus_id;
    }


    public MechStatus(Long mechStatus_id, String name) {
        this.mechStatus_id = mechStatus_id;
        this.name = name;
    }
}
