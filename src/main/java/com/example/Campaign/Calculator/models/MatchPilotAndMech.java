package com.example.Campaign.Calculator.models;

import com.example.Campaign.Calculator.repo.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;


public class MatchPilotAndMech {
    private Mech mech;
    private  Pilot pilot;

    public Mech getMech() {
        return mech;
    }

    public Pilot getPilot() {
        return pilot;
    }

    public MatchPilotAndMech(Pilot pilot, Mech mech) {
        this.pilot = pilot;
        this.mech = mech;
    }
}
