package com.example.Campaign.Calculator.repo;

import com.example.Campaign.Calculator.models.Pilot;
import com.example.Campaign.Calculator.models.Player;
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
    @Modifying
    @Transactional
    @Query(value = "SELECT m.pilot_id FROM match_pilot_mech m WHERE m.match_id = :match_id", nativeQuery = true)
    List<Long> findPilotsByMatchId(@Param("match_id") Long match_id);

    @Modifying
    @Transactional
    @Query(value = "SELECT p.pilot_id FROM pilot p " +
            "WHERE p.player = :player_id " +
            "AND p.pilot_status = 1 " +
            "AND p.currently_in_campaign = 0 ", nativeQuery = true)
    List<Long> findPilotsReadyForMatch(@Param("player_id") Long player_id);


    @Query(value = "SELECT DISTINCT p.pilot_id FROM pilot p " +
            " WHERE p.pilot_id IN " +
            "   (SELECT DISTINCT mpm.pilot_id FROM match_pilot_mech mpm " +
            "   WHERE mpm.match_id IN " +
            "       (SELECT DISTINCT m.match_id FROM match1 m " +
            "       WHERE m.campaign = :campaign_id " +
            "       ) " +
            "   ) " +
            "AND p.pilot_status = 1 " +
            "AND p.player = :player_id", nativeQuery = true)
    List<Long> findReadyPilotsParticipatingInCampaign(@Param("campaign_id") Long campaign_id,
                                                 @Param("player_id") Long player_id);

    @Query(value = "SELECT DISTINCT p.pilot_id FROM pilot p " +
            " WHERE p.pilot_id IN " +
            "   (SELECT DISTINCT mpm.pilot_id FROM match_pilot_mech mpm " +
            "   WHERE mpm.match_id IN " +
            "       (SELECT DISTINCT m.match_id FROM match1 m " +
            "       WHERE m.campaign = :campaign_id " +
            "       ) " +
            "   ) ", nativeQuery = true)
    List<Long> findPilotsParticipatingInCampaign(@Param("campaign_id") Long campaign_id);
}

