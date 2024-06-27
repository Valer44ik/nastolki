package com.example.Campaign.Calculator.Controllers;

import com.example.Campaign.Calculator.models.*;
import com.example.Campaign.Calculator.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;

@Controller
@SessionAttributes("loggedInUser")
public class matchController {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private PilotRepository pilotRepository;

    @Autowired
    private PilotStatusRepository pilotStatusRepository;

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
        List<Mech> firstPlayerMechs = new ArrayList<>();
        List<Mech> secondPlayerMechs = new ArrayList<>();
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
        if (matchName == null || matchName.isEmpty() || firstPlayerPilots_id == null ||
                firstPlayerPilots_id.isEmpty() || firstPlayerMechs_id == null ||
                firstPlayerMechs_id.isEmpty() || secondPlayerPilots_id == null ||
                secondPlayerPilots_id.isEmpty() || secondPlayerMechs_id == null ||
                secondPlayerMechs_id.isEmpty() || mainTasksTextForPlayer1 == null ||
                mainTasksTextForPlayer1.size() < 2 || secondaryTasksTextForPlayer1 == null ||
                secondaryTasksTextForPlayer1.size() < 2 || mainTasksTextForPlayer2 == null ||
                mainTasksTextForPlayer2.size() < 2 || secondaryTasksTextForPlayer2 == null ||
                secondaryTasksTextForPlayer2.size() < 2) {
            redirectAttributes.addFlashAttribute("error", "Incorrect match details, please try again");
            return "redirect:/startFirstMatch";
        }

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

            Pilot pilot = pilotRepository.findById(firstPlayerPilots_id.get(i)).orElse(null);
            pilot.setCurrentlyInCampaign(true);

            Mech mech = mechRepository.findById(firstPlayerMechs_id.get(i)).orElse(null);
            mech.setCurrentlyInCampaign(true);

            pilotRepository.save(pilot);
            mechRepository.save(mech);
        }
        for(int i = 0; i < secondPlayerPilots_id.size(); i++) {
            matchRepository.bindPilotsAndMechsToMatch(match_id, secondPlayerPilots_id.get(i), secondPlayerMechs_id.get(i));
            Pilot pilot = pilotRepository.findById(secondPlayerPilots_id.get(i)).orElse(null);
            pilot.setCurrentlyInCampaign(true);

            Mech mech = mechRepository.findById(secondPlayerMechs_id.get(i)).orElse(null);
            mech.setCurrentlyInCampaign(true);

            pilotRepository.save(pilot);
            mechRepository.save(mech);
        }

        List<MainTask> firstUserMainTasks = new ArrayList<>();
        List<MainTask> secondUserMainTasks = new ArrayList<>();
        List<SecondaryTask> firstUserSecondaryTasks = new ArrayList<>();
        List<SecondaryTask> secondUserSecondaryTasks = new ArrayList<>();
        MainTask mainTask;
        SecondaryTask secondaryTask;

        for(String text : mainTasksTextForPlayer1) {
            mainTask = new MainTask(text, match, player1);
            mainTaskRepository.save(mainTask);
            firstUserMainTasks.add(mainTask);
        }
        for(String text : secondaryTasksTextForPlayer1) {
            secondaryTask = new SecondaryTask(text, match, player1);
            secondaryTaskRepository.save(secondaryTask);
            firstUserSecondaryTasks.add(secondaryTask);
        }

        for(String text : mainTasksTextForPlayer2) {
            mainTask = new MainTask(text, match, player2);
            mainTaskRepository.save(mainTask);
            secondUserMainTasks.add(mainTask);
        }
        for(String text : secondaryTasksTextForPlayer2) {
            secondaryTask = new SecondaryTask(text, match, player2);
            secondaryTaskRepository.save(secondaryTask);
            secondUserSecondaryTasks.add(secondaryTask);
        }

        redirectAttributes.addAttribute("campaign_id", campaign_id);
        redirectAttributes.addAttribute("match_id", match_id);
        return "redirect:/playMatch";
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

        List<Long> firstPlayerPilotsReadyForCampaign = pilotRepository.findPilotsReadyForMatch(player1.getPlayer_id());
        List<Long> secondPlayerPilotsReadyForCampaign = pilotRepository.findPilotsReadyForMatch(player2.getPlayer_id());

        List<Long> firstPlayerPilotsParticipatingInCampaign = pilotRepository.findReadyPilotsParticipatingInCampaign
                (campaign_id, player1.getPlayer_id());
        List<Long> secondPlayerPilotsParticipatingInCampaign = pilotRepository.findReadyPilotsParticipatingInCampaign
                (campaign_id, player2.getPlayer_id());

        List<Long> allAvailableFirstPlayerPilots = new ArrayList<>();
        List<Long> allAvailableSecondPlayerPilots = new ArrayList<>();

        allAvailableFirstPlayerPilots.addAll(firstPlayerPilotsReadyForCampaign);
        allAvailableFirstPlayerPilots.addAll(firstPlayerPilotsParticipatingInCampaign);

        allAvailableSecondPlayerPilots.addAll(secondPlayerPilotsReadyForCampaign);
        allAvailableSecondPlayerPilots.addAll(secondPlayerPilotsParticipatingInCampaign);

        List<Pilot> firstPlayerPilots = new ArrayList<>();
        List<Pilot> secondPlayerPilots = new ArrayList<>();
        for(Long pilot_id : allAvailableFirstPlayerPilots) {
            Pilot pilot = pilotRepository.findById(pilot_id).orElse(null);
            firstPlayerPilots.add(pilot);
        }
        for(Long pilot_id : allAvailableSecondPlayerPilots) {
            Pilot pilot = pilotRepository.findById(pilot_id).orElse(null);
            secondPlayerPilots.add(pilot);
        }
        model.addAttribute("firstPlayerPilots", firstPlayerPilots);
        model.addAttribute("secondPlayerPilots", secondPlayerPilots);

        List<Long> firstPlayerMechsReadyForCampaign = mechRepository.findMechsReadyForMatch(player1.getPlayer_id());
        List<Long> secondPlayerMechsReadyForCampaign = mechRepository.findMechsReadyForMatch(player2.getPlayer_id());

        List<Long> firstPlayerMechsParticipatingInCampaign = mechRepository.findReadyMechsParticipatingInCampaign
                (campaign_id, player1.getPlayer_id());
        List<Long> secondPlayerMechsParticipatingInCampaign = mechRepository.findReadyMechsParticipatingInCampaign
                (campaign_id, player2.getPlayer_id());

        List<Long> allAvailableFirstPlayerMechs = new ArrayList<>();
        List<Long> allAvailableSecondPlayerMechs = new ArrayList<>();

        allAvailableFirstPlayerMechs.addAll(firstPlayerMechsReadyForCampaign);
        allAvailableFirstPlayerMechs.addAll(firstPlayerMechsParticipatingInCampaign);

        allAvailableSecondPlayerMechs.addAll(secondPlayerMechsReadyForCampaign);
        allAvailableSecondPlayerMechs.addAll(secondPlayerMechsParticipatingInCampaign);

        List<Mech> firstPlayerMechs = new ArrayList<>();
        List<Mech> secondPlayerMechs = new ArrayList<>();
        for(Long mech_id : allAvailableFirstPlayerMechs) {
            Mech mech = mechRepository.findById(mech_id).orElse(null);
            firstPlayerMechs.add(mech);
        }
        for(Long mech_id : allAvailableSecondPlayerMechs) {
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
    public String startNewMatch(@RequestParam String matchName, @RequestParam List<Long> firstPlayerPilots_id,
                                @RequestParam List<Long> firstPlayerMechs_id,
                                @RequestParam List<Long> secondPlayerPilots_id,
                                @RequestParam List<Long> secondPlayerMechs_id,
                                @RequestParam List<String> mainTasksTextForPlayer1,
                                @RequestParam List<String> secondaryTasksTextForPlayer1,
                                @RequestParam List<String> mainTasksTextForPlayer2,
                                @RequestParam List<String> secondaryTasksTextForPLayer2,
                                @RequestParam Long campaign_id, RedirectAttributes redirectAttributes,
                                Model model) {
        if (matchName == null || matchName.isEmpty() || firstPlayerPilots_id == null ||
                firstPlayerPilots_id.isEmpty() || firstPlayerMechs_id == null ||
                firstPlayerMechs_id.isEmpty() || secondPlayerPilots_id == null ||
                secondPlayerPilots_id.isEmpty() || secondPlayerMechs_id == null ||
                secondPlayerMechs_id.isEmpty() || mainTasksTextForPlayer1 == null ||
                mainTasksTextForPlayer1.size() < 2 || secondaryTasksTextForPlayer1 == null ||
                secondaryTasksTextForPlayer1.size() < 2 || mainTasksTextForPlayer2 == null ||
                mainTasksTextForPlayer2.size() < 2 || secondaryTasksTextForPLayer2 == null ||
                secondaryTasksTextForPLayer2.size() < 2) {
            redirectAttributes.addFlashAttribute("error", "Incorrect match details, please try again");
            return "redirect:/startNewMatch";
        }

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

            Pilot pilot = pilotRepository.findById(firstPlayerPilots_id.get(i)).orElse(null);
            pilot.setCurrentlyInCampaign(true);

            Mech mech = mechRepository.findById(firstPlayerMechs_id.get(i)).orElse(null);
            mech.setCurrentlyInCampaign(true);
        }
        for(int i = 0; i < secondPlayerPilots_id.size(); i++) {
            matchRepository.bindPilotsAndMechsToMatch(match_id, secondPlayerPilots_id.get(i), secondPlayerMechs_id.get(i));

            Pilot pilot = pilotRepository.findById(secondPlayerPilots_id.get(i)).orElse(null);
            pilot.setCurrentlyInCampaign(true);

            Mech mech = mechRepository.findById(secondPlayerMechs_id.get(i)).orElse(null);
            mech.setCurrentlyInCampaign(true);
        }

        List<MainTask> firstUserMainTasks = new ArrayList<>();
        List<MainTask> secondUserMainTasks = new ArrayList<>();
        List<SecondaryTask> firstUserSecondaryTasks = new ArrayList<>();
        List<SecondaryTask> secondUserSecondaryTasks = new ArrayList<>();
        MainTask mainTask;
        SecondaryTask secondaryTask;

        for(String text : mainTasksTextForPlayer1) {
            mainTask = new MainTask(text, match, player1);
            mainTaskRepository.save(mainTask);
            firstUserMainTasks.add(mainTask);
        }
        for(String text : secondaryTasksTextForPlayer1) {
            secondaryTask = new SecondaryTask(text, match, player1);
            secondaryTaskRepository.save(secondaryTask);
            firstUserSecondaryTasks.add(secondaryTask);
        }

        for(String text : mainTasksTextForPlayer2) {
            mainTask = new MainTask(text, match, player2);
            mainTaskRepository.save(mainTask);
            secondUserMainTasks.add(mainTask);
        }
        for(String text : secondaryTasksTextForPLayer2) {
            secondaryTask = new SecondaryTask(text, match, player2);
            secondaryTaskRepository.save(secondaryTask);
            secondUserSecondaryTasks.add(secondaryTask);
        }

        redirectAttributes.addAttribute("campaign_id", campaign_id);
        redirectAttributes.addAttribute("match_id", match_id);
        return "redirect:/playMatch";
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
        model.addAttribute("title", "play match");
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

        model.addAttribute("player1", player1);
        model.addAttribute("player2", player2);

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

        // creating total value variables for player 1 and 2
        int firstPlayerTotalBattleValue = 0;
        int secondPlayerTotalBattleValue = 0;

        List<MatchPilotAndMech> firstPlayerMatchPilotsAndMechs = new ArrayList<>();
        List<MatchPilotAndMech> secondPlayerMatchPilotsAndMechs = new ArrayList<>();
        for(int i = 0; i < firstPlayerPilots.size(); i++) {
            // finding mech by mech_id
            List<Long> mech_id = mechRepository.findMechByMatchAndPilot_id(match.getMatch_id(),
                    firstPlayerPilots.get(i).getPilot_id());
            Mech mech = mechRepository.findById(mech_id.get(0)).orElse(null);

            // adding mech battle value to total battle value count
            firstPlayerTotalBattleValue += mech.getBattleValue();

            // creating match_pilot_and_mech variable and adding it to the list
            MatchPilotAndMech matchPilotAndMech = new MatchPilotAndMech(firstPlayerPilots.get(i), mech);
            firstPlayerMatchPilotsAndMechs.add(matchPilotAndMech);
        }
        for(int i = 0; i < secondPlayerPilots.size(); i++) {
            // finding mech by mech_id
            List<Long> mech_id = mechRepository.findMechByMatchAndPilot_id(match.getMatch_id(),
                    secondPlayerPilots.get(i).getPilot_id());
            Mech mech = mechRepository.findById(mech_id.get(0)).orElse(null);

            // adding mech battle value to total battle value count
            secondPlayerTotalBattleValue += mech.getBattleValue();

            // creating match_pilot_and_mech variable and adding it to the list
            MatchPilotAndMech matchPilotAndMech = new MatchPilotAndMech(secondPlayerPilots.get(i), mech);
            secondPlayerMatchPilotsAndMechs.add(matchPilotAndMech);
        }

        // adding player 1 and 2 total battle values to model
        model.addAttribute("firstPlayerTotalBattleValue", firstPlayerTotalBattleValue);
        model.addAttribute("secondPlayerTotalBattleValue", secondPlayerTotalBattleValue);

        // adding first and second match pilot and mech lists to the model
        model.addAttribute("firstPlayerMatchPilotsAndMechs", firstPlayerMatchPilotsAndMechs);
        model.addAttribute("secondPlayerMatchPilotsAndMechs", secondPlayerMatchPilotsAndMechs);

        // finding main and secondary tasks for player 1
        List<Long> firstPlayerMainTasks_ids = mainTaskRepository.findMainTasksByPlayerAndMatch
                (match_id, player1.getPlayer_id());
        List<Long> firstPlayerSecondaryTasks_ids = secondaryTaskRepository.findSecondaryTasksByPlayerAndMatch
                (match_id, player1.getPlayer_id());

        // finding main and secondary tasks_ids for player 2
        List<Long> secondPlayerMainTasks_ids = mainTaskRepository.findMainTasksByPlayerAndMatch
                (match_id, player2.getPlayer_id());
        List<Long> secondPlayerSecondaryTasks_ids = secondaryTaskRepository.findSecondaryTasksByPlayerAndMatch
                (match_id, player2.getPlayer_id());

        // creating main and secondary tasks lists for player 1
        List<MainTask> firstPlayerMainTasks = new ArrayList<>();
        List<SecondaryTask> firstPlayerSecondaryTasks = new ArrayList<>();

        // finding main and secondary tasks for player 1 and adding them to the list
        for(int i = 0; i < firstPlayerMainTasks_ids.size(); i ++) {
            MainTask mainTask = mainTaskRepository.findById
                    (firstPlayerMainTasks_ids.get(i)).orElse(null);
            SecondaryTask secondaryTask = secondaryTaskRepository.findById
                    (firstPlayerSecondaryTasks_ids.get(i)).orElse(null);
            firstPlayerMainTasks.add(mainTask);
            firstPlayerSecondaryTasks.add(secondaryTask);
        }

        // adding player 1 main and secondary tasks to the model
        model.addAttribute("firstPlayerMainTasks", firstPlayerMainTasks);
        model.addAttribute("firstPlayerSecondaryTasks", firstPlayerSecondaryTasks);

        // creating main and secondary tasks lists for player 2
        List<MainTask> secondPlayerMainTasks = new ArrayList<>();
        List<SecondaryTask> secondPlayerSecondaryTasks = new ArrayList<>();

        // finding main and secondary tasks for player 1 and adding them to the list
        for(int i = 0; i < secondPlayerMainTasks_ids.size(); i ++) {
            MainTask mainTask = mainTaskRepository.findById
                    (secondPlayerMainTasks_ids.get(i)).orElse(null);
            SecondaryTask secondaryTask = secondaryTaskRepository.findById
                    (secondPlayerSecondaryTasks_ids.get(i)).orElse(null);
            secondPlayerMainTasks.add(mainTask);
            secondPlayerSecondaryTasks.add(secondaryTask);
        }

        //
        model.addAttribute("secondPlayerMainTasks", secondPlayerMainTasks);
        model.addAttribute("secondPlayerSecondaryTasks", secondPlayerSecondaryTasks);

        return "playMatch";
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

        List<Long> firstPlayerMainTasks_ids = mainTaskRepository.findMainTasksByPlayerAndMatch
                (match_id, player1.getPlayer_id());
        List<Long> firstPlayerSecondaryTasks_ids = secondaryTaskRepository.findSecondaryTasksByPlayerAndMatch
                (match_id, player1.getPlayer_id());

        List<Long> secondPlayerMainTasks_ids = mainTaskRepository.findMainTasksByPlayerAndMatch
                (match_id, player2.getPlayer_id());
        List<Long> secondPlayerSecondaryTasks_ids = secondaryTaskRepository.findSecondaryTasksByPlayerAndMatch
                (match_id, player2.getPlayer_id());

        List<MainTask> firstPlayerMainTasks = new ArrayList<>();
        List<SecondaryTask> firstPlayerSecondaryTasks = new ArrayList<>();

        for(int i = 0; i < firstPlayerMainTasks_ids.size(); i ++) {
            MainTask mainTask = mainTaskRepository.findById
                    (firstPlayerMainTasks_ids.get(i)).orElse(null);
            SecondaryTask secondaryTask = secondaryTaskRepository.findById
                    (firstPlayerSecondaryTasks_ids.get(i)).orElse(null);
            firstPlayerMainTasks.add(mainTask);
            firstPlayerSecondaryTasks.add(secondaryTask);
        }

        model.addAttribute("firstPlayerMainTasks", firstPlayerMainTasks);
        model.addAttribute("firstPlayerSecondaryTasks", firstPlayerSecondaryTasks);

        List<MainTask> secondPlayerMainTasks = new ArrayList<>();
        List<SecondaryTask> secondPlayerSecondaryTasks = new ArrayList<>();

        for(int i = 0; i < secondPlayerMainTasks_ids.size(); i ++) {
            MainTask mainTask = mainTaskRepository.findById
                    (secondPlayerMainTasks_ids.get(i)).orElse(null);
            SecondaryTask secondaryTask = secondaryTaskRepository.findById
                    (secondPlayerSecondaryTasks_ids.get(i)).orElse(null);
            secondPlayerMainTasks.add(mainTask);
            secondPlayerSecondaryTasks.add(secondaryTask);
        }

        model.addAttribute("secondPlayerMainTasks", secondPlayerMainTasks);
        model.addAttribute("secondPlayerSecondaryTasks", secondPlayerSecondaryTasks);

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
    public String endMatch(@RequestParam(defaultValue = "0") Long winningPlayer_id,
                           @RequestParam(defaultValue = "0") Long campaign_id,
                           @RequestParam(defaultValue = "0") Long match_id,
                           RedirectAttributes redirectAttributes, Model model){
        if(winningPlayer_id < 1 || campaign_id < 1 || match_id < 1) {
            redirectAttributes.addFlashAttribute("error", "Incorrect details, please try again");
            return "redirect:/endMatch";
        }

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

        match.setWinningPlayer_id(winningPlayer_id);

        List<Long> pilots_id = pilotRepository.findPilotsParticipatingInCampaign(campaign_id);
        for (long pilot_id : pilots_id) {
            Pilot pilot = pilotRepository.findById(pilot_id).orElse(null);
            if(Objects.equals(pilot.getPilotStatus().getName(), "Injured")) {
                if(pilot.getInactiveCount() > 0) {
                    pilot.setInactiveCount((short)(pilot.getInactiveCount()-1));
                }
                else {
                    PilotStatus pilotStatus = pilotStatusRepository.findByName("Ready");
                    pilot.setPilotStatus(pilotStatus);
                }
            }
            pilotRepository.save(pilot);
        }

        matchRepository.save(match);

        return "redirect:/mainPage";
    }
}
