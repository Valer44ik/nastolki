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
        return "createMech";
    }

    @PostMapping("/createMech")
    public String createMech(@RequestParam Long mechChasis_id,@RequestParam int weight, @RequestParam String mechName,
                             @RequestParam int battleValue, Model model) {
        MechStatus mechStatus = mechStatusRepository.findByName("Ready");
        if (mechStatus == null) {
            mechStatus = new MechStatus("Ready");
            mechStatusRepository.save(mechStatus);
        }

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
            return "createMech";
        }
        Mech mech = new Mech(mechName, mechStatus, mechClass, battleValue, weight);
        mechRepository.save(mech);

        MechChasis mechChasis = mechChasisRepository.findById(mechChasis_id).orElse(null);
        assert mechChasis != null;
        mechChasis.setMech(mech);
        mechChasisRepository.save(mechChasis);

        return "/createMech";
    }

    @GetMapping("/createPilot")
    public String createPilot(Model model)
    {
        model.addAttribute("title", "create pilot");
        return "createPilot";
    }

    @PostMapping("/createPilot")
    public String createPilot(@RequestParam String pilotName, @RequestParam String pilotSurname,
                              @RequestParam String pilotNickname,  Model model) {
        PilotStatus pilotStatus = pilotStatusRepository.findByName("Ready");
        if (pilotStatus == null) {
            pilotStatus = new PilotStatus("Ready");
            pilotStatusRepository.save(pilotStatus);
        }

        PilotRank pilotRank = pilotRankRepository.findByName("novice");
        if (pilotRank == null) {
            pilotRank = new PilotRank("novice");
            pilotRankRepository.save(pilotRank);
        }

        Pilot pilot = new Pilot(pilotName, pilotSurname, pilotNickname, pilotRank, pilotStatus);
        pilotRepository.save(pilot);
        return "createPilot";
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
}
