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

    @Autowired
    private MainTaskRepository mainTaskRepository;

    @Autowired
    private SecondaryTaskRepository secondaryTaskRepository;

    @Autowired
    private MechRepository mechRepository;


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

        List<Pilot> pilots = (List<Pilot>) pilotRepository.findAll();
        model.addAttribute("pilots", pilots);

        List<Mech> mechs = (List<Mech>) mechRepository.findAll();
        model.addAttribute("mechs", mechs);

        model.addAttribute("campaign_id", campaign_id);

        return "startNewMatch";
    }

    @PostMapping("/startNewMatch")
    public String createMatch(@RequestParam List<Pilot> pilots, @RequestParam List<Mech> mechs,
                              @RequestParam List<String> mainTasksText, @RequestParam List<String> secondaryTasksText,
                              @RequestParam User user1, @RequestParam User user2, Model model) {
        Match1 match = new Match1();

        List<Pilot> firstUserPilots = null;
        List<Pilot> secondUserPilots = null;
        List<Mech> firstUserMechs = null;
        List<Mech> secondUserMechs = null;
        int halfSize = pilots.size()/2;
        for(int i = 0; i < halfSize; i++) {
            firstUserPilots.add(pilots.get(i));
            Mech mech = mechs.get(i);
            mech.setPilot(pilots.get(i));
            mechRepository.save(mech);
            firstUserMechs.add(mech);
        }
        for(int i = halfSize; i < pilots.size(); i++) {
            secondUserPilots.add(pilots.get(i));
            Mech mech = mechs.get(i);
            mech.setPilot(pilots.get(i));
            mechRepository.save(mech);
            firstUserMechs.add(mech);
        }

        assert firstUserPilots != null;
        for(Pilot pilot : firstUserPilots) {
            pilot.setUser(user1);
            pilotRepository.save(pilot);
        }
        assert secondUserPilots != null;
        for(Pilot pilot : secondUserPilots) {
            pilot.setUser(user2);
            pilotRepository.save(pilot);
        }

        List<MainTask> mainTasks = null;
        List<SecondaryTask> secondaryTasks = null;
        MainTask mainTask;
        SecondaryTask secondaryTask;

        for(String text : mainTasksText) {
            mainTask = new MainTask(text);
            mainTask.setMatch_id(match);
            mainTaskRepository.save(mainTask);
            mainTasks.add(mainTask);
        }
        for(String text : secondaryTasksText) {
            secondaryTask = new SecondaryTask(text);
            secondaryTask.setMatch_id(match);
            secondaryTaskRepository.save(secondaryTask);
            secondaryTasks.add(secondaryTask);
        }

        matchRepository.save(match);

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

    @GetMapping("/match")
    public String viewMatch(@RequestParam Long campaign_id, @RequestParam Long match_id, Model model) {
        model.addAttribute("title", "match");
        model.addAttribute("campaign_id", campaign_id);
        model.addAttribute("match_id", match_id);

        Campaign campaign = campaignRepository.findById(campaign_id).orElse(null);
        assert campaign != null;
        int numOfPilots = campaign.getNumOfPilots();
        model.addAttribute("numOfPilots", numOfPilots);

        Match1 match = matchRepository.findById(match_id).orElse(null);
        assert match != null;
        List<Pilot> pilots = pilotRepository.findByMatch(match);

        return "match";
    }
}
