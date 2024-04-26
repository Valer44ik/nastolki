package com.example.Campaign.Calculator.repo;

import com.example.Campaign.Calculator.models.Mech;
import com.example.Campaign.Calculator.models.Pilot;
import com.example.Campaign.Calculator.models.Player;
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
    List<Mech> findByPlayer(Player player);

    @Modifying
    @Transactional
    @Query(value =  "SELECT m.mech_id FROM match_pilot_mech m " +
                    " WHERE m.match_id = :match_id " +
                    "  AND m.pilot_id = :pilot_id", nativeQuery = true)
    List<Long> findMechByMatchAndPilot_id(@Param("match_id") Long match_id, @Param("pilot_id") Long pilot_id);

    @Modifying
    @Transactional
    @Query(value = "SELECT m.mech_id FROM mech m " +
            "WHERE m.player = :player_id " +
            "AND m.mech_status = 1 " +
            "AND m.currently_in_campaign = 0", nativeQuery = true)
    List<Long> findMechsReadyForMatch(@Param("player_id") Long player_id);

    @Query(value = "SELECT DISTINCT m.mech_id FROM mech m " +
            " WHERE m.mech_id IN " +
            "   (SELECT DISTINCT mpm.mech_id FROM match_pilot_mech mpm " +
            "   WHERE mpm.match_id IN " +
            "       (SELECT DISTINCT m.match_id FROM match1 m " +
            "       WHERE m.campaign = :campaign_id " +
            "       ) " +
            "   ) " +
            "AND m.mech_status = 1 " +
            "AND m.player = :player_id", nativeQuery = true)
    List<Long> findReadyMechsParticipatingInCampaign(@Param("campaign_id") Long campaign_id,
                                                 @Param("player_id") Long player_id);

    @Query(value = "SELECT DISTINCT m.mech_id FROM mech m " +
            " WHERE m.mech_id IN " +
            "   (SELECT DISTINCT mpm.mech_id FROM match_pilot_mech mpm " +
            "   WHERE mpm.match_id IN " +
            "       (SELECT DISTINCT m.match_id FROM match1 m " +
            "       WHERE m.campaign = :campaign_id " +
            "       ) " +
            "   ) ", nativeQuery = true)
    List<Long> findMechsParticipatingInCampaign(@Param("campaign_id") Long campaign_id);
}

