package com.policycenter.controller;

import com.policycenter.gs.classes.claims.ClaimEngine;
import com.policycenter.model.Claim;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/claims")
@CrossOrigin(origins = "*")
public class ClaimController {

    @GetMapping
    public Map<String, Object> getAccountClaims(@RequestParam(name = "accountNumber", defaultValue = "ACC-1001") String accountNumber,
                                                @RequestParam(name = "earnedPremium", defaultValue = "2400.00") double earnedPremium) {
        List<Claim> claims = ClaimEngine.getClaimsForAccount(accountNumber);
        double lossRatio = ClaimEngine.calculate3YearLossRatio(accountNumber, earnedPremium);

        Map<String, Object> response = new HashMap<>();
        response.put("accountNumber", accountNumber);
        response.put("lossRatioPercentage", lossRatio);
        response.put("claims", claims);
        return response;
    }

    @PostMapping
    public Claim reportPriorClaim(@RequestBody Claim claim) {
        if (claim.getPublicID() == null) {
            claim.setPublicID("clm-" + System.currentTimeMillis());
        }
        if (claim.getClaimNumber() == null) {
            claim.setClaimNumber("CLM-" + (800000 + (int)(Math.random() * 90000)));
        }
        ClaimEngine.addClaim(claim);
        return claim;
    }
}
