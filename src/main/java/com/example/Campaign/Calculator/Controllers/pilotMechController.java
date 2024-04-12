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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class pilotMechController {

    @Autowired
    private MechStatusRepository mechStatusRepository;

    @Autowired
    private MechClassRepository mechClassRepository;

    @Autowired
    private MechRepository mechRepository;

    @Autowired
    private PilotStatusRepository pilotStatusRepository;

    @Autowired
    private PilotRankRepository pilotRankRepository;

    @Autowired
    private MechChasisRepository mechChasisRepository;

    @Autowired
    private PilotRepository pilotRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping("/createMechChasis")
    public String createMechChasis(Model model)
    {
        model.addAttribute("title", "create mech chasis");
        return "createMechChasis";
    }

    @PostMapping("/createMechChasis")
    public String createChasis(@RequestParam String chasisName,
                               @RequestParam int chasisWeight, Model model){
        MechChasis mechChasis = new MechChasis(chasisName, chasisWeight);
        mechChasisRepository.save(mechChasis);
        return "/createMechChasis";
    }

    @GetMapping("/createMech")
    public String createMech(Model model)
    {
        model.addAttribute("title", "create mech");
        List<MechChasis> mechChases = (List<MechChasis>)mechChasisRepository.findAll();
        model.addAttribute("mechChases", mechChases);
        List<Player> players = (List<Player>) playerRepository.findAll();
        model.addAttribute("players", players);
        return "createMech";
    }

    @PostMapping("/createMech")
    public String createMech(@RequestParam String mechName,
                             @RequestParam int battleValue, @RequestParam Long mechChasis_id,
                             @RequestParam Long player_id, Model model) {
        MechStatus mechStatus = mechStatusRepository.findByName("Ready");
        if (mechStatus == null) {
            mechStatus = new MechStatus("Ready");
            mechStatusRepository.save(mechStatus);
        }

        MechChasis mechChasis = mechChasisRepository.findById(mechChasis_id).orElse(null);
        assert mechChasis != null;

        int weight = mechChasis.getChasisWeight();

        MechClass mechClass;
        if (weight >= 20 && weight <= 35) {
            mechClass = mechClassRepository.findByClassName("Light");
            if (mechClass == null) {
                mechClass = new MechClass("Light", 20, 35);
                mechClassRepository.save(mechClass);
            }
        } else if (weight >= 40 && weight <= 55) {
            mechClass = mechClassRepository.findByClassName("Medium");
            if (mechClass == null) {
                mechClass = new MechClass("Medium", 40, 55);
                mechClassRepository.save(mechClass);
            }
        } else if (weight >= 60 && weight <= 75) {
            mechClass = mechClassRepository.findByClassName("Heavy");
            if (mechClass == null) {
                mechClass = new MechClass("Heavy", 60, 75);
                mechClassRepository.save(mechClass);
            }
        } else if (weight >= 80 && weight <= 100) {
            mechClass = mechClassRepository.findByClassName("Assault");
            if (mechClass == null) {
                mechClass = new MechClass("Assault", 80, 100);
                mechClassRepository.save(mechClass);
            }
        } else {
            model.addAttribute("errorMessage", "Incorrect weight input. " +
                    "The correct examples are:" +
                    "\n20-35/Light" +
                    "\n40-55-Medium" +
                    "\n60-75-Heavy" +
                    "\n80-100-Assault\n");
            return "redirect:/createMech";
        }

        Player player = playerRepository.findById(player_id).orElse(null);

        Mech mech = new Mech(mechName, mechStatus, mechClass, battleValue, player, mechChasis);
        mechRepository.save(mech);

        return "redirect:/createMech";
    }

    @GetMapping("/createPilot")
    public String createPilot(Model model)
    {
        model.addAttribute("title", "create pilot");
        List<Player> players = (List<Player>) playerRepository.findAll();
        model.addAttribute("players", players);
        return "createPilot";
    }

    @PostMapping("/createPilot")
    public String createPilot(@RequestParam String pilotName, @RequestParam String pilotSurname,
                              @RequestParam String pilotNickname, @RequestParam Long player_id,  Model model) {
        PilotStatus pilotStatus = pilotStatusRepository.findByName("Ready");
        if (pilotStatus == null) {
            pilotStatus = new PilotStatus("Ready");
            pilotStatusRepository.save(pilotStatus);
            pilotStatus = new PilotStatus("Injured");
            pilotStatusRepository.save(pilotStatus);
            pilotStatus = new PilotStatus("Dead");
            pilotStatusRepository.save(pilotStatus);
            pilotStatus = new PilotStatus("Captured");
            pilotStatusRepository.save(pilotStatus);
        }

        PilotRank pilotRank = pilotRankRepository.findByName("novice");
        if (pilotRank == null) {
            pilotRank = new PilotRank("novice");
            pilotRankRepository.save(pilotRank);
        }

        Player player = playerRepository.findById(player_id).orElse(null);

        Pilot pilot = new Pilot(pilotName, pilotSurname, pilotNickname, pilotRank, pilotStatus, player);
        pilotRepository.save(pilot);
        return "redirect:/createPilot";
    }

    @GetMapping("/pilotInfoScreen")
    public String viewPilot(@RequestParam Long campaign_id, Model model) {
        model.addAttribute("title", "Pilot Info Screen");
        model.addAttribute("campaign_id", campaign_id);

        return "pilotInfoScreen";
    }

    @GetMapping("/mechInfoScreen")
    public String viewMech(@RequestParam Long campaign_id, Model model) {
        model.addAttribute("title", "Mech Info Screen");
        model.addAttribute("campaign_id", campaign_id);

        return "mechInfoScreen";
    }

    @GetMapping("/makeChangesInPilot")
    public String showPilot(@RequestParam Long campaign_id, @RequestParam Long pilot_id, Model model) {
        model.addAttribute("title", "Pilot Info Screen");
        model.addAttribute("campaign_id", campaign_id);

        Pilot pilot = pilotRepository.findById(pilot_id).orElse(null);
        model.addAttribute("pilot", pilot);

        List<PilotStatus> pilotStatuses = (List<PilotStatus>) pilotStatusRepository.findAll();
        model.addAttribute("pilotStatuses", pilotStatuses);

        List<PilotRank> pilotRanks = (List<PilotRank>) pilotRankRepository.findAll();
        model.addAttribute("pilotRanks", pilotRanks);

        return "makeChangesInPilot";
    }

    @PostMapping("/makeChangesInPilot")
    public String changePilot(@RequestParam Long pilotRank_id,
                              @RequestParam Long pilotStatus_id,
                              @RequestParam Long campaign_id,
                              @RequestParam Long pilot_id,
                              RedirectAttributes redirectAttributes, Model model) {

        Pilot pilot = pilotRepository.findById(pilot_id).orElse(null);
        model.addAttribute("pilot", pilot);

        PilotStatus pilotStatus = pilotStatusRepository.findById(pilotStatus_id).orElse(null);
        pilot.setPilotStatus(pilotStatus);

        PilotRank pilotRank = pilotRankRepository.findById(pilotRank_id).orElse(null);
        pilot.setPilotRank(pilotRank);

        pilotRepository.save(pilot);

        redirectAttributes.addAttribute("campaign_id", campaign_id);
        return "redirect:/endCampaign";
    }

    @GetMapping("/makeChangesInMech")
    public String showMech(@RequestParam Long campaign_id, @RequestParam Long mech_id, Model model) {
        model.addAttribute("title", "Pilot Info Screen");
        model.addAttribute("campaign_id", campaign_id);

        Mech mech = mechRepository.findById(mech_id).orElse(null);
        model.addAttribute("mech", mech);

        return "makeChangesInMech";
    }
}
