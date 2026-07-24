package com.policycenter.gs.classes.job;

import com.policycenter.model.PolicyVersion;

import java.util.ArrayList;
import java.util.List;

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
}
