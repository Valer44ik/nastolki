package com.example.Campaign.Calculator.repo;

import com.example.Campaign.Calculator.models.MechChasis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MechChasisService {

    private final MechChasisRepository mechChasisRepository;

    @Autowired
    public MechChasisService(MechChasisRepository mechChasisRepository) {
        this.mechChasisRepository = mechChasisRepository;
    }

    public List<MechChasis> getAllMechChasis() {
        return (List<MechChasis>) mechChasisRepository.findAll();
    }
}
