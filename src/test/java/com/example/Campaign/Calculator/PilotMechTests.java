package com.example.Campaign.Calculator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.Campaign.Calculator.Controllers.pilotMechController;
import com.example.Campaign.Calculator.models.*;
import com.example.Campaign.Calculator.repo.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

@WebMvcTest(pilotMechController.class)
class PilotMechTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MechStatusRepository mechStatusRepository;

	@MockBean
	private MechClassRepository mechClassRepository;

	@MockBean
	private MechRepository mechRepository;

	@MockBean
	private PilotStatusRepository pilotStatusRepository;

	@MockBean
	private PilotRankRepository pilotRankRepository;

	@MockBean
	private MechChasisRepository mechChasisRepository;

	@MockBean
	private PilotRepository pilotRepository;

	@MockBean
	private PlayerRepository playerRepository;

	@Test
	public void testCreatePilot_withValidData() throws Exception {
		// Arrange
		PilotStatus readyStatus = new PilotStatus("Ready");
		PilotRank noviceRank = new PilotRank("novice");
		Player player = new Player();
		player.setPlayer_id(1L);  // Set player ID or other necessary fields

		// Mocking repository methods
		Mockito.when(pilotStatusRepository.findByName("Ready")).thenReturn(readyStatus);
		Mockito.when(pilotRankRepository.findByName("novice")).thenReturn(noviceRank);
		Mockito.when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
		Mockito.when(pilotRepository.save(Mockito.any(Pilot.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// Act and Assert
		mockMvc.perform(post("/createPilot")
						.param("pilotName", "John")
						.param("pilotSurname", "Doe")
						.param("pilotNickname", "Ace")
						.param("player_id", "1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/createPilot"));
	}

	@Test
	public void testCreatePilot_withInvalidData() throws Exception {
		// Arrange
		PilotStatus readyStatus = new PilotStatus("Ready");
		PilotRank noviceRank = new PilotRank("novice");
		Player player = new Player();
		player.setPlayer_id(1L);  // Set player ID or other necessary fields

		// Mocking repository methods
		Mockito.when(pilotStatusRepository.findByName("Ready")).thenReturn(readyStatus);
		Mockito.when(pilotRankRepository.findByName("novice")).thenReturn(noviceRank);
		Mockito.when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

		// Act and Assert
		mockMvc.perform(post("/createPilot")
						.param("pilotName", "")
						.param("pilotSurname", "Doe")
						.param("pilotNickname", "Ace")
						.param("player_id", "1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/createPilot"))
				.andExpect(flash().attributeExists("error"));
	}

	@Test
	public void testMech_withoutChoosingMechChasis() throws Exception {
		MechStatus readyStatus = new MechStatus("Ready");
		Player player = new Player();
		player.setPlayer_id(1L);

		mockMvc.perform(post("/createMech")
					.param("mechName", "mech1")
					.param("battleValue", "456")
					.param("mechChasis_id", "")
					.param("player_id", "1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/createMech"))
				.andExpect(flash().attributeExists("error"));
	}

	@Test
	public void testMechChasis_withIncorrectWeight() throws Exception {
		mockMvc.perform(post("/createMechChasis")
					.param("chasisName", "chasis1")
					.param("chasisWeight", ""))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("error"));
	}

	@Test
	public void testChangePilot_withInvalidData() throws Exception {
		mockMvc.perform(post("/makeChangesInPilot")
						.param("pilotRank_id", "0")
						.param("pilotStatus_id", "0")
						.param("campaign_id", "0")
						.param("pilot_id", "0")
						.param("match_id", "0"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/makeChangesInPilot"))
				.andExpect(flash().attributeExists("error"))
				.andExpect(flash().attribute("error", "Incorrect details, please try again"));
	}

	@Test
	public void testChangePilot_withValidData() throws Exception {
		// Arrange
		Long pilotId = 1L;
		Long pilotRankId = 1L;
		Long pilotStatusId = 1L;
		Long campaignId = 1L;
		Long matchId = 1L;

		Pilot pilot = new Pilot();
		PilotStatus pilotStatus = new PilotStatus("Ready");
		PilotRank pilotRank = new PilotRank("Captain");

		Mockito.when(pilotRepository.findById(pilotId)).thenReturn(Optional.of(pilot));
		Mockito.when(pilotStatusRepository.findById(pilotStatusId)).thenReturn(Optional.of(pilotStatus));
		Mockito.when(pilotRankRepository.findById(pilotRankId)).thenReturn(Optional.of(pilotRank));

		// Act and Assert
		mockMvc.perform(post("/makeChangesInPilot")
						.param("pilotRank_id", pilotRankId.toString())
						.param("pilotStatus_id", pilotStatusId.toString())
						.param("campaign_id", campaignId.toString())
						.param("pilot_id", pilotId.toString())
						.param("match_id", matchId.toString()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/endMatch"))
				.andExpect(flash().attributeExists("campaign_id"))
				.andExpect(flash().attribute("campaign_id", campaignId))
				.andExpect(flash().attributeExists("match_id"))
				.andExpect(flash().attribute("match_id", matchId));

		// Verify interactions
		Mockito.verify(pilotRepository).findById(pilotId);
		Mockito.verify(pilotStatusRepository).findById(pilotStatusId);
		Mockito.verify(pilotRankRepository).findById(pilotRankId);
		Mockito.verify(pilotRepository).save(pilot);

		assert pilot.getPilotStatus().equals(pilotStatus);
		assert pilot.getPilotRank().equals(pilotRank);
		assert pilot.getInactiveCount() == 0;
	}

	@Test
	public void testChangeMech_withInvalidData() throws Exception {
		mockMvc.perform(post("/makeChangesInMech")
						.param("mechChasis_id", "0")
						.param("mechStatus_id", "0")
						.param("campaign_id", "0")
						.param("mech_id", "0")
						.param("match_id", "0"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/makeChangesInMech"))
				.andExpect(flash().attributeExists("error"))
				.andExpect(flash().attribute("error", "Incorrect details, please try again"));
	}

	@Test
	public void testChangeMech_withValidData() throws Exception {
		// Arrange
		Long mechChasisId = 1L;
		Long mechStatusId = 1L;
		Long campaignId = 1L;
		Long mechId = 1L;
		Long matchId = 1L;

		Mech mech = new Mech();
		MechChasis mechChasis = new MechChasis();
		mechChasis.setChasisWeight(50); // Medium weight
		MechClass mechClass = new MechClass("Medium", 40, 55);
		MechStatus mechStatus = new MechStatus("Operational");

		Mockito.when(mechRepository.findById(mechId)).thenReturn(Optional.of(mech));
		Mockito.when(mechChasisRepository.findById(mechChasisId)).thenReturn(Optional.of(mechChasis));
		Mockito.when(mechClassRepository.findByClassName("Medium")).thenReturn(mechClass);
		Mockito.when(mechStatusRepository.findById(mechStatusId)).thenReturn(Optional.of(mechStatus));

		// Act and Assert
		mockMvc.perform(post("/makeChangesInMech")
						.param("mechChasis_id", mechChasisId.toString())
						.param("mechStatus_id", mechStatusId.toString())
						.param("campaign_id", campaignId.toString())
						.param("mech_id", mechId.toString())
						.param("match_id", matchId.toString()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/endMatch"))
				.andExpect(flash().attributeExists("campaign_id"))
				.andExpect(flash().attribute("campaign_id", campaignId))
				.andExpect(flash().attributeExists("match_id"))
				.andExpect(flash().attribute("match_id", matchId));

		// Verify interactions
		Mockito.verify(mechRepository).findById(mechId);
		Mockito.verify(mechChasisRepository).findById(mechChasisId);
		Mockito.verify(mechClassRepository).findByClassName("Medium");
		Mockito.verify(mechStatusRepository).findById(mechStatusId);
		Mockito.verify(mechRepository).save(mech);

		assert mech.getMechClass().equals(mechClass);
		assert mech.getMechStatus().equals(mechStatus);
	}
}

