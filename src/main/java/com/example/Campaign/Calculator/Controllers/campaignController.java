package com.example.Campaign.Calculator.Controllers;

import com.example.Campaign.Calculator.models.*;
import com.example.Campaign.Calculator.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.Optional;

@Controller
public class campaignController {

    @Autowired
    private MechChasisRepository mechChasisRepository;

    @Autowired
    private PilotRepository pilotRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
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

        return "startACampaign";
    }

    @PostMapping("/startACampaign")
    public String createCampaign(@RequestParam String campaignName, @RequestParam CampaignType campaignType,
                                 @RequestParam FormationOrder formationOrder, @RequestParam int battleValue, Model model)
    {
        model.addAttribute("title", "start a campaign");
        LocalDate startDate = LocalDate.now();
        Campaign campaign = new Campaign(campaignName, campaignType, formationOrder, battleValue, startDate);
        campaignRepository.save(campaign);
        return "startACampaign";
    }

    @GetMapping("/campaignSheet")
    public String campaignSheet(Model model)
    {
        model.addAttribute("title", "campaign sheet");
        return "match";
    }

    @GetMapping("/startNewMatch")
    public String startNewMatch(@RequestParam Long campaign_id, Model model) {
        model.addAttribute("title", "start new match");

        Campaign campaign = campaignRepository.findById(campaign_id).orElse(null);
        assert campaign != null;
        int numOfPilots = campaign.getNumOfPilots();
        model.addAttribute("numOfPilots", numOfPilots);

        List<Pilot> pilot = new ArrayList<>();
        List<Pilot> pilots = (List<Pilot>) pilotRepository.findAll();
        model.addAttribute("pilots", pilots);

        List<MechChasis> mechChasis = new ArrayList<>();
        List<MechChasis> mechChases = (List<MechChasis>) mechChasisRepository.findAll();
        model.addAttribute("mechChases", mechChases);

        model.addAttribute("campaign_id", campaign_id);

        return "startNewMatch";
    }

    @PostMapping("/startNewMatch")
    public String createMatch(@RequestParam List<Pilot> pilots, @RequestParam List<MechChasis> mechChasis,
                              @RequestParam List<String> mainTasksText, @RequestParam List<String> secondaryTasksText,
                              @RequestParam User user1, @RequestParam User user2, Model model) {
        List<Pilot> firstUserPilots = null;
        List<Pilot> secondUserPilots = null;

        int halfSize = pilots.size()/2;

        for(int i = 0; i < halfSize; i++) {
            firstUserPilots.add(pilots.get(i));
        }
        for(int i = halfSize; i < pilots.size(); i++) {
            secondUserPilots.add(pilots.get(i));
        }

        for(Pilot pilot : firstUserPilots) {
            pilot.setUser(user1);
        }

        for(Pilot pilot : secondUserPilots) {
            pilot.setUser(user2);
        }

        List<MainTask> mainTasks = null;
        MainTask mainTask;

        for(String text : mainTasksText) {
            mainTask = new MainTask(text);
            mainTasks.add(mainTask);
        }

        return "matchList";
    }

    @GetMapping("/matchList")
    public String matchList(@RequestParam Long campaign_id, Model model) {
        model.addAttribute("title", "match list");
        Optional<Campaign> optionalCampaign = campaignRepository.findById(campaign_id);

        if(optionalCampaign.isPresent()){
            Campaign campaign = optionalCampaign.get();
            List<Match1> matches = matchRepository.findByCampaign(campaign);
            model.addAttribute("matches", matches);

            model.addAttribute("campaign_id", campaign_id);
            return "matchList";
        }
        else {
            model.addAttribute("errorMessage", "Campaign not found");
            return "campaignList";
        }
    }
}
