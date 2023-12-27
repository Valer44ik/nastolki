package com.example.Campaign.Calculator.repo;

import com.example.Campaign.Calculator.models.Pilot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface PilotRepository extends CrudRepository<Pilot, Long> {

}

