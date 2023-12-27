package com.example.Campaign.Calculator.repo;

import com.example.Campaign.Calculator.models.MechChasis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MechChasisRepository extends CrudRepository<MechChasis, Long> {

}

