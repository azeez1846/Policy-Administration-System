package com.policycenter.test;

import com.policycenter.gs.classes.job.OOSEngine;
import com.policycenter.model.PolicyVersion;

import java.util.List;

public class VerifyOOS {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println(" Guidewire PolicyCenter Out-of-Sequence Engine Test");
        System.out.println("=================================================");

        List<PolicyVersion> history = OOSEngine.getHistoryForPolicy("POL-88201");
        System.out.println("Fetched " + history.size() + " term slice versions for POL-88201:");
        for (PolicyVersion v : history) {
            System.out.println(" - Seq #" + v.getSequenceNumber() + " | Effective: " + v.getEffectiveDate() + " | Type: " + v.getJobType() + " | OOS: " + v.isOOS() + " | " + v.getDescription());
        }

        System.out.println("\nExecuting Out-of-Sequence Endorsement for retroactive date 2026-03-15...");
        PolicyVersion oosVer = OOSEngine.executeOOSEndorsement("POL-88201", "2026-03-15", "Retroactive Equipment Floater Schedule");
        System.out.println("Generated Version: Seq #" + oosVer.getSequenceNumber() + " | OOS: " + oosVer.isOOS() + " | " + oosVer.getDescription());

        if (oosVer.isOOS() && oosVer.getSequenceNumber() > 3) {
            System.out.println("\nSUCCESS: OOSEngine retroactive term slice version created cleanly!");
        } else {
            System.err.println("\nFAILURE: OOS version failed to create!");
            System.exit(1);
        }
    }
}
