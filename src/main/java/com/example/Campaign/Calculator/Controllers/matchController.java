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

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping("/startFirstMatch")
    public String startFirstMatch(@RequestParam Long campaign_id, Model model) {
        model.addAttribute("title", "start first match");

        Campaign campaign = campaignRepository.findById(campaign_id).orElse(null);
        assert campaign != null;
        int numOfPilots = campaign.getNumOfPilots();
        model.addAttribute("numOfPilots", numOfPilots);

        List<Player> players = campaignRepository.findPlayersByCampaign(campaign);
        Player player1 = players.get(0);
        Player player2 = players.get(1);

        List<Pilot> firstPlayerPilots = pilotRepository.findByPlayer(player1);
        List<Pilot> secondPlayerPilots = pilotRepository.findByPlayer(player2);
        model.addAttribute("firstPlayerPilots", firstPlayerPilots);
        model.addAttribute("secondPlayerPilots", secondPlayerPilots);

        List<Mech> firstPlayerMechs = mechRepository.findByPlayer(player1);
        List<Mech> secondPlayerMechs = mechRepository.findByPlayer(player2);
        model.addAttribute("firstPlayerMechs", firstPlayerMechs);
        model.addAttribute("secondPlayerMechs", secondPlayerMechs);

        model.addAttribute("campaign_id", campaign_id);

        return "startFirstMatch";
    }

    @PostMapping("/startFirstMatch")
    public String createFirstMatch(@RequestParam String matchName, @RequestParam List<Long> firstPlayerPilots_id,
                                   @RequestParam List<Long> firstPlayerMechs_id,
                                   @RequestParam List<Long> secondPlayerPilots_id,
                                   @RequestParam List<Long> secondPlayerMechs_id,
                                   @RequestParam List<String> mainTasksTextForPlayer1,
                                   @RequestParam List<String> secondaryTasksTextForPlayer1,
                                   @RequestParam List<String> mainTasksTextForPlayer2,
                                   @RequestParam List<String> secondaryTasksTextForPlayer2,
                                   @RequestParam Long campaign_id, RedirectAttributes redirectAttributes,
                                   Model model) {
        Campaign campaign = campaignRepository.findById(campaign_id).orElse(null);
        assert campaign != null;
        List<Player> players = campaignRepository.findPlayersByCampaign(campaign);
        Player player1 = players.get(0);
        Player player2 = players.get(1);

        LocalDate startdate = LocalDate.now();
        boolean isEnded = false;

        Match1 match = new Match1(startdate, matchName, isEnded, campaign);
        matchRepository.save(match);
        Long match_id = match.getMatch_id();

        for(int i = 0; i < firstPlayerPilots_id.size(); i++) {
            matchRepository.bindPilotsAndMechsToMatch(match_id, firstPlayerPilots_id.get(i), firstPlayerMechs_id.get(i));
        }
        for(int i = 0; i < secondPlayerPilots_id.size(); i++) {
            matchRepository.bindPilotsAndMechsToMatch(match_id, secondPlayerPilots_id.get(i), secondPlayerMechs_id.get(i));
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

        List<Player> players = campaignRepository.findPlayersByCampaign(campaign);
        Player player1 = players.get(0);
        Player player2 = players.get(1);

        List<Long> firstPlayerPilots_ids = pilotRepository.findPilotsReadyForMatch(player1.getPlayer_id());
        List<Long> secondPlayerPilots_ids = pilotRepository.findPilotsReadyForMatch(player2.getPlayer_id());
        List<Pilot> firstPlayerPilots = new ArrayList<>();
        List<Pilot> secondPlayerPilots = new ArrayList<>();
        for(Long pilot_id : firstPlayerPilots_ids) {
            Pilot pilot = pilotRepository.findById(pilot_id).orElse(null);
            firstPlayerPilots.add(pilot);
        }
        for(Long pilot_id : secondPlayerPilots_ids) {
            Pilot pilot = pilotRepository.findById(pilot_id).orElse(null);
            secondPlayerPilots.add(pilot);
        }
        model.addAttribute("firstPlayerPilots", firstPlayerPilots);
        model.addAttribute("secondPlayerPilots", secondPlayerPilots);

        List<Long> firstPlayerMechs_ids = mechRepository.findMechsReadyForMatch(player1.getPlayer_id());
        List<Long> secondPlayerMechs_ids = mechRepository.findMechsReadyForMatch(player2.getPlayer_id());
        List<Mech> firstPlayerMechs = mechRepository.findByPlayer(player1);
        List<Mech> secondPlayerMechs = mechRepository.findByPlayer(player2);
        for(Long mech_id : firstPlayerMechs_ids) {
            Mech mech = mechRepository.findById(mech_id).orElse(null);
            firstPlayerMechs.add(mech);
        }
        for (Long mech_id : secondPlayerMechs_ids) {
            Mech mech = mechRepository.findById(mech_id).orElse(null);
            secondPlayerMechs.add(mech);
        }
        model.addAttribute("firstPlayerMechs", firstPlayerMechs);
        model.addAttribute("secondPlayerMechs", secondPlayerMechs);

        List<MechChasis> mechChases = (List<MechChasis>) mechChasisRepository.findAll();
        model.addAttribute("mechChases", mechChases);

        model.addAttribute("campaign_id", campaign_id);

        return "startNewMatch";
    }

    @PostMapping("/startNewMatch")
    public String startNewMatch(@RequestParam Long campaign_id, Model model) {
        Campaign campaign = campaignRepository.findById(campaign_id).orElse(null);
        assert campaign != null;
        List<Player> players = campaignRepository.findPlayersByCampaign(campaign);
        Player player1 = players.get(0);
        Player player2 = players.get(1);

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
            return "redirect:/mainPage";
        }
    }

    @GetMapping("/endedMatchList")
    public String watchEndedMatchList(@RequestParam Long campaign_id, Model model) {
        model.addAttribute("title", "match list");
        Optional<Campaign> optionalCampaign = campaignRepository.findById(campaign_id);

        if(optionalCampaign.isPresent()){
            Campaign campaign = optionalCampaign.get();
            List<Match1> matches = matchRepository.findByCampaign(campaign);
            model.addAttribute("matches", matches);

            model.addAttribute("campaign_id", campaign_id);
            return "endedMatchList";
        }
        else {
            model.addAttribute("errorMessage", "Campaign not found");
            return "redirect:/mainPage";
        }
    }

    @GetMapping("/playMatch")
    public String viewMatch(@RequestParam Long campaign_id, @RequestParam Long match_id, Model model) {
        model.addAttribute("title", "match");
        model.addAttribute("campaign_id", campaign_id);
        model.addAttribute("match_id", match_id);

        Campaign campaign = campaignRepository.findById(campaign_id).orElse(null);
        assert campaign != null;
        int numOfPilots = campaign.getNumOfPilots();
        model.addAttribute("numOfPilots", numOfPilots);

        Match1 match = matchRepository.findById(match_id).orElse(null);
        List<Player> players = new ArrayList<>(campaign.getPlayers());
        Player player1 = players.get(0);
        Player player2 = players.get(1);

        List<Long>pilots_ids = pilotRepository.findPilotsByMatchId(match.getMatch_id());
        List<Pilot> firstPlayerPilots = new ArrayList<>();
        List<Pilot> secondPlayerPilots = new ArrayList<>();
        for(Long pilot_id : pilots_ids) {
            Pilot pilot = pilotRepository.findById(pilot_id).orElse(null);
            if(pilot.getPlayer() == player1) {
                firstPlayerPilots.add(pilot);
            }
            else {
                secondPlayerPilots.add(pilot);
            }
        }

        model.addAttribute("firstPlayerPilots", firstPlayerPilots);
        model.addAttribute("secondPlayerPilots", secondPlayerPilots);

        List<MatchPilotAndMech> firstPlayerMatchPilotsAndMechs = new ArrayList<>();
        List<MatchPilotAndMech> secondPlayerMatchPilotsAndMechs = new ArrayList<>();
        for(int i = 0; i < firstPlayerPilots.size(); i++) {
            List<Long> mech_id = mechRepository.findMechByMatchAndPilot_id(match.getMatch_id(),
                    firstPlayerPilots.get(i).getPilot_id());
            Mech mech = mechRepository.findById(mech_id.get(0)).orElse(null);
            MatchPilotAndMech matchPilotAndMech = new MatchPilotAndMech(firstPlayerPilots.get(i), mech);
            System.out.println("pilot " + matchPilotAndMech.getPilot());
            firstPlayerMatchPilotsAndMechs.add(matchPilotAndMech);
        }
        for(int i = 0; i < secondPlayerPilots.size(); i++) {
            List<Long> mech_id = mechRepository.findMechByMatchAndPilot_id(match.getMatch_id(),
                    secondPlayerPilots.get(i).getPilot_id());
            Mech mech = mechRepository.findById(mech_id.get(0)).orElse(null);
            MatchPilotAndMech matchPilotAndMech = new MatchPilotAndMech(secondPlayerPilots.get(i), mech);
            System.out.println("pilot " + matchPilotAndMech.getPilot());
            secondPlayerMatchPilotsAndMechs.add(matchPilotAndMech);
        }

        model.addAttribute("firstPlayerMatchPilotsAndMechs", firstPlayerMatchPilotsAndMechs);
        model.addAttribute("secondPlayerMatchPilotsAndMechs", secondPlayerMatchPilotsAndMechs);

        List<MainTask> mainTasks = mainTaskRepository.findByMatch1(match);
        List<SecondaryTask> secondaryTasks = secondaryTaskRepository.findByMatch1(match);

        model.addAttribute("mainTasks", mainTasks);
        model.addAttribute("secondaryTasks", secondaryTasks);

        return "playMatch";
    }

    @PostMapping("/playMatch")
    public String submitMatch(@RequestParam Long campaign_id, RedirectAttributes redirectAttributes,Model model) {
        redirectAttributes.addAttribute("campaign_id", campaign_id);
        return "redirect:/matchList";
    }

    @GetMapping("/watchMatch")
    public String viewMatchForm(@RequestParam Long campaign_id, @RequestParam Long match_id, Model model) {
        model.addAttribute("title", "match");
        model.addAttribute("campaign_id", campaign_id);
        model.addAttribute("match_id", match_id);

        Campaign campaign = campaignRepository.findById(campaign_id).orElse(null);
        assert campaign != null;
        int numOfPilots = campaign.getNumOfPilots();
        model.addAttribute("numOfPilots", numOfPilots);

        Match1 match = matchRepository.findById(match_id).orElse(null);
        List<Player> players = new ArrayList<>(campaign.getPlayers());
        Player player1 = players.get(0);
        Player player2 = players.get(1);

        List<Long>pilots_ids = pilotRepository.findPilotsByMatchId(match.getMatch_id());
        List<Pilot> firstPlayerPilots = new ArrayList<>();
        List<Pilot> secondPlayerPilots = new ArrayList<>();
        for(Long pilot_id : pilots_ids) {
            Pilot pilot = pilotRepository.findById(pilot_id).orElse(null);
            if(pilot.getPlayer() == player1) {
                firstPlayerPilots.add(pilot);
            }
            else {
                secondPlayerPilots.add(pilot);
            }
        }

        model.addAttribute("firstPlayerPilots", firstPlayerPilots);
        model.addAttribute("secondPlayerPilots", secondPlayerPilots);

        List<MatchPilotAndMech> firstPlayerMatchPilotsAndMechs = new ArrayList<>();
        List<MatchPilotAndMech> secondPlayerMatchPilotsAndMechs = new ArrayList<>();
        for(int i = 0; i < firstPlayerPilots.size(); i++) {
            List<Long> mech_id = mechRepository.findMechByMatchAndPilot_id(match.getMatch_id(),
                    firstPlayerPilots.get(i).getPilot_id());
            Mech mech = mechRepository.findById(mech_id.get(0)).orElse(null);
            MatchPilotAndMech matchPilotAndMech = new MatchPilotAndMech(firstPlayerPilots.get(i), mech);
            System.out.println("pilot " + matchPilotAndMech.getPilot());
            firstPlayerMatchPilotsAndMechs.add(matchPilotAndMech);
        }
        for(int i = 0; i < secondPlayerPilots.size(); i++) {
            List<Long> mech_id = mechRepository.findMechByMatchAndPilot_id(match.getMatch_id(),
                    secondPlayerPilots.get(i).getPilot_id());
            Mech mech = mechRepository.findById(mech_id.get(0)).orElse(null);
            MatchPilotAndMech matchPilotAndMech = new MatchPilotAndMech(secondPlayerPilots.get(i), mech);
            System.out.println("pilot " + matchPilotAndMech.getPilot());
            secondPlayerMatchPilotsAndMechs.add(matchPilotAndMech);
        }

        model.addAttribute("firstPlayerMatchPilotsAndMechs", firstPlayerMatchPilotsAndMechs);
        model.addAttribute("secondPlayerMatchPilotsAndMechs", secondPlayerMatchPilotsAndMechs);

        List<MainTask> mainTasks = mainTaskRepository.findByMatch1(match);
        List<SecondaryTask> secondaryTasks = secondaryTaskRepository.findByMatch1(match);

        model.addAttribute("mainTasks", mainTasks);
        model.addAttribute("secondaryTasks", secondaryTasks);

        return "watchMatch";
    }

    @GetMapping("/endMatch")
    public String showEndMatchForm(@RequestParam Long campaign_id, @RequestParam Long match_id, Model model) {
        model.addAttribute("title", "end match");
        model.addAttribute("match_id", match_id);
        model.addAttribute("campaign_id", campaign_id);

        Campaign campaign = campaignRepository.findById(campaign_id).orElse(null);
        assert campaign != null;
        int numOfPilots = campaign.getNumOfPilots();
        model.addAttribute("numOfPilots", numOfPilots);

        Match1 match = matchRepository.findById(match_id).orElse(null);
        List<Player> players = new ArrayList<>(campaign.getPlayers());
        Player player1 = players.get(0);
        Player player2 = players.get(1);

        model.addAttribute("players", players);

        List<Long>pilots_ids = pilotRepository.findPilotsByMatchId(match.getMatch_id());
        List<Pilot> firstPlayerPilots = new ArrayList<>();
        List<Pilot> secondPlayerPilots = new ArrayList<>();
        for(Long pilot_id : pilots_ids) {
            Pilot pilot = pilotRepository.findById(pilot_id).orElse(null);
            if(pilot.getPlayer() == player1) {
                firstPlayerPilots.add(pilot);
            }
            else {
                secondPlayerPilots.add(pilot);
            }
        }

        model.addAttribute("firstPlayerPilots", firstPlayerPilots);
        model.addAttribute("secondPlayerPilots", secondPlayerPilots);

        List<MatchPilotAndMech> firstPlayerMatchPilotsAndMechs = new ArrayList<>();
        List<MatchPilotAndMech> secondPlayerMatchPilotsAndMechs = new ArrayList<>();
        for(int i = 0; i < firstPlayerPilots.size(); i++) {
            List<Long> mech_id = mechRepository.findMechByMatchAndPilot_id(match.getMatch_id(),
                    firstPlayerPilots.get(i).getPilot_id());
            Mech mech = mechRepository.findById(mech_id.get(0)).orElse(null);
            MatchPilotAndMech matchPilotAndMech = new MatchPilotAndMech(firstPlayerPilots.get(i), mech);
            System.out.println("pilot " + matchPilotAndMech.getPilot());
            firstPlayerMatchPilotsAndMechs.add(matchPilotAndMech);
        }
        for(int i = 0; i < secondPlayerPilots.size(); i++) {
            List<Long> mech_id = mechRepository.findMechByMatchAndPilot_id(match.getMatch_id(),
                    secondPlayerPilots.get(i).getPilot_id());
            Mech mech = mechRepository.findById(mech_id.get(0)).orElse(null);
            MatchPilotAndMech matchPilotAndMech = new MatchPilotAndMech(secondPlayerPilots.get(i), mech);
            System.out.println("pilot " + matchPilotAndMech.getPilot());
            secondPlayerMatchPilotsAndMechs.add(matchPilotAndMech);
        }

        model.addAttribute("firstPlayerMatchPilotsAndMechs", firstPlayerMatchPilotsAndMechs);
        model.addAttribute("secondPlayerMatchPilotsAndMechs", secondPlayerMatchPilotsAndMechs);

        return "endMatch";
    }

    @PostMapping("/endMatch")
    public String endMatch(@RequestParam Long winningPlayer_id,
                           @RequestParam Long campaign_id,
                           @RequestParam Long match_id, Model model){
        Match1 match = matchRepository.findById(match_id).orElse(null);
        match.setEnded(true);

        Campaign campaign = campaignRepository.findById(campaign_id).orElse(null);

        List<Player> players = campaignRepository.findPlayersByCampaign(campaign);
        Player lostPlayer;
        if(Objects.equals(players.get(0).getPlayer_id(), winningPlayer_id)) {
            lostPlayer = players.get(1);
        }
        else {
            lostPlayer = players.get(0);
        }

        playerRepository.incrementWinMatchByOne(winningPlayer_id);

        playerRepository.incrementMatchByOne(lostPlayer.getPlayer_id());

        return "redirect:/mainPage";
    }
}
