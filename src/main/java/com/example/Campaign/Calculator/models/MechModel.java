package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

@Entity
public class MechModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mechModel_id;

    @OneToOne(mappedBy = "mechModel_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Mech mech;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mechChasis_id")
    private MechChasis mechChasis_id;

    @ManyToOne
    @JoinColumn(name = "mechClass_id")
    private MechClass mechClass_id;

    private String modelName, description;

    private int modelWeight;

    public MechModel(String modelName, int modelWeight) {
        this.modelName = modelName;
        this.modelWeight = modelWeight;
    }

    public MechModel() {

    }

    public Long getMechModel_id() {
        return mechModel_id;
    }


}
