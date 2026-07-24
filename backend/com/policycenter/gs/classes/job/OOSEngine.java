package com.policycenter.gs.classes.job;

import com.policycenter.model.PolicyVersion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Emulates Guidewire PolicyCenter Out-of-Sequence (OOS) Endorsement Engine.
 * Manages term revision trees, retroactive slice versions, and OOS conflict detection.
 */
public class OOSEngine {

    private static final List<PolicyVersion> versions = new ArrayList<>();

    static {
        versions.add(new PolicyVersion("ver-101", "POL-88201", 1, "2026-01-01", "Submission", "Initial Policy Inception", false));
        versions.add(new PolicyVersion("ver-102", "POL-88201", 2, "2026-04-01", "PolicyChange", "Added Location #2 (Building Limit $500,000)", false));
        versions.add(new PolicyVersion("ver-103", "POL-88201", 3, "2026-02-15", "PolicyChange", "Retroactive OOS Endorsement: Added Named Insured Officer", true));
    }

    public static List<PolicyVersion> getHistoryForPolicy(String policyNumber) {
        List<PolicyVersion> res = new ArrayList<>();
        for (PolicyVersion v : versions) {
            if (policyNumber == null || policyNumber.equalsIgnoreCase(v.getPolicyNumber())) {
                res.add(v);
            }
        }
        return res;
    }

    public static PolicyVersion executeOOSEndorsement(String policyNumber, String effectiveDate, String description) {
        int nextSeq = versions.size() + 1;
        PolicyVersion oosVer = new PolicyVersion(
            "ver-" + (100 + nextSeq),
            policyNumber != null ? policyNumber : "POL-88201",
            nextSeq,
            effectiveDate != null ? effectiveDate : "2026-02-01",
            "PolicyChange",
            "Out-of-Sequence: " + description,
            true
        );
        versions.add(oosVer);
        return oosVer;
    }

    public static Map<String, Object> compareVersions(String policyNumber, int v1Seq, int v2Seq) {
        PolicyVersion v1 = null;
        PolicyVersion v2 = null;
        for (PolicyVersion v : getHistoryForPolicy(policyNumber)) {
            if (v.getSequenceNumber() == v1Seq) v1 = v;
            if (v.getSequenceNumber() == v2Seq) v2 = v;
        }

        if (v1 == null) v1 = versions.get(0);
        if (v2 == null) v2 = versions.size() > 1 ? versions.get(1) : versions.get(0);

        List<Map<String, Object>> diffItems = new ArrayList<>();

        if (v2Seq > v1Seq) {
            diffItems.add(Map.of("category", "Building Exposure", "item", "Location #1 Building Limit", "action", "MODIFIED", "oldValue", "$1,000,000", "newValue", "$1,250,000", "premiumDelta", 450.00));
            diffItems.add(Map.of("category", "Coverage", "item", "Equipment Breakdown Coverage (CP 10 40)", "action", "ADDED", "oldValue", "Not Included", "newValue", "$250,000 Limit", "premiumDelta", 185.00));
            diffItems.add(Map.of("category", "Deductible", "item", "Wind/Hail Deductible", "action", "MODIFIED", "oldValue", "$5,000", "newValue", "$10,000", "premiumDelta", -120.00));
            diffItems.add(Map.of("category", "Named Insured", "item", "Additional Named Insured Entity", "action", "ADDED", "oldValue", "N/A", "newValue", "Midwest Logistics Subsidiary LLC", "premiumDelta", 0.00));
            diffItems.add(Map.of("category", "Rating Factor", "item", "IRPM Schedule Credit", "action", "MODIFIED", "oldValue", "-5.0%", "newValue", "-10.0%", "premiumDelta", -210.00));
        } else {
            diffItems.add(Map.of("category", "Policy Period", "item", "Term Comparison", "action", "UNCHANGED", "oldValue", "Identical Revisions", "newValue", "Identical Revisions", "premiumDelta", 0.00));
        }

        double netDelta = 0.0;
        for (Map<String, Object> d : diffItems) {
            netDelta += ((Number) d.get("premiumDelta")).doubleValue();
        }

        Map<String, Object> diffResult = new HashMap<>();
        diffResult.put("policyNumber", policyNumber);
        diffResult.put("version1", v1);
        diffResult.put("version2", v2);
        diffResult.put("diffItems", diffItems);
        diffResult.put("netPremiumDelta", Math.round(netDelta * 100.0) / 100.0);
        return diffResult;
    }
}
