package com.example.Campaign.Calculator.repo;

import com.example.Campaign.Calculator.models.Mech;
import com.example.Campaign.Calculator.models.Pilot;
import com.example.Campaign.Calculator.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MechRepository extends CrudRepository<Mech, Long> {
    List<Mech> findByUser(User user);

    @Modifying
    @Transactional
    @Query(value =  "SELECT m.mech_id FROM match_pilot_mech m " +
                    " WHERE m.match_id = :match_id " +
                    "  AND m.pilot_id = :pilot_id", nativeQuery = true)
    List<Long> findMechByMatchAndPilot_id(@Param("match_id") Long match_id, @Param("pilot_id") Long pilot_id);
}

