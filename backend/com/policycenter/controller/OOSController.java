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

    @GetMapping("/diff")
    public Map<String, Object> comparePolicyVersions(
            @RequestParam(name = "policyNumber", defaultValue = "POL-88201") String policyNumber,
            @RequestParam(name = "v1", defaultValue = "1") int v1,
            @RequestParam(name = "v2", defaultValue = "2") int v2) {
        return OOSEngine.compareVersions(policyNumber, v1, v2);
    }

    @PostMapping("/oos-endorse")
    public PolicyVersion createOOSEndorsement(@RequestBody Map<String, String> payload) {
        String policyNumber = payload.getOrDefault("policyNumber", "POL-88201");
        String effDate = payload.get("effectiveDate");
        String description = payload.getOrDefault("description", "Retroactive Exposure Adjustment");
        return OOSEngine.executeOOSEndorsement(policyNumber, effDate, description);
    }
}
