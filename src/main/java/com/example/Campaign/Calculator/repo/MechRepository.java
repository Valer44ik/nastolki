package com.example.Campaign.Calculator.repo;

import com.example.Campaign.Calculator.models.Campaign;
import com.example.Campaign.Calculator.models.Match1;
import com.example.Campaign.Calculator.models.Mech;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MechRepository extends CrudRepository<Mech, Long> {

    List<Mech> findByCampaign(Campaign campaign);
}

