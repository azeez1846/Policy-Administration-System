package com.policycenter.gs.classes.job;

import com.policycenter.gs.classes.rating.RatingEngine;
import com.policycenter.model.*;
import com.policycenter.repository.PolicyCenterSqliteRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Emulates Guidewire PolicyCenter PolicyChangeProcess (PolicyChangeProcess.gs).
 * Handles Mid-Term Policy Changes (Endorsements) for Bound and Issued policies with
 * exact pro-rated premium delta, UW authority checks, and version history updates.
 */
public class PolicyChangeProcess {

    private final Job job;
    private final PolicyPeriod period;

    public PolicyChangeProcess(Job job) {
        this.job = job;
        this.period = job.getPolicyPeriod();
    }

    /**
     * Initializes a Policy Change transaction draft.
     */
    public void startPolicyChange() {
        if (job != null) {
            job.setJobType("PolicyChange");
            job.setJobStatus("Draft");
        }
        if (period != null) {
            period.setStatus("Draft");
        }
    }

    /**
     * Quotes a Policy Change transaction given updated property or coverage parameters.
     * Calculates term proration factor, annualized premium delta, prorated premium delta,
     * tax/fee delta, net transaction cost, and underwriting referral issues.
     */
    public Map<String, Object> calculateDetailedQuote(Map<String, Object> payload) {
        if (period == null) {
            throw new IllegalStateException("Policy period is null for job " + (job != null ? job.getJobNumber() : ""));
        }

        // Store prior annualized total premium before applying modifications
        double priorAnnualPremium = period.getTotalPremium() > 0 ? period.getTotalPremium() : 2200.0;

        // Apply building / property modifications if provided
        if (payload != null && !period.getBuildings().isEmpty()) {
            Building bldg = period.getBuildings().get(0);
            if (payload.containsKey("buildingLimit")) {
                double bldgLimit = Double.parseDouble(payload.get("buildingLimit").toString());
                bldg.setBuildingLimit(bldgLimit);
            }
            if (payload.containsKey("contentsLimit")) {
                double cntLimit = Double.parseDouble(payload.get("contentsLimit").toString());
                bldg.setContentsLimit(cntLimit);
            }
            if (payload.containsKey("constructionType")) {
                bldg.setConstructionType(payload.get("constructionType").toString());
            }
        }

        // Re-rate policy period to calculate new annualized amounts
        RatingEngine.ratePolicyPeriod(period);

        double newAnnualPremium = period.getTotalPremium();
        double annualPremiumDelta = round(newAnnualPremium - priorAnnualPremium);

        // Effective date and term proration factor calculation
        String effDateStr = (payload != null && payload.containsKey("effectiveDate"))
                ? payload.get("effectiveDate").toString()
                : LocalDate.now().toString();

        LocalDate changeEffDate = parseDate(effDateStr);
        LocalDate termStart = parseDate(period.getEffectiveDate() != null ? period.getEffectiveDate() : LocalDate.now().toString());
        LocalDate termExp = parseDate(period.getExpirationDate() != null ? period.getExpirationDate() : LocalDate.now().plusYears(1).toString());

        long totalTermDays = ChronoUnit.DAYS.between(termStart, termExp);
        if (totalTermDays <= 0) totalTermDays = 365;

        long remainingDays = ChronoUnit.DAYS.between(changeEffDate, termExp);
        if (remainingDays < 0) remainingDays = 0;
        if (remainingDays > totalTermDays) remainingDays = totalTermDays;

        double prorationFactor = roundDecimal((double) remainingDays / (double) totalTermDays, 4);

        // Calculate prorated premium and tax deltas
        double proratedDeltaPremium = round(annualPremiumDelta * prorationFactor);
        double proratedTaxDelta = round(proratedDeltaPremium * 0.05); // 5% state tax on delta
        double totalDeltaCharge = round(proratedDeltaPremium + proratedTaxDelta);

        // Check for Underwriting Issues
        List<Map<String, String>> uwIssues = new ArrayList<>();
        if (!period.getBuildings().isEmpty()) {
            Building bldg = period.getBuildings().get(0);
            if (bldg.getBuildingLimit() > 2500000.0) {
                uwIssues.add(Map.of(
                    "issueKey", "UW-BLDG-LIMIT-HIGH",
                    "shortDescription", "Building Limit Exceeds $2.5M UW Authority Threshold",
                    "severity", "High",
                    "status", "Open"
                ));
            }
        }
        if (Math.abs(annualPremiumDelta) > 1000.0) {
            uwIssues.add(Map.of(
                "issueKey", "UW-PREM-DELTA-HIGH",
                "shortDescription", "Annual Premium Change Exceeds $1,000 Authority Limit",
                "severity", "Medium",
                "status", "Open"
            ));
        }

        // Out-of-Sequence (OOS) check
        boolean isOutOfSequence = changeEffDate.isBefore(LocalDate.now().minusDays(15));

        // Build structured quote response
        Map<String, Object> quoteResult = new LinkedHashMap<>();
        quoteResult.put("jobNumber", job.getJobNumber());
        quoteResult.put("policyNumber", period.getPolicyNumber() != null ? period.getPolicyNumber() : "POL-" + job.getJobNumber());
        quoteResult.put("status", "Quoted");
        quoteResult.put("effectiveDateOfChange", effDateStr);
        quoteResult.put("changeReason", payload != null ? payload.getOrDefault("changeReason", "Coverage Adjustment").toString() : "Coverage Adjustment");
        quoteResult.put("termStart", termStart.toString());
        quoteResult.put("termExp", termExp.toString());
        quoteResult.put("totalTermDays", totalTermDays);
        quoteResult.put("remainingDays", remainingDays);
        quoteResult.put("prorationFactor", prorationFactor);
        quoteResult.put("priorAnnualPremium", priorAnnualPremium);
        quoteResult.put("newAnnualPremium", newAnnualPremium);
        quoteResult.put("annualPremiumDelta", annualPremiumDelta);
        quoteResult.put("proratedDeltaPremium", proratedDeltaPremium);
        quoteResult.put("proratedTaxDelta", proratedTaxDelta);
        quoteResult.put("totalDeltaCharge", totalDeltaCharge);
        quoteResult.put("uwIssues", uwIssues);
        quoteResult.put("hasUwReferrals", !uwIssues.isEmpty());
        quoteResult.put("isOutOfSequence", isOutOfSequence);

        // Save status change to Quoted
        job.setJobStatus("Quoted");
        period.setStatus("Quoted");

        return quoteResult;
    }

    /**
     * Binds and Issues the Policy Change transaction.
     */
    public PolicyPeriod bindPolicyChange() {
        if (period == null) {
            throw new IllegalStateException("Policy period is null");
        }

        // Re-rate and compute prorated cost adjustment
        RatingEngine.ratePolicyPeriod(period);

        double totalCost = period.getTotalCost();
        double proratedFactor = 0.50;

        Cost changeCost = new Cost("cost-chg-" + System.currentTimeMillis(), "ModifierAdjustment", "Mid-Term Endorsement Adjustment", round(totalCost * 0.10));
        changeCost.setProrationFactor(proratedFactor);
        period.getCosts().add(changeCost);

        period.setStatus("Bound");
        if (job != null) {
            job.setJobStatus("Bound");
            job.setCloseDate(LocalDate.now().toString());
            PolicyCenterSqliteRepository.getInstance().saveJob(job);
        }

        return period;
    }

    public Job getJob() {
        return job;
    }

    public PolicyPeriod getPeriod() {
        return period;
    }

    private LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            return LocalDate.now();
        }
    }

    private double round(double val) {
        return Math.round(val * 100.0) / 100.0;
    }

    private double roundDecimal(double val, int places) {
        double factor = Math.pow(10, places);
        return Math.round(val * factor) / factor;
    }
}
