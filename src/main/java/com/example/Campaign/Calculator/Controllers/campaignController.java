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
import java.time.LocalDate;

@Controller
public class campaignController {

    @Autowired
    private MechChasisRepository mechChasisRepository;

    @Autowired
    private PilotRepository pilotRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @GetMapping("/")
    public String mainPage(Model model)
    {
        model.addAttribute("title", "CampaignList");

        List<Campaign> campaign = new ArrayList<>();
        List<Campaign> campaigns = (List<Campaign>) campaignRepository.findAll();
        model.addAttribute("campaigns", campaigns);

        return "campaignList";
    }

    @GetMapping("/startACampaign")
    public String startACampaign(Model model)
    {
        model.addAttribute("title", "start a campaign");

        model.addAttribute("campaignTypes", CampaignType.values());

        model.addAttribute("formationOrders", FormationOrder.values());

        return "startACampaign";
    }

    @PostMapping("/startACampaign")
    public String createCampaign(@RequestParam String campaignName, @RequestParam CampaignType campaignType,
                                 @RequestParam FormationOrder formationOrder, @RequestParam int battleValue, Model model)
    {
        model.addAttribute("title", "start a campaign");
        LocalDate startDate = LocalDate.now();
        Campaign campaign = new Campaign(campaignName, campaignType, formationOrder, battleValue, startDate);
        campaignRepository.save(campaign);
        return "startACampaign";
    }

    @GetMapping("/campaignSheet")
    public String campaignSheet(Model model)
    {
        model.addAttribute("title", "campaign sheet");
        return "campaignSheet";
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
}
