package com.example.Campaign.Calculator.repo;

import com.example.Campaign.Calculator.models.Mech;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MechRepository extends CrudRepository<Mech, Long> {

}

