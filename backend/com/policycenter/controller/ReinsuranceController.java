package com.policycenter.controller;

import com.policycenter.gs.classes.reinsurance.ReinsuranceEngine;
import com.policycenter.model.ReinsuranceTreaty;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reinsurance")
@CrossOrigin(origins = "*")
public class ReinsuranceController {

    @GetMapping("/treaties")
    public List<ReinsuranceTreaty> getTreaties() {
        return ReinsuranceEngine.getAllTreaties();
    }

    @PostMapping("/treaties")
    public ReinsuranceTreaty addTreaty(@RequestBody ReinsuranceTreaty treaty) {
        return ReinsuranceEngine.addTreaty(treaty);
    }

    @PostMapping("/calculate-cession")
    public Map<String, Object> calculateCession(@RequestBody Map<String, Object> payload) {
        double grossPremium = Double.parseDouble(payload.getOrDefault("grossPremium", "2400.00").toString());
        double buildingLimit = Double.parseDouble(payload.getOrDefault("buildingLimit", "1000000.00").toString());
        return ReinsuranceEngine.calculateCession(grossPremium, buildingLimit);
    }
}
