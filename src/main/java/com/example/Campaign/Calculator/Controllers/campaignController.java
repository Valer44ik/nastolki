package com.example.Campaign.Calculator.Controllers;

import com.example.Campaign.Calculator.models.*;
import com.example.Campaign.Calculator.repo.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.*;
import java.time.LocalDate;

@Controller
@SessionAttributes("loggedInUser")
public class campaignController {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PilotRepository pilotRepository;

    @Autowired
    private PilotStatusRepository pilotStatusRepository;

    @Autowired
    private MechRepository mechRepository;

    @Autowired
    private MechStatusRepository mechStatusRepository;

    @GetMapping("/mainPage")
    public String mainPage(HttpSession session, Model model)
    {
        model.addAttribute("title", "CampaignList");

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        System.out.println("User: " + loggedInUser);

        List<Campaign> campaign = new ArrayList<>();
        List<Campaign> campaigns = (List<Campaign>) campaignRepository.findByUser(loggedInUser);
        model.addAttribute("campaigns", campaigns);

        return "campaignList";
    }

    @GetMapping("/startACampaign")
    public String startACampaign(Model model)
    {
        model.addAttribute("title", "start a campaign");

        model.addAttribute("campaignTypes", CampaignType.values());

        model.addAttribute("formationOrders", FormationOrder.values());

        List<Player> players = (List<Player>) playerRepository.findAll();
        model.addAttribute("players", players);

        return "startACampaign";
    }

    @PostMapping("/startACampaign")
    public String createCampaign(@RequestParam String campaignName, @RequestParam CampaignType campaignType,
                                 @RequestParam FormationOrder formationOrder,
                                 @RequestParam(defaultValue = "0") Long firstPlayer_id,
                                 @RequestParam(defaultValue = "0") Long secondPlayer_id,
                                 @RequestParam(defaultValue = "0") int battleValue,
                                 HttpSession session,
                                 Model model)
    {
        if(campaignName == null || campaignName.isEmpty() ||
                campaignType == null || formationOrder == null ||
                firstPlayer_id < 1 || secondPlayer_id < 1 || battleValue < 1) {
            model.addAttribute("error", "incorrect details, please try again");
            return "redirect:/startACampaign";
        }

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        LocalDate startDate = LocalDate.now();
        Campaign campaign = new Campaign(campaignName, campaignType, formationOrder, battleValue, startDate, loggedInUser);

        Player firstPlayer = playerRepository.findById(firstPlayer_id).orElse(null);
        Player secondPlayer = playerRepository.findById(secondPlayer_id).orElse(null);
        Set<Player> players = new HashSet<>();
        players.add(firstPlayer);
        players.add(secondPlayer);

        campaign.setPlayers(players);

        campaignRepository.save(campaign);
        return "startACampaign";
    }

    @GetMapping("/endCampaign")
    public String viewEndCampaignForm(@RequestParam Long campaign_id, Model model) {
        model.addAttribute("title", "endCampaign");

        Optional<Campaign> optionalCampaign = campaignRepository.findById(campaign_id);

        if(optionalCampaign.isPresent()){
            Campaign campaign = optionalCampaign.get();
            model.addAttribute("campaign", campaign);

            List<Match1> matches = matchRepository.findByCampaign(campaign);
            model.addAttribute("matches", matches);

            List<Map<String, Long>> playerStats = matchRepository.countWinsForPlayer(campaign_id);

            Long winningPlayer_id = null;
            Long maxWinCount = (long) -1;

            Long lostPlayer_id = null;
            Long minWinCount = 999999L;

            for(Map<String, Long> playerStat : playerStats) {
                Long player_id = Long.valueOf(playerStat.get("winning_player_id"));
                Long playerWinMatches = Long.valueOf(playerStat.get("cnt"));

                if(maxWinCount < playerWinMatches && winningPlayer_id != null) {
                    minWinCount = maxWinCount;
                    maxWinCount = playerWinMatches;
                    winningPlayer_id = Long.valueOf(player_id);
                } else if (maxWinCount < playerWinMatches) {
                    maxWinCount = playerWinMatches;
                    winningPlayer_id = Long.valueOf(player_id);
                }else  if(maxWinCount > playerWinMatches) {
                    minWinCount = playerWinMatches;
                }else if(maxWinCount == playerWinMatches) {
                    minWinCount = maxWinCount;
                }
            }

            if(maxWinCount.equals(minWinCount)) {
                model.addAttribute("player", null);
            }
            else {
                Player winningPlayer = playerRepository.findById(winningPlayer_id).orElse(null);
                model.addAttribute("player", winningPlayer);
            }

            model.addAttribute("campaign_id", campaign_id);

            return "endCampaign";
        }
        else {
            model.addAttribute("errorMessage", "Campaign not found");
            return "redirect:/mainPage";
        }
    }

    @PostMapping("/endCampaign")
    public String endCampaign(@RequestParam Long campaign_id, Model model) {
        Campaign campaign = campaignRepository.findById(campaign_id).orElse(null);
        campaign.setEnded(true);

        List<Map<String, Long>> playerStats = matchRepository.countWinsForPlayer(campaign_id);

            Long winningPlayer_id = null;
            Long maxWinCount = (long) -1;

            Long lostPlayer_id = null;
            Long minWinCount = 999999L;

            for(Map<String, Long> playerStat : playerStats) {
                Long player_id = Long.valueOf(playerStat.get("winning_player_id"));
                Long playerWinMatches = Long.valueOf(playerStat.get("cnt"));

                if(maxWinCount < playerWinMatches && winningPlayer_id != null) {
                    lostPlayer_id = winningPlayer_id;
                    minWinCount = maxWinCount;
                    maxWinCount = playerWinMatches;
                    winningPlayer_id = Long.valueOf(player_id);
                } else if (maxWinCount < playerWinMatches) {
                    maxWinCount = playerWinMatches;
                    winningPlayer_id = Long.valueOf(player_id);
                }else  if(maxWinCount > playerWinMatches) {
                    lostPlayer_id = Long.valueOf(player_id);
                    minWinCount = playerWinMatches;
                }else if(maxWinCount == playerWinMatches) {
                    minWinCount = maxWinCount;
                    lostPlayer_id = Long.valueOf(player_id);
                }
            }

        if(maxWinCount == minWinCount) {
            playerRepository.incrementCampaignByOne(winningPlayer_id);
            playerRepository.incrementCampaignByOne(lostPlayer_id);
        }
        else {
            playerRepository.incrementWinCampaignByOne(winningPlayer_id);
            playerRepository.incrementCampaignByOne(lostPlayer_id);
        }

        List<Long> pilots_id = pilotRepository.findPilotsParticipatingInCampaign(campaign_id);
        for (long pilot_id : pilots_id) {
            Pilot pilot = pilotRepository.findById(pilot_id).orElse(null);

            PilotStatus pilotStatus = pilotStatusRepository.findByName("Ready");

            pilot.setPilotStatus(pilotStatus);
            pilot.setInactiveCount((short) 0);
            pilot.setCurrentlyInCampaign(false);
        }

        List<Long> mechs_id = mechRepository.findMechsParticipatingInCampaign(campaign_id);
        for(long mech_id : mechs_id) {
            Mech mech = mechRepository.findById(mech_id).orElse(null);

            MechStatus mechStatus = mechStatusRepository.findByName("Ready");

            mech.setMechStatus(mechStatus);
            mech.setCurrentlyInCampaign(false);
        }
        return "redirect:/mainPage";
    }
}
