package com.policycenter.gs.classes.claims;

import com.policycenter.model.Claim;
import com.policycenter.model.PolicyPeriod;
import com.policycenter.model.UWIssue;

import java.util.ArrayList;
import java.util.List;

/**
 * Emulates Guidewire ClaimCenter Integration Engine (ClaimHistoryDV.gs).
 * Calculates 3-Year Loss Ratio % and triggers High Loss Ratio UW Referral issues.
 */
public class ClaimEngine {

    private static final List<Claim> seedClaims = new ArrayList<>();

    static {
        seedClaims.add(new Claim("clm-101", "CLM-890123", "ACC-1001", "2024-05-14", "Water Damage - Broken Pipe", 12500.0, 0.0));
        seedClaims.add(new Claim("clm-102", "CLM-890456", "ACC-1001", "2025-01-10", "Windstorm Roof Loss", 4200.0, 1500.0));
    }

    public static List<Claim> getClaimsForAccount(String accountNumber) {
        List<Claim> result = new ArrayList<>();
        for (Claim c : seedClaims) {
            if (accountNumber == null || accountNumber.equalsIgnoreCase(c.getAccountNumber())) {
                result.add(c);
            }
        }
        return result;
    }

    public static void addClaim(Claim claim) {
        seedClaims.add(claim);
    }

    public static double calculate3YearLossRatio(String accountNumber, double totalEarnedPremium) {
        List<Claim> claims = getClaimsForAccount(accountNumber);
        double totalIncurredLosses = 0.0;
        for (Claim c : claims) {
            totalIncurredLosses += c.getTotalPaid() + c.getReserveAmount();
        }

        double premium = totalEarnedPremium > 0 ? totalEarnedPremium : 2400.00;
        double ratio = (totalIncurredLosses / premium) * 100.0;
        return Math.round(ratio * 10.0) / 10.0;
    }

    public static void evaluateClaimRules(PolicyPeriod period) {
        if (period == null || period.getAccount() == null) return;

        double lossRatio = calculate3YearLossRatio(period.getAccount().getAccountNumber(), period.getTotalCost());
        if (lossRatio > 65.0) {
            UWIssue issue = new UWIssue(
                "uwi-loss-ratio-" + period.getPeriodID(),
                "HighLossRatioReferral",
                "3-Year Loss Ratio (" + lossRatio + "%) exceeds threshold of 65.0%",
                "Bind"
            );
            issue.setLongDescription("Account has high prior loss history ($18,200 incurred). Requires Senior UW authorization to bind.");
            period.addUwIssue(issue);
        }
    }
}
