package com.example.Campaign.Calculator.repo;

import com.example.Campaign.Calculator.models.Campaign;
import com.example.Campaign.Calculator.models.Match1;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends CrudRepository<Match1, Long> {
    List<Match1> findByCampaign(Campaign campaign);
}
