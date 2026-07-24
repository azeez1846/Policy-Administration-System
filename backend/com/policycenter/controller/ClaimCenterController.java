package com.policycenter.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/claims")
@CrossOrigin(origins = "*")
public class ClaimCenterController {

    @GetMapping("/loss-history")
    public List<Map<String, Object>> getClaimHistory(@RequestParam(defaultValue = "C00010928") String accountNumber) {
        return List.of(
            Map.of("claimNumber", "CLM-90021", "policyNumber", "CP-3451127", "lossDate", "2025-11-14", "lossCause", "Water Pipe Burst & Property Damage", "status", "Closed", "paidAmount", 12500.0, "reserveAmount", 0.0),
            Map.of("claimNumber", "CLM-94810", "policyNumber", "POL-3764124", "lossDate", "2026-03-02", "lossCause", "Commercial Auto Fender Collision", "status", "Open Reserve", "paidAmount", 3200.0, "reserveAmount", 4500.0)
        );
    }
}
