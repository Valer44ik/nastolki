package com.example.Campaign.Calculator.Controllers;

import com.example.Campaign.Calculator.models.*;
import com.example.Campaign.Calculator.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.time.LocalDate;

@Controller
public class campaignController {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/mainPage")
    public String mainPage(Model model)
    {
        model.addAttribute("title", "CampaignList");

        List<Campaign> campaign = new ArrayList<>();
        List<Campaign> campaigns = (List<Campaign>) campaignRepository.findAll();
        model.addAttribute("campaigns", campaigns);

        return "campaignList";
    }

    @GetMapping("/startACampaign")
    public String startACampaign(Model model)
    {
        model.addAttribute("title", "start a campaign");

        model.addAttribute("campaignTypes", CampaignType.values());

        model.addAttribute("formationOrders", FormationOrder.values());

        List<User> users = (List<User>) userRepository.findAll();
        model.addAttribute("users", users);

        return "startACampaign";
    }

    @PostMapping("/startACampaign")
    public String createCampaign(@RequestParam String campaignName, @RequestParam CampaignType campaignType,
                                 @RequestParam FormationOrder formationOrder, @RequestParam Long firstUser_id,
                                 @RequestParam Long secondUser_id, @RequestParam int battleValue, Model model)
    {
        model.addAttribute("title", "start a campaign");

        LocalDate startDate = LocalDate.now();
        Campaign campaign = new Campaign(campaignName, campaignType, formationOrder, battleValue, startDate);

        User firstUser = userRepository.findById(firstUser_id).orElse(null);
        User secondUser = userRepository.findById(secondUser_id).orElse(null);
        Set<User> users = new HashSet<>();
        users.add(firstUser);
        users.add(secondUser);

        campaign.setUsers(users);

        campaignRepository.save(campaign);
        return "startACampaign";
    }

    @GetMapping("/endCampaign")
    public String endCampaign(@RequestParam Long campaign_id, Model model) {
        model.addAttribute("title", "endCampaign");

        Optional<Campaign> optionalCampaign = campaignRepository.findById(campaign_id);

        if(optionalCampaign.isPresent()){
            Campaign campaign = optionalCampaign.get();

            List<Match1> matches = matchRepository.findByCampaign(campaign);
            model.addAttribute("matches", matches);

            List<User> users = campaignRepository.findUsersByCampaign(campaign);
            User user1 = users.get(0);
            User user2 = users.get(1);
            if(user1.getUserStatistics().getGamesWon() > user2.getUserStatistics().getGamesWon())
                model.addAttribute("user", user1);
            else
                model.addAttribute("user", user2);

            model.addAttribute("campaign", campaign);

            model.addAttribute("campaign_id", campaign_id);
            return "endCampaign";
        }
        else {
            model.addAttribute("errorMessage", "Campaign not found");
            return "campaignList";
        }
    }
}
