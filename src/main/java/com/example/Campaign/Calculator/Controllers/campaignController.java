package com.example.Campaign.Calculator.Controllers;

import com.example.Campaign.Calculator.models.Mech;
import com.example.Campaign.Calculator.models.MechChasis;
import com.example.Campaign.Calculator.models.MechClass;
import com.example.Campaign.Calculator.models.MechModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class campaignController {

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

    @PostMapping("/createPilotAndMech")
    public String pilotMechAdd(@RequestParam String modelName,
                               @RequestParam String chasisName, @RequestParam int battleValue,
                               Model model) {
        MechModel mechModel = new MechModel(modelName);
        MechClass mechClass = new MechClass();
        MechChasis mechChasis = new MechChasis(chasisName);



        Mech mech = new Mech();
      //  postRepository.save(post);
        return "redirect:/blog";
    }
}
