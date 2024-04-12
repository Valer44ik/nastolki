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
    List<Pilot> findByPlayer(Player player);

    @Modifying
    @Transactional
    @Query(value = "SELECT m.pilot_id FROM match_pilot_mech m WHERE m.match_id = :match_id", nativeQuery = true)
    List<Long> findPilotsByMatchId(@Param("match_id") Long match_id);

    /*
    @Modifying
    @Transactional
    @Query(value = "SELECT m.pilot_id FROM match_pilot_mech m " +
            "INNER JOIN match1 ON match1.match_id = m.match_id " +
            "AND match1.is_Ended = 0 " +
            "INNER JOIN pilot p ON p.pilot_id = m.pilot_id " +
            "AND p.user = :user_id " +
            "AND p.pilot_status = 'Ready' " +
            "WHERE m.match_id = :match_id", nativeQuery = true)
    List<Long> findPilotsReadyForMatch(@Param("user_id") Long user_id);
     */
    @Modifying
    @Transactional
    @Query(value = "SELECT p.pilot_id FROM pilot p " +
            "WHERE p.player = :player_id " +
            "AND p.pilot_status = 1 ", nativeQuery = true)
    List<Long> findPilotsReadyForMatch(@Param("player_id") Long player_id);
}

