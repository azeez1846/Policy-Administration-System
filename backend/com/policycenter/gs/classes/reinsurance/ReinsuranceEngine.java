package com.policycenter.gs.classes.reinsurance;

import com.policycenter.model.ReinsuranceTreaty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Emulates Guidewire PolicyCenter Reinsurance Management Engine (ReinsuranceLV.gs).
 * Calculates Quota Share (QS) and Excess of Loss (XOL) cessions, ceded premium, and net retention.
 */
public class ReinsuranceEngine {

    private static final List<ReinsuranceTreaty> treaties = new ArrayList<>();

    static {
        treaties.add(new ReinsuranceTreaty("ri-101", "Property Quota Share Treaty 2026", "QuotaShare", 40.0, 0.0, "Swiss Re Specialty"));
        treaties.add(new ReinsuranceTreaty("ri-102", "Excess of Loss High Exposure Treaty", "ExcessOfLoss", 25.0, 1000000.0, "Munich Re America"));
    }

    public static List<ReinsuranceTreaty> getAllTreaties() {
        return new ArrayList<>(treaties);
    }

    public static Map<String, Object> calculateCession(double grossPremium, double totalBuildingLimit) {
        double totalCededPercentage = 0.0;
        for (ReinsuranceTreaty t : treaties) {
            if ("QuotaShare".equalsIgnoreCase(t.getTreatyType())) {
                totalCededPercentage += t.getCededPercentage();
            } else if ("ExcessOfLoss".equalsIgnoreCase(t.getTreatyType()) && totalBuildingLimit >= t.getAttachmentPoint()) {
                totalCededPercentage += t.getCededPercentage();
            }
        }

        if (totalCededPercentage > 80.0) totalCededPercentage = 80.0; // Max cession cap

        double cededPremium = grossPremium * (totalCededPercentage / 100.0);
        double netRetainedPremium = grossPremium - cededPremium;

        Map<String, Object> result = new HashMap<>();
        result.put("grossPremium", grossPremium);
        result.put("buildingLimit", totalBuildingLimit);
        result.put("totalCededPercentage", totalCededPercentage);
        result.put("cededPremium", Math.round(cededPremium * 100.0) / 100.0);
        result.put("netRetainedPremium", Math.round(netRetainedPremium * 100.0) / 100.0);
        result.put("treaties", treaties);
        return result;
    }
}
