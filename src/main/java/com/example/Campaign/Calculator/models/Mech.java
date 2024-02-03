package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

@Entity
public class Mech {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mech_id;

    private String name;

    private int battleValue, weight;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mechModel_id")
    private MechModel mechModel_id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pilot_id")
    private Pilot pilot_id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mechStatus_id")
    private MechStatus mechStatus_id;

    public void setPilot(Pilot pilot_id) {
        this.pilot_id = pilot_id;
    }

    public String getName() {
        return name;
    }

    public Long getMech_id() {
        return mech_id;
    }

    public Mech(String name, MechStatus mechStatus_id, MechModel mechModel_id, int battleValue) {
        this.name = name;
        this.mechStatus_id = mechStatus_id;
        this.mechModel_id = mechModel_id;
        this.battleValue = battleValue;
    }

    public Mech() {

    }
}
