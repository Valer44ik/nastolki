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
    private MechModelRepository mechModelRepository;

    @Autowired
    private MechStatusRepository mechStatusRepository;

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
        List<MechChasis> mechChasis = new ArrayList<>();
        List<MechChasis> mechChases = (List<MechChasis>) mechChasisRepository.findAll();
        model.addAttribute("mechChases", mechChases);
        return "createMech";
    }

    @PostMapping("/createMech")
    public String createMech(@RequestParam int modelWeight, @RequestParam String mechName,
                             @RequestParam int battleValue, Model model) {
        MechModel mechModel = new MechModel(modelWeight);
        mechModelRepository.save(mechModel);
        long modelId = mechModel.getMechModel_id();


        MechStatus mechStatus = mechStatusRepository.findByName("Ready");
        if (mechStatus == null) {
            mechStatus = new MechStatus("Ready");
            mechStatusRepository.save(mechStatus);
        }

        Mech mech = new Mech(mechName, mechStatus, battleValue);
        mechRepository.save(mech);
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
            pilotStatusRepository.save(pilotStatus);
        }

        Pilot pilot = new Pilot(pilotName, pilotSurname, pilotNickname, pilotRank, pilotStatus);
        pilotRepository.save(pilot);
        return "redirect:/assignPilotToMech";
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
