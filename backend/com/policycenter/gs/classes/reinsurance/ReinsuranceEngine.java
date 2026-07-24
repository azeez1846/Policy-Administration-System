package com.policycenter.gs.classes.reinsurance;

import com.policycenter.model.RIAttachment;
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
        treaties.add(new ReinsuranceTreaty("ri-103", "Catastrophe Facultative Layer 2026", "Facultative", 15.0, 5000000.0, "Lloyd's Syndicate 2003"));
    }

    public static List<ReinsuranceTreaty> getAllTreaties() {
        return new ArrayList<>(treaties);
    }

    public static ReinsuranceTreaty addTreaty(ReinsuranceTreaty treaty) {
        if (treaty.getTreatyID() == null || treaty.getTreatyID().trim().isEmpty()) {
            treaty.setTreatyID("ri-" + (treaties.size() + 101));
        }
        treaties.add(treaty);
        return treaty;
    }

    public static Map<String, Object> calculateCession(double grossPremium, double totalBuildingLimit) {
        double totalCededPercentage = 0.0;
        List<RIAttachment> attachments = new ArrayList<>();
        List<Map<String, Object>> layerStack = new ArrayList<>();

        for (ReinsuranceTreaty t : treaties) {
            boolean attached = false;
            double treatyShare = 0.0;

            if ("QuotaShare".equalsIgnoreCase(t.getTreatyType())) {
                attached = true;
                treatyShare = t.getCededPercentage();
            } else if ("ExcessOfLoss".equalsIgnoreCase(t.getTreatyType()) && totalBuildingLimit >= t.getAttachmentPoint()) {
                attached = true;
                treatyShare = t.getCededPercentage();
            } else if ("Facultative".equalsIgnoreCase(t.getTreatyType()) && totalBuildingLimit >= t.getAttachmentPoint()) {
                attached = true;
                treatyShare = t.getCededPercentage();
            }

            if (attached) {
                totalCededPercentage += treatyShare;
                double layerCededPremium = grossPremium * (treatyShare / 100.0);
                attachments.add(new RIAttachment(
                    "ria-" + System.currentTimeMillis() + "-" + t.getTreatyID(),
                    "risk-loc-1",
                    t.getTreatyID(),
                    t.getTreatyType(),
                    treatyShare
                ));
                layerStack.add(Map.of(
                    "treatyID", t.getTreatyID(),
                    "treatyName", t.getTreatyName(),
                    "treatyType", t.getTreatyType(),
                    "reinsurerName", t.getReinsurerName(),
                    "cededPercentage", treatyShare,
                    "layerCededPremium", Math.round(layerCededPremium * 100.0) / 100.0
                ));
            }
        }

        if (totalCededPercentage > 85.0) totalCededPercentage = 85.0; // Max cession cap

        double cededPremium = grossPremium * (totalCededPercentage / 100.0);
        double netRetainedPremium = grossPremium - cededPremium;

        Map<String, Object> result = new HashMap<>();
        result.put("grossPremium", grossPremium);
        result.put("buildingLimit", totalBuildingLimit);
        result.put("totalCededPercentage", totalCededPercentage);
        result.put("cededPremium", Math.round(cededPremium * 100.0) / 100.0);
        result.put("netRetainedPremium", Math.round(netRetainedPremium * 100.0) / 100.0);
        result.put("netRetentionPercentage", Math.round((100.0 - totalCededPercentage) * 10.0) / 10.0);
        result.put("layerStack", layerStack);
        result.put("treaties", treaties);
        result.put("attachments", attachments);
        return result;
    }
}

