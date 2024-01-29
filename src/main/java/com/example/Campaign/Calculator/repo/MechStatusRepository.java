package com.example.Campaign.Calculator.repo;

import com.example.Campaign.Calculator.models.MechStatus;
import com.example.Campaign.Calculator.models.PilotStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MechStatusRepository extends CrudRepository<MechStatus, Long> {
    MechStatus findByName(String name);
}

