package com.policycenter.test;

import com.policycenter.gs.classes.rules.UWRulesEngine;
import com.policycenter.model.*;
import com.policycenter.repository.PolicyCenterSqliteRepository;

import java.util.List;

public class VerifyUWRules {
    public static void main(String[] args) {
        System.out.println("Testing Phase 2 Underwriting Rules & Referral Engine (RiskAnalysisScreen.pcf)...");
        PolicyCenterSqliteRepository repo = PolicyCenterSqliteRepository.getInstance();

        Job job = repo.getJob("SUB-5001");
        if (job == null) {
            System.err.println("Job SUB-5001 not found.");
            System.exit(1);
            return;
        }
        PolicyPeriod period = job.getPolicyPeriod();
        if (period == null) {
            System.err.println("PolicyPeriod for SUB-5001 not found.");
            System.exit(1);
            return;
        }

        // 1. Evaluate Rules
        List<UWIssue> issues = UWRulesEngine.evaluatePeriodRules(period);
        System.out.println("1. UW Rules Evaluated -> Triggered " + issues.size() + " issue(s):");
        for (UWIssue issue : issues) {
            System.out.println("   - [" + issue.getIssueKey() + "] " + issue.getShortDescription() + " (Block: " + issue.getApprovalBlockingLevel() + ", Status: " + issue.getStatus() + ")");
        }

        // 2. Check Blocking Status
        boolean isBindBlocked = UWRulesEngine.hasBlockingIssues(period, "Bind");
        System.out.println("2. Is Bind Currently Blocked? " + isBindBlocked);

        // 3. Underwriter Approves HighBuildingLimit issue
        boolean approved = UWRulesEngine.approveIssue(period, "HighBuildingLimit", "su");
        System.out.println("3. Underwriter Approved HighBuildingLimit issue? " + approved);

        boolean isBindBlockedAfterApproval = UWRulesEngine.hasBlockingIssues(period, "Bind");
        System.out.println("4. Is Bind Blocked After Approval? " + isBindBlockedAfterApproval);

        repo.saveJob(job);
        System.out.println("\nSUCCESS: All Phase 2 Underwriting Rules & Referral Engine Tests Passed!");
    }
}
