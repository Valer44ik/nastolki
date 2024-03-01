package com.example.Campaign.Calculator.repo;

import com.example.Campaign.Calculator.models.Match1;
import com.example.Campaign.Calculator.models.Pilot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PilotRepository extends CrudRepository<Pilot, Long> {
    List<Pilot> findByMatch(Match1 match);
}

