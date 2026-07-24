package com.policycenter.controller;

import com.policycenter.gs.classes.job.OOSEngine;
import com.policycenter.model.PolicyVersion;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/policies/history")
@CrossOrigin(origins = "*")
public class OOSController {

    @GetMapping
    public List<PolicyVersion> getPolicyHistory(@RequestParam(name = "policyNumber", defaultValue = "POL-88201") String policyNumber) {
        return OOSEngine.getHistoryForPolicy(policyNumber);
    }

    @PostMapping("/oos-endorse")
    public PolicyVersion createOOSEndorsement(@RequestBody Map<String, String> payload) {
        String policyNumber = payload.getOrDefault("policyNumber", "POL-88201");
        String effDate = payload.get("effectiveDate");
        String description = payload.getOrDefault("description", "Retroactive Exposure Adjustment");
        return OOSEngine.executeOOSEndorsement(policyNumber, effDate, description);
    }
}
