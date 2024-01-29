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

    public MechClass getMechClass() {
        return mechClass;
    }

    public MechStatus(Long mechStatus_id, String name, MechClass mechClass) {
        this.mechStatus_id = mechStatus_id;
        this.name = name;
        this.mechClass = mechClass;
    }
}
