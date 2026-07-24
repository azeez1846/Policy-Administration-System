package com.policycenter.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rating/irpm")
@CrossOrigin(origins = "*")
public class IRPMController {

    @GetMapping
    public Map<String, Object> getIRPMFactors() {
        Map<String, Object> res = new HashMap<>();
        res.put("basePremium", 2400.0);
        res.put("appliedIRPMPct", -15.0);
        res.put("modifiedPremium", 2040.0);
        res.put("categories", List.of(
            Map.of("category", "Premises & Building Maintenance", "maxCredit", -10.0, "maxDebit", 10.0, "applied", -7.0, "reason", "Superior housekeeping & alarm systems"),
            Map.of("category", "Management Experience & Safety Program", "maxCredit", -10.0, "maxDebit", 10.0, "applied", -5.0, "reason", "Formal safety training program in place"),
            Map.of("category", "Equipment & Machinery Condition", "maxCredit", -5.0, "maxDebit", 5.0, "applied", -3.0, "reason", "Annual preventive maintenance schedule")
        ));
        return res;
    }

    @PostMapping
    public Map<String, Object> applyIRPMOverride(@RequestBody Map<String, Object> payload) {
        double basePremium = 2400.0;
        double irpmPct = -15.0;
        if (payload != null && payload.containsKey("irpmPct")) {
            try {
                irpmPct = Double.parseDouble(payload.get("irpmPct").toString());
            } catch (NumberFormatException | NullPointerException ignored) {}
        }

        double modified = basePremium * (1.0 + (irpmPct / 100.0));

        Map<String, Object> res = new HashMap<>();
        res.put("basePremium", basePremium);
        res.put("appliedIRPMPct", irpmPct);
        res.put("modifiedPremium", Math.round(modified * 100.0) / 100.0);
        res.put("status", "SUCCESS");
        res.put("auditLog", "IRPM factor override of " + irpmPct + "% applied by Senior UW 'su' at " + java.time.LocalTime.now().toString().substring(0, 5));
        return res;
    }
}
