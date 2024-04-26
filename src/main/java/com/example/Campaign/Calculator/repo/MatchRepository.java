package com.example.Campaign.Calculator.repo;

import com.example.Campaign.Calculator.models.Campaign;
import com.example.Campaign.Calculator.models.Match1;
import com.example.Campaign.Calculator.models.Mech;
import com.example.Campaign.Calculator.models.Pilot;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MatchRepository extends CrudRepository<Match1, Long> {
    List<Match1> findByCampaign(Campaign campaign);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO match_pilot_mech(match_id, pilot_id, mech_id) VALUES (?, ?, ?)", nativeQuery = true)
    void bindPilotsAndMechsToMatch(Long match_id, Long pilot_id, Long mech_id);

    @Transactional
    @Query(value = "SELECT winning_player_id, COUNT(1) cnt " +
            "FROM match1 m WHERE m.campaign = :campaign_id " +
            "GROUP BY winning_player_id", nativeQuery = true)
    List<Map<String, Long>> countWinsForPlayer(@Param("campaign_id") Long campaign_id);

    @Transactional
    @Query(value =  "SELECT m.pilot_id, m.mech_id FROM match_pilot_mech m " +
                    " WHERE m.match_id = :match_id", nativeQuery = true)
    Map<Pilot, Mech> findMechAndPilotById(@Param("match_id") Long match_id);

}
