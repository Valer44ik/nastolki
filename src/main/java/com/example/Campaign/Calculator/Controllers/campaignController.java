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

@Controller
public class campaignController {

    @Autowired
    private MechChasisRepository mechChasisRepository;

    @Autowired
    private MechModelRepository mechModelRepository;

    @Autowired
    private MechStatusRepository mechStatusRepository;

    @Autowired
    private MechRepository mechRepository;

    @Autowired
    private PilotStatusRepository pilotStatusRepository;

    @Autowired
    private PilotRepository pilotRepository;

    @Autowired
    private PilotRankRepository pilotRankRepository;


    @GetMapping("/")
    public String mainPage(Model model)
    {
        model.addAttribute("title", "CampaignList");
        return "campaignList";
    }

    @GetMapping("/startACampaign")
    public String startACampaign(Model model)
    {
        model.addAttribute("title", "start a campaign");
        return "startACampaign";
    }

    @GetMapping("/campaignSheet")
    public String campaignSheet(Model model)
    {
        model.addAttribute("title", "campaign sheet");
        return "campaignSheet";
    }

    @GetMapping("/assignPilotToMech")
    public String createPilotAndMech(Model model)
    {
        model.addAttribute("title", "Assign pilot to mech");

        List<Pilot> pilot = new ArrayList<>();
        List<Pilot> pilots = (List<Pilot>) pilotRepository.findAll();
        model.addAttribute("pilots", pilots);

        List<Mech> mech = new ArrayList<>();
        List<Mech> mechs = (List<Mech>) mechRepository.findAll();
        model.addAttribute("mechs", mechs);

        return "assignPilotToMech";
    }

    @PostMapping("/assignPilotToMech")
    public String assignPilotToMech(Model model) {




        Mech mech = new Mech();
        //  postRepository.save(post);
        return "redirect:/";
    }

    @GetMapping("/startNewMatch")
    public String startNewMatch(Model model) {
        model.addAttribute("title", "start new match");

        List<Pilot> pilot = new ArrayList<>();
        List<Pilot> pilots = (List<Pilot>) pilotRepository.findAll();
        model.addAttribute("pilots", pilots);

        List<MechChasis> mechChasis = new ArrayList<>();
        List<MechChasis> mechChases = (List<MechChasis>) mechChasisRepository.findAll();
        model.addAttribute("mechChases", mechChases);
        return "startNewMatch";
    }

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
    public String createMech(@RequestParam String modelName, @RequestParam int modelWeight,
                             @RequestParam int battleValue, Model model) {
        MechModel mechModel = new MechModel(modelName, modelWeight);
        long modelId = mechModel.getMechModel_id();
        mechModelRepository.save(mechModel);

        MechStatus mechStatus = mechStatusRepository.findByName("Ready");
        if (mechStatus == null) {
            mechStatus = new MechStatus("Ready");
            mechStatusRepository.save(mechStatus);
        }

        Mech mech = new Mech(mechModel, mechStatus, battleValue);
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

}
