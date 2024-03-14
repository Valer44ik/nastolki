package com.example.Campaign.Calculator.repo;

import com.example.Campaign.Calculator.models.MechClass;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MechClassRepository extends CrudRepository<MechClass, Long> {
    MechClass findByClassName(String className);
}

