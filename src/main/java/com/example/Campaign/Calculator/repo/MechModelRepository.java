package com.example.Campaign.Calculator.repo;

import com.example.Campaign.Calculator.models.MechModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MechModelRepository extends CrudRepository<MechModel, Long> {

}

