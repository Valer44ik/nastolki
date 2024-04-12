package com.example.Campaign.Calculator.Controllers;

import com.example.Campaign.Calculator.models.*;
import com.example.Campaign.Calculator.repo.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
                                 @RequestParam FormationOrder formationOrder, @RequestParam Long firstPlayer_id,
                                 @RequestParam Long secondPlayer_id, @RequestParam int battleValue,
                                 HttpSession session,
                                 Model model)
    {
        model.addAttribute("title", "start a campaign");

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

            System.out.println("Campaign: " + campaign);

            List<Match1> matches = matchRepository.findByCampaign(campaign);
            model.addAttribute("matches", matches);

            List<Player> players = campaignRepository.findPlayersByCampaign(campaign);
            Player player1 = players.get(0);
            Player player2 = players.get(1);
            if(player1.getGamesWon() > player2.getGamesWon())
                model.addAttribute("player", player1);
            else if (player1.getGamesWon() < player2.getGamesWon())
                model.addAttribute("player", player2);
            else
                model.addAttribute("player", null);

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

        List<Player> players = campaignRepository.findPlayersByCampaign(campaign);
        Player winningPlayer;
        Player lostPlayer;
        if(players.get(0).getGamesWon() < players.get(1).getGamesWon()) {
            lostPlayer = players.get(0);
            winningPlayer = players.get(1);
        }
        else {
            lostPlayer = players.get(1);
            winningPlayer = players.get(0);
        }

        playerRepository.incrementWinCampaignByOne(winningPlayer.getPlayer_id());

        playerRepository.incrementCampaignByOne(lostPlayer.getPlayer_id());

        return "redirect:/mainPage";
    }
}
