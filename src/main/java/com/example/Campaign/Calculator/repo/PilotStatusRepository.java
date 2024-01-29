package com.example.Campaign.Calculator.repo;

import com.example.Campaign.Calculator.models.PilotStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PilotStatusRepository extends CrudRepository<PilotStatus, Long> {
    PilotStatus findByName(String name);
}

