package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

@Entity
@Table(name = "pilot")
public class Pilot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pilot_id")
    private Long pilot_id;

    private String name, nickname, surname;

    private boolean hasMech;

    @ManyToOne
    @JoinColumn(name = "pilotRank_id")
    private PilotRank pilotRank_id;

    @ManyToOne
    @JoinColumn(name = "pilotStatus_id")
    private PilotStatus pilotStatus_id;

    @OneToOne(mappedBy = "pilot_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Mech mech;

    public Long getPilot_id() {
        return pilot_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHasMech(boolean hasMech) {
        this.hasMech = hasMech;
    }

    public Pilot(String name, String pilotSurname, String pilotNickname , PilotRank pilotRank_id,
                 PilotStatus pilotStatus_id) {
        this.name = name;
        this.surname = pilotSurname;
        this.nickname = pilotNickname;
        this.pilotRank_id = pilotRank_id;
        this.pilotStatus_id = pilotStatus_id;
        hasMech = false;
    }

    public Pilot() {
    }
}
