package com.example.Campaign.Calculator.repo;

import com.example.Campaign.Calculator.models.PilotRank;
import com.example.Campaign.Calculator.models.PilotStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PilotRankRepository extends CrudRepository<PilotRank, Long> {
    PilotRank findByName(String name);
}

