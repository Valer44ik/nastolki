package com.example.Campaign.Calculator;

import com.example.Campaign.Calculator.Controllers.campaignController;
import com.example.Campaign.Calculator.models.*;
import com.example.Campaign.Calculator.repo.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(campaignController.class)
public class CampaignTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CampaignRepository campaignRepository;

    @MockBean
    private MatchRepository matchRepository;

    @MockBean
    private PlayerRepository playerRepository;

    @MockBean
    private PilotRepository pilotRepository;

    @MockBean
    private PilotStatusRepository pilotStatusRepository;

    @MockBean
    private MechRepository mechRepository;

    @MockBean
    private MechStatusRepository mechStatusRepository;

    @MockBean
    private MainTaskRepository mainTaskRepository;

    @MockBean
    private SecondaryTaskRepository secondaryTaskRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testCreateCampaign_withValidData() throws Exception {
        // Arrange
        Player firstPlayer = new Player();
        firstPlayer.setPlayer_id(1L);
        Player secondPlayer = new Player();
        secondPlayer.setPlayer_id(2L);
        User loggedInUser = new User();
        loggedInUser.setUser_id(1L); // Assume User has a userId field

        // Mocking repository methods
        Mockito.when(playerRepository.findById(1L)).thenReturn(Optional.of(firstPlayer));
        Mockito.when(playerRepository.findById(2L)).thenReturn(Optional.of(secondPlayer));
        Mockito.when(campaignRepository.save(Mockito.any(Campaign.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Setting up the session
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loggedInUser", loggedInUser);

        // Act and Assert
        mockMvc.perform(post("/startACampaign")
                        .param("campaignName", "New Campaign")
                        .param("campaignType", "custom")
                        .param("formationOrder", "lance")
                        .param("firstPlayer_id", "1")
                        .param("secondPlayer_id", "2")
                        .param("battleValue", "1000")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"));
    }

    @Test
    public void testCreateCampaign_withInvalidData() throws Exception {
        // Arrange
        User loggedInUser = new User();
        loggedInUser.setUser_id(1L); // Assume User has a userId field

        // Setting up the session
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loggedInUser", loggedInUser);

        // Act and Assert
        mockMvc.perform(post("/startACampaign")
                        .param("campaignName", "")
                        .param("campaignType", "custom")
                        .param("formationOrder", "lance")
                        .param("firstPlayer_id", "0")
                        .param("secondPlayer_id", "2")
                        .param("battleValue", "1000")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/startACampaign"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    public void testCreateCampaign_withNoLoggedInUser() throws Exception {
        // Act and Assert
        mockMvc.perform(post("/startACampaign")
                        .param("campaignName", "New Campaign")
                        .param("campaignType", "custom")
                        .param("formationOrder", "lance")
                        .param("firstPlayer_id", "1")
                        .param("secondPlayer_id", "2")
                        .param("battleValue", "1000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    public void testEndCampaign_withValidData() throws Exception {
        // Arrange
        Long campaignId = 1L;
        Campaign campaign = new Campaign();
        campaign.setCampaign_id(campaignId);

        Mockito.when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaign));

        List<Map<String, Long>> playerStats = new ArrayList<>();
        Map<String, Long> playerStat1 = new HashMap<>();
        playerStat1.put("winning_player_id", 1L);
        playerStat1.put("cnt", 3L);
        playerStats.add(playerStat1);

        Map<String, Long> playerStat2 = new HashMap<>();
        playerStat2.put("winning_player_id", 2L);
        playerStat2.put("cnt", 2L);
        playerStats.add(playerStat2);

        Mockito.when(matchRepository.countWinsForPlayer(campaignId)).thenReturn(playerStats);

        List<Long> pilotIds = Arrays.asList(1L, 2L);
        Mockito.when(pilotRepository.findPilotsParticipatingInCampaign(campaignId)).thenReturn(pilotIds);

        Pilot pilot = new Pilot();
        PilotStatus readyStatus = new PilotStatus("Ready");
        Mockito.when(pilotRepository.findById(1L)).thenReturn(Optional.of(pilot));
        Mockito.when(pilotRepository.findById(2L)).thenReturn(Optional.of(pilot));
        Mockito.when(pilotStatusRepository.findByName("Ready")).thenReturn(readyStatus);

        List<Long> mechIds = Arrays.asList(1L, 2L);
        Mockito.when(mechRepository.findMechsParticipatingInCampaign(campaignId)).thenReturn(mechIds);

        Mech mech = new Mech();
        MechStatus readyMechStatus = new MechStatus("Ready");
        Mockito.when(mechRepository.findById(1L)).thenReturn(Optional.of(mech));
        Mockito.when(mechRepository.findById(2L)).thenReturn(Optional.of(mech));
        Mockito.when(mechStatusRepository.findByName("Ready")).thenReturn(readyMechStatus);

        // Act and Assert
        mockMvc.perform(post("/endCampaign")
                        .param("campaign_id", campaignId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mainPage"));

        // Verify updates
        Mockito.verify(campaignRepository).save(campaign);
        Mockito.verify(pilotRepository, Mockito.times(2)).save(pilot);
        Mockito.verify(mechRepository, Mockito.times(2)).save(mech);
    }

    @Test
    public void testEndCampaign_withInvalidCampaignId() throws Exception {
        // Arrange
        Long campaignId = 999L;

        Mockito.when(campaignRepository.findById(campaignId)).thenReturn(Optional.empty());

        // Act and Assert
        mockMvc.perform(post("/endCampaign")
                        .param("campaign_id", campaignId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mainPage"));
    }

    @Test
    public void testDeleteCampaign_withValidData() throws Exception {
        // Arrange
        Long campaignId = 1L;
        Campaign campaign = Mockito.mock(Campaign.class);
        Mockito.when(campaign.getCampaign_id()).thenReturn(campaignId);

        User user = Mockito.mock(User.class);
        Mockito.when(user.getUser_id()).thenReturn(1L);
        Mockito.when(user.getCampaigns()).thenReturn(Collections.singleton(campaign));

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loggedInUser", user);

        List<Match1> matches = new ArrayList<>();
        Match1 match1 = Mockito.mock(Match1.class);
        matches.add(match1);

        List<Player> players = new ArrayList<>();
        Player player = Mockito.mock(Player.class);
        players.add(player);

        List<MainTask> mainTasks = new ArrayList<>();
        List<SecondaryTask> secondaryTasks = new ArrayList<>();
        Set<Pilot> pilots = new HashSet<>();
        Set<Mech> mechs = new HashSet<>();

        Mockito.when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaign));
        Mockito.when(userRepository.findById(user.getUser_id())).thenReturn(Optional.of(user));
        Mockito.when(matchRepository.findByCampaign(campaign)).thenReturn(matches);
        Mockito.when(campaignRepository.findPlayersByCampaign(campaign)).thenReturn(players);
        Mockito.when(mainTaskRepository.findByMatch1(match1)).thenReturn(mainTasks);
        Mockito.when(secondaryTaskRepository.findByMatch1(match1)).thenReturn(secondaryTasks);
        Mockito.when(match1.getPilots()).thenReturn(pilots);
        Mockito.when(match1.getMechs()).thenReturn(mechs);

        // Act and Assert
        mockMvc.perform(post("/delete/{campaign_id}", campaignId)
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/campaignList"));


        // Verify repository interactions
        Mockito.verify(userRepository).save(user);
        Mockito.verify(playerRepository).save(player);
        Mockito.verify(mainTaskRepository).deleteAll(mainTasks);
        Mockito.verify(secondaryTaskRepository).deleteAll(secondaryTasks);
        Mockito.verify(matchRepository).deleteAll(matches);
        Mockito.verify(campaignRepository).deleteById(campaignId);
    }

    @Test
    public void testDeleteCampaign_withMissingCampaign() throws Exception {
        // Arrange
        Long campaignId = 999L;
        MockHttpSession session = new MockHttpSession();
        User user = new User();
        user.setUser_id(1L);
        session.setAttribute("loggedInUser", user);

        Mockito.when(campaignRepository.findById(campaignId)).thenReturn(Optional.empty());

        // Act and Assert
        mockMvc.perform(post("/delete/{campaign_id}", campaignId)
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    public void testDeleteCampaign_withMissingUserSession() throws Exception {
        // Arrange
        Long campaignId = 1L;
        Campaign campaign = new Campaign();
        campaign.setCampaign_id(campaignId);

        Mockito.when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaign));

        // Act and Assert
        mockMvc.perform(post("/delete/{campaign_id}", campaignId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("error"));
    }
}

