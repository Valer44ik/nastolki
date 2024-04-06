package com.example.Campaign.Calculator.Controllers;

import com.example.Campaign.Calculator.models.*;
import com.example.Campaign.Calculator.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;

@Controller
public class matchController {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private PilotRepository pilotRepository;

    @Autowired
    private MechRepository mechRepository;

    @Autowired
    private MechChasisRepository mechChasisRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private MainTaskRepository mainTaskRepository;

    @Autowired
    private SecondaryTaskRepository secondaryTaskRepository;

    @GetMapping("/startFirstMatch")
    public String startFirstMatch(@RequestParam Long campaign_id, Model model) {
        model.addAttribute("title", "start first match");

        Campaign campaign = campaignRepository.findById(campaign_id).orElse(null);
        assert campaign != null;
        int numOfPilots = campaign.getNumOfPilots();
        model.addAttribute("numOfPilots", numOfPilots);

        List<User> users = campaignRepository.findUsersByCampaign(campaign);
        User user1 = users.get(0);
        User user2 = users.get(1);

        List<Pilot> firstUserPilots = pilotRepository.findByUser(user1);
        List<Pilot> secondUserPilots = pilotRepository.findByUser(user2);
        model.addAttribute("firstUserPilots", firstUserPilots);
        model.addAttribute("secondUserPilots", secondUserPilots);

        List<Mech> firstUserMechs = mechRepository.findByUser(user1);
        List<Mech> secondUserMechs = mechRepository.findByUser(user2);
        model.addAttribute("firstUserMechs", firstUserMechs);
        model.addAttribute("secondUserMechs", secondUserMechs);

        model.addAttribute("campaign_id", campaign_id);

        return "startFirstMatch";
    }

    @PostMapping("/startFirstMatch")
    public String createFirstMatch(@RequestParam String matchName, @RequestParam List<Long> firstUserPilots_id,
                                   @RequestParam List<Long> firstUserMechs_id,
                                   @RequestParam List<Long> secondUserPilots_id,
                                   @RequestParam List<Long> secondUserMechs_id,
                                   @RequestParam List<String> mainTasksTextForPlayer1,
                                   @RequestParam List<String> secondaryTasksTextForPlayer1,
                                   @RequestParam List<String> mainTasksTextForPlayer2,
                                   @RequestParam List<String> secondaryTasksTextForPlayer2,
                                   @RequestParam Long campaign_id, RedirectAttributes redirectAttributes,
                                   Model model) {
        Campaign campaign = campaignRepository.findById(campaign_id).orElse(null);
        assert campaign != null;
        List<User> users = campaignRepository.findUsersByCampaign(campaign);
        User user1 = users.get(0);
        User user2 = users.get(1);

        LocalDate startdate = LocalDate.now();
        boolean isEnded = false;

        Match1 match = new Match1(startdate, matchName, isEnded, campaign);
        matchRepository.save(match);
        Long match_id = match.getMatch_id();

        for(int i = 0; i < firstUserPilots_id.size(); i++) {
            matchRepository.bindPilotsAndMechsToMatch(match_id, firstUserPilots_id.get(i), firstUserMechs_id.get(i));
        }
        for(int i = 0; i < secondUserPilots_id.size(); i++) {
            matchRepository.bindPilotsAndMechsToMatch(match_id, secondUserPilots_id.get(i), secondUserMechs_id.get(i));
        }
        /*
        List<MainTask> firstUserMainTasks = new ArrayList<>();
        List<MainTask> secondUserMainTasks = new ArrayList<>();
        List<SecondaryTask> firstUserSecondaryTasks = new ArrayList<>();
        List<SecondaryTask> secondUserSecondaryTasks = new ArrayList<>();
        MainTask mainTask;
        SecondaryTask secondaryTask;

        for(String text : mainTasksTextForPlayer1) {
            mainTask = new MainTask(text);
            mainTask.setMatch1(match);
            mainTaskRepository.save(mainTask);
            firstUserMainTasks.add(mainTask);
            //user1.s
        }
        for(String text : secondaryTasksTextForPlayer1) {
            secondaryTask = new SecondaryTask(text);
            secondaryTask.setMatch1(match);
            secondaryTaskRepository.save(secondaryTask);
            firstUserSecondaryTasks.add(secondaryTask);
        }
        */
        redirectAttributes.addAttribute("campaign_id", campaign_id);
        return "redirect:/matchList";
    }

    @GetMapping("/startNewMatch")
    public String showNewMatch(@RequestParam Long campaign_id, Model model) {
        model.addAttribute("title", "start new match");

        Campaign campaign = campaignRepository.findById(campaign_id).orElse(null);
        assert campaign != null;
        int numOfPilots = campaign.getNumOfPilots();
        model.addAttribute("numOfPilots", numOfPilots);

        List<User> users = campaignRepository.findUsersByCampaign(campaign);
        User user1 = users.get(0);
        User user2 = users.get(1);

        List<Long> firstUserPilots_ids = pilotRepository.findPilotsReadyForMatch(user1.getUser_id());
        List<Long> secondUserPilots_ids = pilotRepository.findPilotsReadyForMatch(user2.getUser_id());
        List<Pilot> firstUserPilots = new ArrayList<>();
        List<Pilot> secondUserPilots = new ArrayList<>();
        for(Long pilot_id : firstUserPilots_ids) {
            Pilot pilot = pilotRepository.findById(pilot_id).orElse(null);
            firstUserPilots.add(pilot);
        }
        for(Long pilot_id : secondUserPilots_ids) {
            Pilot pilot = pilotRepository.findById(pilot_id).orElse(null);
            secondUserPilots.add(pilot);
        }
        model.addAttribute("firstUserPilots", firstUserPilots);
        model.addAttribute("secondUserPilots", secondUserPilots);

        List<Long> firstUserMechs_ids = mechRepository.findMechsReadyForMatch(user1.getUser_id());
        List<Long> secondUserMechs_ids = mechRepository.findMechsReadyForMatch(user2.getUser_id());
        List<Mech> firstUserMechs = mechRepository.findByUser(user1);
        List<Mech> secondUserMechs = mechRepository.findByUser(user2);
        for(Long mech_id : firstUserMechs_ids) {
            Mech mech = mechRepository.findById(mech_id).orElse(null);
            firstUserMechs.add(mech);
        }
        for (Long mech_id : secondUserMechs_ids) {
            Mech mech = mechRepository.findById(mech_id).orElse(null);
            secondUserMechs.add(mech);
        }
        model.addAttribute("firstUserMechs", firstUserMechs);
        model.addAttribute("secondUserMechs", secondUserMechs);

        List<MechChasis> mechChases = (List<MechChasis>) mechChasisRepository.findAll();
        model.addAttribute("mechChases", mechChases);

        model.addAttribute("campaign_id", campaign_id);

        return "startNewMatch";
    }

    @PostMapping("/startNewMatch")
    public String startNewMatch(@RequestParam Long campaign_id, Model model) {
        Campaign campaign = campaignRepository.findById(campaign_id).orElse(null);
        assert campaign != null;
        List<User> users = campaignRepository.findUsersByCampaign(campaign);
        User user1 = users.get(0);
        User user2 = users.get(1);

        LocalDate startdate = LocalDate.now();
        boolean isEnded = false;

       // Match1 match = new Match1(startdate, matchName, isEnded, campaign);
       // matchRepository.save(match);
        //Long match_id = match.getMatch_id();

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
        List<User> users = new ArrayList<>(campaign.getUsers());
        User user1 = users.get(0);
        User user2 = users.get(1);

        List<Long>pilots_ids = pilotRepository.findPilotsByMatchId(match.getMatch_id());
        List<Pilot> firstUserPilots = new ArrayList<>();
        List<Pilot> secondUserPilots = new ArrayList<>();
        for(Long pilot_id : pilots_ids) {
            Pilot pilot = pilotRepository.findById(pilot_id).orElse(null);
            if(pilot.getUser() == user1) {
                firstUserPilots.add(pilot);
            }
            else {
                secondUserPilots.add(pilot);
            }
        }

        model.addAttribute("firstUserPilots", firstUserPilots);
        model.addAttribute("secondUserPilots", secondUserPilots);

        List<MatchPilotAndMech> firstUserMatchPilotsAndMechs = new ArrayList<>();
        List<MatchPilotAndMech> secondUserMatchPilotsAndMechs = new ArrayList<>();
        for(int i = 0; i < firstUserPilots.size(); i++) {
            List<Long> mech_id = mechRepository.findMechByMatchAndPilot_id(match.getMatch_id(),
                    firstUserPilots.get(i).getPilot_id());
            Mech mech = mechRepository.findById(mech_id.get(0)).orElse(null);
            MatchPilotAndMech matchPilotAndMech = new MatchPilotAndMech(firstUserPilots.get(i), mech);
            System.out.println("pilot " + matchPilotAndMech.getPilot());
            firstUserMatchPilotsAndMechs.add(matchPilotAndMech);
        }
        for(int i = 0; i < secondUserPilots.size(); i++) {
            List<Long> mech_id = mechRepository.findMechByMatchAndPilot_id(match.getMatch_id(),
                    secondUserPilots.get(i).getPilot_id());
            Mech mech = mechRepository.findById(mech_id.get(0)).orElse(null);
            MatchPilotAndMech matchPilotAndMech = new MatchPilotAndMech(secondUserPilots.get(i), mech);
            System.out.println("pilot " + matchPilotAndMech.getPilot());
            secondUserMatchPilotsAndMechs.add(matchPilotAndMech);
        }

        model.addAttribute("firstUserMatchPilotsAndMechs", firstUserMatchPilotsAndMechs);
        model.addAttribute("secondUserMatchPilotsAndMechs", secondUserMatchPilotsAndMechs);

        List<MainTask> mainTasks = mainTaskRepository.findByMatch1(match);
        List<SecondaryTask> secondaryTasks = secondaryTaskRepository.findByMatch1(match);

        model.addAttribute("mainTasks", mainTasks);
        model.addAttribute("secondaryTasks", secondaryTasks);

        return "match";
    }

    @PostMapping("/match")
    public String submitMatch(@RequestParam Long campaign_id, RedirectAttributes redirectAttributes,Model model) {
        redirectAttributes.addAttribute("campaign_id", campaign_id);
        return "redirect:/matchList";
    }

    @GetMapping("/endMatch")
    public String endMatch(@RequestParam Long campaign_id, @RequestParam Long match_id, Model model) {
        model.addAttribute("title", "end match");
        model.addAttribute("match_id", match_id);
        model.addAttribute("campaign_id", campaign_id);


        Campaign campaign = campaignRepository.findById(campaign_id).orElse(null);
        assert campaign != null;
        int numOfPilots = campaign.getNumOfPilots();
        model.addAttribute("numOfPilots", numOfPilots);

        Match1 match = matchRepository.findById(match_id).orElse(null);
        List<User> users = new ArrayList<>(campaign.getUsers());
        User user1 = users.get(0);
        User user2 = users.get(1);

        List<Long>pilots_ids = pilotRepository.findPilotsByMatchId(match.getMatch_id());
        List<Pilot> firstUserPilots = new ArrayList<>();
        List<Pilot> secondUserPilots = new ArrayList<>();
        for(Long pilot_id : pilots_ids) {
            Pilot pilot = pilotRepository.findById(pilot_id).orElse(null);
            if(pilot.getUser() == user1) {
                firstUserPilots.add(pilot);
            }
            else {
                secondUserPilots.add(pilot);
            }
        }

        model.addAttribute("firstUserPilots", firstUserPilots);
        model.addAttribute("secondUserPilots", secondUserPilots);

        List<MatchPilotAndMech> firstUserMatchPilotsAndMechs = new ArrayList<>();
        List<MatchPilotAndMech> secondUserMatchPilotsAndMechs = new ArrayList<>();
        for(int i = 0; i < firstUserPilots.size(); i++) {
            List<Long> mech_id = mechRepository.findMechByMatchAndPilot_id(match.getMatch_id(),
                    firstUserPilots.get(i).getPilot_id());
            Mech mech = mechRepository.findById(mech_id.get(0)).orElse(null);
            MatchPilotAndMech matchPilotAndMech = new MatchPilotAndMech(firstUserPilots.get(i), mech);
            System.out.println("pilot " + matchPilotAndMech.getPilot());
            firstUserMatchPilotsAndMechs.add(matchPilotAndMech);
        }
        for(int i = 0; i < secondUserPilots.size(); i++) {
            List<Long> mech_id = mechRepository.findMechByMatchAndPilot_id(match.getMatch_id(),
                    secondUserPilots.get(i).getPilot_id());
            Mech mech = mechRepository.findById(mech_id.get(0)).orElse(null);
            MatchPilotAndMech matchPilotAndMech = new MatchPilotAndMech(secondUserPilots.get(i), mech);
            System.out.println("pilot " + matchPilotAndMech.getPilot());
            secondUserMatchPilotsAndMechs.add(matchPilotAndMech);
        }

        model.addAttribute("firstUserMatchPilotsAndMechs", firstUserMatchPilotsAndMechs);
        model.addAttribute("secondUserMatchPilotsAndMechs", secondUserMatchPilotsAndMechs);

        return "endMatch";
    }

}
