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
    @JoinColumn(name = "pilotRank")
    private PilotRank pilotRank;

    @ManyToOne
    @JoinColumn(name = "pilotStatus")
    private PilotStatus pilotStatus;

    @ManyToOne
    @JoinColumn(name = "match1")
    private Match1 match1;

    @ManyToOne
    @JoinColumn(name = "campaign")
    private Campaign campaign;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "playerPilot",
            joinColumns = @JoinColumn(name = "pilot_id", referencedColumnName = "pilot_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"))
    private Set<User> users = new HashSet<>();

    @OneToOne(mappedBy = "pilot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Mech mech;

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

    public Mech getMech() {
        return mech;
    }

    public Long getPilot_id() {
        return pilot_id;
    }

    public void setMatch(Match1 match1) {
        this.match1 = match1;
    }

    public Pilot(String name, String pilotSurname, String pilotNickname , PilotRank pilotRank_id,
                 PilotStatus pilotStatus_id) {
        this.name = name;
        this.surname = pilotSurname;
        this.nickname = pilotNickname;
        this.pilotRank = pilotRank_id;
        this.pilotStatus = pilotStatus_id;
        this.hasMech = false;
    }

    public Pilot() {
    }
}
