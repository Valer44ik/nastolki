package com.example.Campaign.Calculator.repo;

import com.example.Campaign.Calculator.models.MechChasis;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MechChasisRepository extends CrudRepository<MechChasis, Long> {
    //@Query("SELECT m FROM MechChasis m WHERE m.mech IS NULL")
    //List<MechChasis> selectUnassignedChases();
}

