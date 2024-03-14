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

    @Autowired
    private MechChasisRepository mechChasisRepository;


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

    @GetMapping("/startFirstMatch")
    public String startFirstMatch(@RequestParam Long campaign_id, Model model) {
        model.addAttribute("title", "start first match");

        Campaign campaign = campaignRepository.findById(campaign_id).orElse(null);
        assert campaign != null;
        int numOfPilots = campaign.getNumOfPilots();
        model.addAttribute("numOfPilots", numOfPilots);

        List<Pilot> pilots = (List<Pilot>) pilotRepository.findAll();
        model.addAttribute("pilots", pilots);

        List<Mech> mechs = (List<Mech>) mechRepository.findAll();
        model.addAttribute("mechs", mechs);

        model.addAttribute("campaign_id", campaign_id);

        return "startFirstMatch";
    }

    @PostMapping("/startFirstMatch")
    public String createFirstMatch(@RequestParam String matchName, @RequestParam List<Pilot> pilots,
                              @RequestParam List<Mech> mechs, @RequestParam List<String> mainTasksText,
                              @RequestParam List<String> secondaryTasksText, @RequestParam Long campaign_id ,
                              Model model) {
        Campaign campaign = campaignRepository.findById(campaign_id).orElse(null);
        assert campaign != null;
        Set<User> set = campaign.getUsers();
        List<User> users = new ArrayList<>(set);
        User user1 = users.get(0);
        User user2 = users.get(1);

        LocalDate startdate = LocalDate.now();
        boolean isEnded = false;

        Match1 match = new Match1(startdate, matchName, isEnded, campaign);

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
            secondUserMechs.add(mech);
        }

        assert firstUserPilots != null;
        for(Pilot pilot : firstUserPilots) {
            pilot.setUser(user1);
            pilot.setMatch(match);
            pilotRepository.save(pilot);
        }
        assert secondUserPilots != null;
        for(Pilot pilot : secondUserPilots) {
            pilot.setUser(user2);
            pilot.setMatch(match);
            pilotRepository.save(pilot);
        }

        List<MainTask> mainTasks = null;
        List<SecondaryTask> secondaryTasks = null;
        MainTask mainTask;
        SecondaryTask secondaryTask;

        for(String text : mainTasksText) {
            mainTask = new MainTask(text);
            mainTask.setMatch(match);
            mainTaskRepository.save(mainTask);
            mainTasks.add(mainTask);
        }
        for(String text : secondaryTasksText) {
            secondaryTask = new SecondaryTask(text);
            secondaryTask.setMatch(match);
            secondaryTaskRepository.save(secondaryTask);
            secondaryTasks.add(secondaryTask);
        }

        matchRepository.save(match);

        return "matchList";
    }

    @GetMapping("/startNewMatch")
    public String startNewMatch(@RequestParam Long campaign_id, Model model) {
        model.addAttribute("title", "start new match");

        Campaign campaign = campaignRepository.findById(campaign_id).orElse(null);
        assert campaign != null;

        List<Pilot> pilots = pilotRepository.findByCampaign(campaign);
        model.addAttribute("pilots", pilots);

        List<Mech> mechs = mechRepository.findByCampaign(campaign);
        model.addAttribute("mechs", mechs);

        List<MechChasis> mechChases = mechChasisRepository.selectUnassignedChases();
        model.addAttribute("mechsChases", mechChases);

        model.addAttribute("campaign_id", campaign_id);

        return "startNewMatch";
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
        List<Pilot> pilots = pilotRepository.findByMatch1(match);
        model.addAttribute("pilots", pilots);

        List<MainTask> mainTasks = mainTaskRepository.findByMatch(match);
        List<SecondaryTask> secondaryTasks = secondaryTaskRepository.findByMatch(match);

        model.addAttribute("mainTasks", mainTasks);
        model.addAttribute("secondaryTasks", secondaryTasks);

        return "match";
    }

    @GetMapping("/createUser")
    public String createUser(Model model) {
        model.addAttribute("title", "createUser");
        return "createUser";
    }

    @PostMapping("/createUser")
    public String createUser(@RequestParam String nickname, @RequestParam String login,
                             @RequestParam String email, @RequestParam String password) {
        User user = new User(nickname, email, login, password);
        userRepository.save(user);
        return "createUser";
    }
}
