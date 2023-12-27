package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

@Entity
public class Mech {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mech_id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mechModel_id")
    private MechModel mechModel_id;

    public Mech(Long mech_id, MechModel mechModel_id, MechStatus mechStatus_id, int battleValue) {
        this.mech_id = mech_id;
        this.mechModel_id = mechModel_id;
        this.mechStatus_id = mechStatus_id;
        this.battleValue = battleValue;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mechStatus_id")
    private MechStatus mechStatus_id;

    private int battleValue;

    public Mech() {

    }
}
