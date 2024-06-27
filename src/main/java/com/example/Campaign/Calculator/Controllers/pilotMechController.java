package com.example.Campaign.Calculator.Controllers;

import com.example.Campaign.Calculator.models.*;
import com.example.Campaign.Calculator.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@SessionAttributes("loggedInUser")
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
                               @RequestParam(defaultValue = "0") int chasisWeight, Model model){
        if (chasisName == null || chasisName.isEmpty()) {
            model.addAttribute("error", "Incorrect chasis detail, please try again");
            return "createMechChasis";
        }

        if(chasisWeight < 20 || (chasisWeight > 35 && chasisWeight < 40) ||
                (chasisWeight > 55 && chasisWeight < 60) ||
                (chasisWeight >75 && chasisWeight < 80) ||
                chasisWeight > 100) {
            model.addAttribute("error", "Incorrect weight input. " +
                                                                "The correct examples are:" +
                                                                "\n20-35/Light" +
                                                                "\n40-55-Medium" +
                                                                "\n60-75-Heavy" +
                                                                "\n80-100-Assault\n");
            return "createMechChasis";
        }

        MechChasis mechChasis = new MechChasis(chasisName, chasisWeight);
        mechChasisRepository.save(mechChasis);
        return "createMechChasis";
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
                             @RequestParam(defaultValue = "0") int battleValue,
                             @RequestParam(defaultValue = "0") Long mechChasis_id,
                             @RequestParam(defaultValue = "0") Long player_id,
                             RedirectAttributes redirectAttributes, Model model) {
        if (battleValue < 1 || mechName == null || mechName.isEmpty() || mechChasis_id < 1 || player_id < 1) {
            redirectAttributes.addFlashAttribute("error", "Incorrect mech details, please try again");
            return "redirect:/createMech";
        }

        MechStatus mechStatus = mechStatusRepository.findByName("Ready");
        if (mechStatus == null) {
            mechStatus = new MechStatus("Ready");
            mechStatusRepository.save(mechStatus);
            mechStatus = new MechStatus("Damaged");
            mechStatusRepository.save(mechStatus);
            mechStatus = new MechStatus("Broken");
            mechStatusRepository.save(mechStatus);
            mechStatus = new MechStatus("Captured");
            mechStatusRepository.save(mechStatus);

            mechStatus = mechStatusRepository.findByName("Ready");
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
            redirectAttributes.addFlashAttribute("error", "Incorrect weight input. " +
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
    public String createPilot(@RequestParam String pilotName,
                              @RequestParam String pilotSurname,
                              @RequestParam String pilotNickname,
                              @RequestParam(defaultValue = "0") Long player_id,
                              RedirectAttributes redirectAttributes, Model model) {

        if (pilotName == null || pilotName.isEmpty() || pilotSurname == null || pilotSurname.isEmpty()
                || pilotNickname == null || pilotNickname.isEmpty() || player_id < 1) {
            redirectAttributes.addFlashAttribute("error", "Incorrect pilot details, please try again");
            return "redirect:/createPilot";
        }

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

            pilotStatus = pilotStatusRepository.findByName("Ready");
        }

        PilotRank pilotRank = pilotRankRepository.findByName("novice");
        if (pilotRank == null) {
            pilotRank = new PilotRank("novice");
            pilotRankRepository.save(pilotRank);
            pilotRank = new PilotRank("experienced");
            pilotRankRepository.save(pilotRank);
            pilotRank = new PilotRank("master");
            pilotRankRepository.save(pilotRank);
            pilotRank = new PilotRank("squad Leader");
            pilotRankRepository.save(pilotRank);

            pilotRank = pilotRankRepository.findByName("novice");
        }

        Player player = playerRepository.findById(player_id).orElse(null);

        Pilot pilot = new Pilot(pilotName, pilotSurname, pilotNickname, pilotRank, pilotStatus, player);
        pilotRepository.save(pilot);
        return "redirect:/createPilot";
    }

    @GetMapping("/pilotInfoScreen")
    public String viewPilot(@RequestParam Long campaign_id, Long match_id, Long pilot_id, Model model) {
        model.addAttribute("title", "Pilot Info Screen");
        model.addAttribute("campaign_id", campaign_id);
        model.addAttribute("match_id", match_id);

        Pilot pilot = pilotRepository.findById(pilot_id).orElse(null);
        model.addAttribute("pilot", pilot);

        return "pilotInfoScreen";
    }

    @GetMapping("/mechInfoScreen")
    public String viewMech(@RequestParam Long campaign_id, Long match_id, Long mech_id, Model model) {
        model.addAttribute("title", "Mech Info Screen");
        model.addAttribute("campaign_id", campaign_id);
        model.addAttribute("match_id", match_id);

        Mech mech = mechRepository.findById(mech_id).orElse(null);
        model.addAttribute("mech", mech);

        return "mechInfoScreen";
    }

    @GetMapping("/makeChangesInPilot")
    public String showPilot(@RequestParam Long campaign_id, @RequestParam Long match_id,
                            @RequestParam Long pilot_id, Model model) {
        model.addAttribute("title", "Pilot Info Screen");
        model.addAttribute("campaign_id", campaign_id);
        model.addAttribute("match_id", match_id);

        Pilot pilot = pilotRepository.findById(pilot_id).orElse(null);
        model.addAttribute("pilot", pilot);

        List<PilotStatus> pilotStatuses = (List<PilotStatus>) pilotStatusRepository.findAll();
        model.addAttribute("pilotStatuses", pilotStatuses);

        List<PilotRank> pilotRanks = (List<PilotRank>) pilotRankRepository.findAll();
        model.addAttribute("pilotRanks", pilotRanks);

        return "makeChangesInPilot";
    }

    @PostMapping("/makeChangesInPilot")
    public String changePilot(@RequestParam(defaultValue = "0") Long pilotRank_id,
                              @RequestParam(defaultValue = "0") Long pilotStatus_id,
                              @RequestParam(defaultValue = "0") Long campaign_id,
                              @RequestParam(defaultValue = "0") Long pilot_id,
                              @RequestParam(defaultValue = "0") Long match_id,
                              RedirectAttributes redirectAttributes, Model model) {
        if(pilot_id < 1 || pilotRank_id < 1 || pilotStatus_id < 1 || campaign_id < 1 || match_id < 1) {
            redirectAttributes.addFlashAttribute("error", "Incorrect details, please try again");
            return "redirect:/makeChangesInPilot";
        }

        Pilot pilot = pilotRepository.findById(pilot_id).orElse(null);
        model.addAttribute("pilot", pilot);

        PilotStatus pilotStatus = pilotStatusRepository.findById(pilotStatus_id).orElse(null);
        pilot.setPilotStatus(pilotStatus);

        if (Objects.equals(pilotStatus.getName(), "Injured")) {
            pilot.setInactiveCount((short) 3);
        }

        if (Objects.equals(pilotStatus.getName(), "Ready")) {
            pilot.setInactiveCount((short) 0);
        }

        PilotRank pilotRank = pilotRankRepository.findById(pilotRank_id).orElse(null);
        pilot.setPilotRank(pilotRank);

        pilotRepository.save(pilot);

        redirectAttributes.addFlashAttribute("campaign_id", campaign_id);
        redirectAttributes.addFlashAttribute("match_id", match_id);
        return "redirect:/endMatch";
    }

    @GetMapping("/makeChangesInMech")
    public String showMech(@RequestParam Long campaign_id, @RequestParam Long match_id,
                           @RequestParam Long mech_id, Model model) {
        model.addAttribute("title", "Pilot Info Screen");
        model.addAttribute("campaign_id", campaign_id);
        model.addAttribute("match_id", match_id);

        Mech mech = mechRepository.findById(mech_id).orElse(null);
        model.addAttribute("mech", mech);

        List<MechChasis> mechChases = (List<MechChasis>) mechChasisRepository.findAll();
        model.addAttribute("mechChases", mechChases);

        List<MechStatus> mechStatuses = (List<MechStatus>) mechStatusRepository.findAll();
        model.addAttribute("mechStatuses", mechStatuses);

        return "makeChangesInMech";
    }

    @PostMapping("/makeChangesInMech")
    public String changeMech(@RequestParam Long mechChasis_id,
                              @RequestParam Long mechStatus_id,
                              @RequestParam Long campaign_id,
                              @RequestParam Long mech_id,
                              @RequestParam Long match_id,
                              RedirectAttributes redirectAttributes, Model model) {
        if(mechChasis_id < 1 || mechStatus_id < 1 || campaign_id < 1 || mech_id < 1 || match_id < 1) {
            redirectAttributes.addFlashAttribute("error", "Incorrect details, please try again");
            return "redirect:/makeChangesInMech";
        }

        Mech mech = mechRepository.findById(mech_id).orElse(null);
        model.addAttribute("mech", mech);

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
            redirectAttributes.addFlashAttribute("error", "Incorrect weight input. " +
                    "The correct examples are:" +
                    "\n20-35/Light" +
                    "\n40-55-Medium" +
                    "\n60-75-Heavy" +
                    "\n80-100-Assault\n");
            return "redirect:/makeChangesInMech";
        }
        mech.setMechClass(mechClass);

        MechStatus mechStatus = mechStatusRepository.findById(mechStatus_id).orElse(null);
        mech.setMechStatus(mechStatus);

        mechRepository.save(mech);

        redirectAttributes.addFlashAttribute("campaign_id", campaign_id);
        redirectAttributes.addFlashAttribute("match_id", match_id);
        return "redirect:/endMatch";
    }

    @GetMapping("/pilotList")
    public String viewAllPilots(Model model) {
        model.addAttribute("title", "Pilot List");

        List<Pilot> pilots = (List<Pilot>)pilotRepository.findAll();

        model.addAttribute("pilots", pilots);

        return "pilotList";
    }
}
