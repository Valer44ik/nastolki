package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Mech {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mech_id;

    private String name;

    private int battleValue;

    @ManyToOne
    @JoinColumn(name = "mechChasis")
    private MechChasis mechChasis;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "mechClass")
    private MechClass mechClass;

    @ManyToOne
    @JoinColumn(name = "mechStatus")
    private MechStatus mechStatus;

    @ManyToMany(mappedBy = "mechs")
    private Set<Match1> matches = new HashSet<>();

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

    public Mech(String name, MechStatus mechStatus, MechClass mechClass,
                int battleValue, User user, MechChasis mechChasis) {
        this.name = name;
        this.mechStatus = mechStatus;
        this.mechClass = mechClass;
        this.battleValue = battleValue;
        this.user = user;
        this.mechChasis = mechChasis;
    }

    public Mech() {

    }
}
