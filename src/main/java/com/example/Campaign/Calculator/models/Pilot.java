package com.example.Campaign.Calculator.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

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

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match1 match_id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "playerPilot",
            joinColumns = @JoinColumn(name = "pilot_id", referencedColumnName = "pilot_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"))
    private Set<User> users = new HashSet<>();

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

    public void setUser(User user) {
        users.add(user);
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
