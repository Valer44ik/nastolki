package com.example.Campaign.Calculator.repo;

import com.example.Campaign.Calculator.models.Campaign;
import com.example.Campaign.Calculator.models.Player;
import com.example.Campaign.Calculator.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface CampaignRepository extends CrudRepository<Campaign, Long> {
    @Query("SELECT c.players FROM Campaign c WHERE c = :campaign")
    List<Player> findPlayersByCampaign(@Param("campaign") Campaign campaign);

    List<Campaign> findByUser(User user);
}
