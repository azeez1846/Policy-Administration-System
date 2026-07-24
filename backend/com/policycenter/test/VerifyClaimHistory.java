package com.policycenter.test;

import com.policycenter.gs.classes.claims.ClaimEngine;
import com.policycenter.gs.classes.rules.UWRulesEngine;
import com.policycenter.model.*;

import java.util.List;

public class VerifyClaimHistory {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println(" Guidewire PolicyCenter ClaimCenter Integration Test");
        System.out.println("=================================================");

        Account acc = new Account("acc-1", "ACC-1001", new Contact("c1", "Acme Inc", "Company", "a@acme.com", "555-0199"), "1001");
        PolicyPeriod period = new PolicyPeriod("prd-test", acc, acc.getAccountHolder(), "2026-07-23", "2027-07-23");
        period.setTotalCost(2400.00);

        List<Claim> claims = ClaimEngine.getClaimsForAccount("ACC-1001");
        System.out.println("Fetched " + claims.size() + " prior claims for account ACC-1001:");
        for (Claim c : claims) {
            System.out.println(" - Claim " + c.getClaimNumber() + " | Date: " + c.getLossDate() + " | Cause: " + c.getCauseOfLoss() + " | Incurred: $" + (c.getTotalPaid() + c.getReserveAmount()));
        }

        double lossRatio = ClaimEngine.calculate3YearLossRatio("ACC-1001", period.getTotalCost());
        System.out.println("\n3-Year Loss Ratio %: " + lossRatio + "%");

        System.out.println("\nEvaluating Underwriting Rules...");
        List<UWIssue> issues = UWRulesEngine.evaluatePeriodRules(period);
        boolean foundLossRatioIssue = false;
        for (UWIssue issue : issues) {
            System.out.println(" [UW ISSUE] " + issue.getIssueKey() + " | Level: " + issue.getApprovalBlockingLevel() + " | " + issue.getShortDescription());
            if ("HighLossRatioReferral".equalsIgnoreCase(issue.getIssueKey())) {
                foundLossRatioIssue = true;
            }
        }

        if (foundLossRatioIssue) {
            System.out.println("\nSUCCESS: HighLossRatioReferral correctly triggered for loss ratio > 65%!");
        } else {
            System.err.println("\nFAILURE: HighLossRatioReferral issue missing!");
            System.exit(1);
        }
    }
}
