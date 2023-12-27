package com.example.Campaign.Calculator.repo;

import com.example.Campaign.Calculator.models.Pilot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PilotService {

    private final PilotRepository pilotRepository;

    @Autowired
    public PilotService(PilotRepository pilotRepository) {
        this.pilotRepository = pilotRepository;
    }

    public List<Pilot> getAllPilots() {
        return (List<Pilot>) pilotRepository.findAll();
    }
}
