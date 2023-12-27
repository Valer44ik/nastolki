package com.example.Campaign.Calculator.Controllers;

import com.example.Campaign.Calculator.models.MechChasis;
import com.example.Campaign.Calculator.models.Pilot;
import com.example.Campaign.Calculator.repo.PilotService;
import com.example.Campaign.Calculator.repo.MechChasisService;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class selectController {
    private final PilotService pilotService;
    private final MechChasisService mechChasisService;

    public selectController(PilotService pilotService, MechChasisService mechChasisService) {
        this.pilotService = pilotService;
        this.mechChasisService = mechChasisService;
    }
/*
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
*/
}
