package com.example.Campaign.Calculator.repo;

import com.example.Campaign.Calculator.models.Campaign;
import com.example.Campaign.Calculator.models.Match1;
import com.example.Campaign.Calculator.models.Player;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {
    Player findByNickname(String nickname);

    @Modifying
    @Transactional
    @Query(value = "UPDATE player p" +
            " SET p.games_won = p.games_won + 1," +
            " p.games_total = p.games_total + 1" +
            " WHERE p.player_id = :player_id", nativeQuery = true)
    void incrementWinMatchByOne(@Param("player_id") Long player_id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE player p" +
            " SET p.games_total = p.games_total + 1" +
            " WHERE p.player_id = :player_id", nativeQuery = true)
    void incrementMatchByOne(@Param("player_id") Long player_id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE player p" +
            " SET p.campaigns_won = p.campaigns_won + 1," +
            " p.campaigns_total = p.campaigns_total + 1" +
            " WHERE p.player_id = :player_id", nativeQuery = true)
    void incrementWinCampaignByOne(@Param("player_id") Long player_id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE player p" +
            " SET p.campaigns_total = p.campaigns_total + 1" +
            " WHERE p.player_id = :player_id", nativeQuery = true)
    void incrementCampaignByOne(@Param("player_id") Long player_id);
}

