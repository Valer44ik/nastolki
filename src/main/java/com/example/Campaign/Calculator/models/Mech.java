package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

@Entity
public class Mech {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mech_id;

    private String name;

    private int battleValue, weight;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pilot")
    private Pilot pilot;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mechStatus")
    private MechStatus mechStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mechChasis")
    private MechChasis mechChasis;

    @ManyToOne
    @JoinColumn(name = "mechClass")
    private MechClass mechClass;

    @ManyToOne
    @JoinColumn(name = "campaign")
    private Campaign campaign;

    public void setPilot(Pilot pilot) {
        this.pilot = pilot;
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

    public MechStatus getMechStatus_id() {
        return mechStatus;
    }

    public MechChasis getMechChasis() {
        return mechChasis;
    }

    public Mech(String name, MechStatus mechStatus, MechClass mechClass, int battleValue, int weight) {
        this.name = name;
        this.mechStatus = mechStatus;
        this.mechClass = mechClass;
        this.battleValue = battleValue;
        this.weight = weight;
    }

    public Mech() {

    }
}
