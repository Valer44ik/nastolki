package com.example.Campaign.Calculator;

import com.example.Campaign.Calculator.Controllers.matchController;
import com.example.Campaign.Calculator.models.*;
import com.example.Campaign.Calculator.repo.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(matchController.class)
public class MatchTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CampaignRepository campaignRepository;

    @MockBean
    private PilotRepository pilotRepository;

    @MockBean
    private PilotStatusRepository pilotStatusRepository;

    @MockBean
    private MechRepository mechRepository;

    @MockBean
    private MechChasisRepository mechChasisRepository;

    @MockBean
    private MatchRepository matchRepository;

    @MockBean
    private MainTaskRepository mainTaskRepository;

    @MockBean
    private SecondaryTaskRepository secondaryTaskRepository;

    @MockBean
    private PlayerRepository playerRepository;

    @Test
    public void testCreateFirstMatch_withMissingFields() throws Exception {
        mockMvc.perform(post("/startFirstMatch")
                        .param("matchName", "")
                        .param("firstPlayerPilots_id", "")
                        .param("firstPlayerMechs_id", "")
                        .param("secondPlayerPilots_id", "")
                        .param("secondPlayerMechs_id", "")
                        .param("mainTasksTextForPlayer1", "")
                        .param("secondaryTasksTextForPlayer1", "")
                        .param("mainTasksTextForPlayer2", "")
                        .param("secondaryTasksTextForPlayer2", "")
                        .param("campaign_id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/startFirstMatch"))
                .andExpect(flash().attributeExists("error"))
                .andExpect(flash().attribute("error", "Incorrect match details, please try again"));
    }

    @Test
    public void testCreateFirstMatch_withValidData() throws Exception {
        // Arrange
        Long campaignId = 1L;
        Campaign campaign = Mockito.mock(Campaign.class);
        Mockito.when(campaign.getCampaign_id()).thenReturn(campaignId);

        Player player1 = Mockito.mock(Player.class);
        Player player2 = Mockito.mock(Player.class);

        List<Player> players = Arrays.asList(player1, player2);

        Mockito.when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaign));
        Mockito.when(campaignRepository.findPlayersByCampaign(campaign)).thenReturn(players);

        Match1 match = Mockito.mock(Match1.class);
        Mockito.when(match.getMatch_id()).thenReturn(1L);

        Mockito.when(matchRepository.save(Mockito.any(Match1.class))).thenReturn(match);

        // Mocking pilot and mech retrieval and status updates
        Pilot pilot1 = new Pilot();
        Pilot pilot2 = new Pilot();
        Pilot pilot3 = new Pilot();
        Pilot pilot4 = new Pilot();
        Mech mech1 = new Mech();
        Mech mech2 = new Mech();
        Mech mech3 = new Mech();
        Mech mech4 = new Mech();

        Mockito.when(pilotRepository.findById(1L)).thenReturn(Optional.of(pilot1));
        Mockito.when(pilotRepository.findById(2L)).thenReturn(Optional.of(pilot2));
        Mockito.when(pilotRepository.findById(3L)).thenReturn(Optional.of(pilot3));
        Mockito.when(pilotRepository.findById(4L)).thenReturn(Optional.of(pilot4));
        Mockito.when(mechRepository.findById(1L)).thenReturn(Optional.of(mech1));
        Mockito.when(mechRepository.findById(2L)).thenReturn(Optional.of(mech2));
        Mockito.when(mechRepository.findById(3L)).thenReturn(Optional.of(mech3));
        Mockito.when(mechRepository.findById(4L)).thenReturn(Optional.of(mech4));

        // Act and Assert
        mockMvc.perform(post("/startFirstMatch")
                        .param("matchName", "Test Match")
                        .param("firstPlayerPilots_id", "1", "2")
                        .param("firstPlayerMechs_id", "1", "2")
                        .param("secondPlayerPilots_id", "3", "4")
                        .param("secondPlayerMechs_id", "3", "4")
                        .param("mainTasksTextForPlayer1", "Task1", "Task2")
                        .param("secondaryTasksTextForPlayer1", "Task1", "Task2")
                        .param("mainTasksTextForPlayer2", "Task1", "Task2")
                        .param("secondaryTasksTextForPlayer2", "Task1", "Task2")
                        .param("campaign_id", campaignId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/playMatch"))
                .andExpect(flash().attributeExists("campaign_id"))
                .andExpect(flash().attribute("campaign_id", campaignId));

        // Verify repository interactions
        Mockito.verify(matchRepository).save(Mockito.any(Match1.class));
        Mockito.verify(pilotRepository).save(pilot1);
        Mockito.verify(pilotRepository).save(pilot2);
        Mockito.verify(pilotRepository).save(pilot3);
        Mockito.verify(pilotRepository).save(pilot4);
        Mockito.verify(mechRepository).save(mech1);
        Mockito.verify(mechRepository).save(mech2);
        Mockito.verify(mechRepository).save(mech3);
        Mockito.verify(mechRepository).save(mech4);
    }

    @Test
    public void testEndMatch_withInvalidData() throws Exception {
        mockMvc.perform(post("/endMatch")
                        .param("winningPlayer_id", "0")
                        .param("campaign_id", "0")
                        .param("match_id", "0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/endMatch"))
                .andExpect(flash().attributeExists("error"))
                .andExpect(flash().attribute("error", "Incorrect details, please try again"));
    }

    @Test
    public void testEndMatch_withValidData() throws Exception {
        // Arrange
        Long campaignId = 1L;
        Long matchId = 1L;
        Long winningPlayerId = 1L;
        Long lostPlayerId = 2L;

        Campaign campaign = Mockito.mock(Campaign.class);
        Match1 match = Mockito.mock(Match1.class);
        Player winningPlayer = Mockito.mock(Player.class);
        Player lostPlayer = Mockito.mock(Player.class);

        Mockito.when(campaign.getCampaign_id()).thenReturn(campaignId);
        Mockito.when(match.getMatch_id()).thenReturn(matchId);
        Mockito.when(winningPlayer.getPlayer_id()).thenReturn(winningPlayerId);
        Mockito.when(lostPlayer.getPlayer_id()).thenReturn(lostPlayerId);

        Mockito.when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaign));
        Mockito.when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));

        List<Player> players = Arrays.asList(winningPlayer, lostPlayer);
        Mockito.when(campaignRepository.findPlayersByCampaign(campaign)).thenReturn(players);

        List<Long> pilotIds = Arrays.asList(1L, 2L);
        Mockito.when(pilotRepository.findPilotsParticipatingInCampaign(campaignId)).thenReturn(pilotIds);

        Pilot pilot1 = Mockito.mock(Pilot.class);
        Pilot pilot2 = Mockito.mock(Pilot.class);

        PilotStatus injuredStatus = new PilotStatus("Injured");
        PilotStatus readyStatus = new PilotStatus("Ready");

        Mockito.when(pilot1.getPilotStatus()).thenReturn(injuredStatus);
        Mockito.when(pilot1.getInactiveCount()).thenReturn((short) 1);
        Mockito.when(pilot2.getPilotStatus()).thenReturn(injuredStatus);
        Mockito.when(pilot2.getInactiveCount()).thenReturn((short) 0);
        Mockito.when(pilotStatusRepository.findByName("Ready")).thenReturn(readyStatus);

        Mockito.when(pilotRepository.findById(1L)).thenReturn(Optional.of(pilot1));
        Mockito.when(pilotRepository.findById(2L)).thenReturn(Optional.of(pilot2));

        // Act and Assert
        mockMvc.perform(post("/endMatch")
                        .param("winningPlayer_id", winningPlayerId.toString())
                        .param("campaign_id", campaignId.toString())
                        .param("match_id", matchId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mainPage"));

        // Verify interactions
        Mockito.verify(playerRepository).incrementWinMatchByOne(winningPlayerId);
        Mockito.verify(playerRepository).incrementMatchByOne(lostPlayerId);
        Mockito.verify(match).setEnded(true);
        Mockito.verify(match).setWinningPlayer_id(winningPlayerId);
        Mockito.verify(matchRepository).save(match);
        Mockito.verify(pilotRepository).save(pilot1);
        Mockito.verify(pilotRepository).save(pilot2);
    }
}
