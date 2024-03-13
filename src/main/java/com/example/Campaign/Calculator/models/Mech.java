package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

@Entity
public class Mech {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mech_id;

    private String name;

    private int battleValue, weight;

    @OneToOne(mappedBy = "mech", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MechModel mechModel;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pilot_id")
    private Pilot pilot_id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mechStatus_id")
    private MechStatus mechStatus_id;

    @ManyToOne
    @JoinColumn(name = "campaign")
    private Campaign campaign;


    public void setPilot(Pilot pilot_id) {
        this.pilot_id = pilot_id;
    }

    public String getName() {
        return name;
    }

    public int getBattleValue() {
        return battleValue;
    }

    public Long getMech_id() {
        return mech_id;
    }

    public MechModel getMechModel() {
        return mechModel;
    }

    public MechModel getMechModel_id() {
        return mechModel;
    }

    public MechStatus getMechStatus_id() {
        return mechStatus_id;
    }

    public Mech(String name, MechStatus mechStatus_id, int battleValue) {
        this.name = name;
        this.mechStatus_id = mechStatus_id;
        this.battleValue = battleValue;
    }

    public Mech() {

    }
}
