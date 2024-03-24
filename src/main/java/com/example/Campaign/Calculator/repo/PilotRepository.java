package com.example.Campaign.Calculator.repo;


import com.example.Campaign.Calculator.models.Match1;
import com.example.Campaign.Calculator.models.Pilot;
import com.example.Campaign.Calculator.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PilotRepository extends CrudRepository<Pilot, Long> {
    List<Pilot> findByUser(User user);

    @Modifying
    @Transactional
    @Query(value = "SELECT m.pilot_id FROM match_pilot_mech m WHERE m.match_id = :match_id", nativeQuery = true)
    List<Long> findPilotsByMatchId(@Param("match_id") Long match_id);
}

