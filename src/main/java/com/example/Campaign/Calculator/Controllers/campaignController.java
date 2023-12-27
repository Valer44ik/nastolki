package com.example.Campaign.Calculator.Controllers;

import com.example.Campaign.Calculator.models.*;
import com.example.Campaign.Calculator.repo.MechChasisRepository;
import com.example.Campaign.Calculator.repo.MechChasisService;
import com.example.Campaign.Calculator.repo.PilotService;
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

    private final PilotService pilotService;
    private final MechChasisService mechChasisService;

    public campaignController(PilotService pilotService, MechChasisService mechChasisService) {
        this.pilotService = pilotService;
        this.mechChasisService = mechChasisService;
    }

    @Autowired
    private MechChasisRepository mechChasisRepository;

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

    @GetMapping("/startNewMatch11")
    public String startNewMatch(Model model)
    {
        model.addAttribute("title", "start new match");
        return "startNewMatch";
    }

    @GetMapping("/createPilotAndMech")
    public String createPilotAndMech(Model model)
    {
        model.addAttribute("title", "create pilot and mech");
        return "createPilotAndMech";
    }

    @GetMapping("/startNewMatch")
    public String selectPilot(Model model) {
        model.addAttribute("title", "start new match");

        List<Pilot> pilot = new ArrayList<>();
        List<Pilot> pilots = pilotService.getAllPilots();
        model.addAttribute("pilots", pilots);

        List<MechChasis> mechChasis = new ArrayList<>();
        List<MechChasis> mechChases = mechChasisService.getAllMechChasis();
        model.addAttribute("mechChases", mechChases);
        return "startNewMatch";
    }

    @PostMapping("/createPilotAndMech")
    public String pilotMechAdd(@RequestParam String modelName, @RequestParam String chasisName,
                               @RequestParam int modelWeigth, @RequestParam int battleValue,
                               Model model) {
        MechModel mechModel = new MechModel(modelName, modelWeigth);
        MechClass mechClass = new MechClass();
       // MechChasis mechChasis = new MechChasis(chasisName);



        Mech mech = new Mech();
      //  postRepository.save(post);
        return "redirect:/blog";
    }

    @GetMapping("/createMechChasis")
    public String createMechChasis(Model model)
    {
        model.addAttribute("title", "create mech chasis");
        return "createMechChasis";
    }

    @PostMapping("/createMechChasis")
    public String createChasis(@RequestParam String chasisName, @RequestParam int chasisWeight, Model model){
        MechChasis mechChasis = new MechChasis(chasisName, chasisWeight);
        mechChasisRepository.save(mechChasis);
        return "/createMechChasis";
    }
}
