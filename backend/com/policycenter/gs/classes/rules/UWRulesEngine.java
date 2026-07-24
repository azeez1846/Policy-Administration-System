package com.policycenter.gs.classes.rules;

import com.policycenter.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Emulates Guidewire PolicyCenter Underwriting Rules Engine (UWRules.gs / RiskAnalysis.gs).
 * Evaluates underwriting issues and enforces blocking levels (Block Quote, Block Bind, Block Issue).
 */
public class UWRulesEngine {

    public static List<UWIssue> evaluatePeriodRules(PolicyPeriod period) {
        List<UWIssue> issues = new ArrayList<>();
        if (period == null || period.getLines() == null) return issues;

        for (PolicyLine line : period.getLines()) {
            if (line.getBuildings() == null) continue;
            for (Building b : line.getBuildings()) {
                // Rule 1: High Building Limit Referral (> $1,000,000)
                if (b.getBuildingLimit() > 1000000.0) {
                    UWIssue issue = new UWIssue(
                        "uwi-bldg-limit-" + b.getBuildingNum(),
                        "HighBuildingLimit",
                        "Building #" + b.getBuildingNum() + " limit ($" + String.format("%.0f", b.getBuildingLimit()) + ") exceeds $1.0M threshold",
                        "Bind"
                    );
                    issue.setLongDescription("Requires Senior Underwriter review and authorization for building coverage limits exceeding $1,000,000.");
                    issues.add(issue);
                }

                // Rule 2: Older High-Risk Construction Building (< 1980)
                if (b.getYearBuilt() > 0 && b.getYearBuilt() < 1980) {
                    UWIssue issue = new UWIssue(
                        "uwi-bldg-age-" + b.getBuildingNum(),
                        "HighRiskBuildingAge",
                        "Building #" + b.getBuildingNum() + " constructed in " + b.getYearBuilt() + " (Prior to 1980)",
                        "Quote"
                    );
                    issue.setLongDescription("Older structure requires verification of wiring, plumbing, and structural updates prior to quoting.");
                    issues.add(issue);
                }

                // Rule 3: Unsprinklered Warehouse / Commercial Premises
                if (!b.isSprinklered()) {
                    UWIssue issue = new UWIssue(
                        "uwi-bldg-nosprink-" + b.getBuildingNum(),
                        "UnsprinkleredFacility",
                        "Building #" + b.getBuildingNum() + " does not have an active automatic sprinkler system",
                        "Quote"
                    );
                    issue.setLongDescription("Unsprinklered commercial facility requires underwriting surcharge and approval.");
                    issues.add(issue);
                }
            }
        }

        // Merge with existing period issues
        for (UWIssue newIssue : issues) {
            boolean exists = false;
            for (UWIssue existing : period.getUwIssues()) {
                if (existing.getIssueKey().equalsIgnoreCase(newIssue.getIssueKey())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                period.addUwIssue(newIssue);
            }
        }

        // Evaluate ClaimCenter Loss History Rules
        com.policycenter.gs.classes.claims.ClaimEngine.evaluateClaimRules(period);

        // Evaluate HazardHub Environmental Risk Rules (Marketplace Accelerator)
        evaluateHazardRules(period, issues);

        return period.getUwIssues();
    }

    private static void evaluateHazardRules(PolicyPeriod period, List<UWIssue> issues) {
        if (period == null || period.getLocations() == null) return;
        com.policycenter.repository.PolicyCenterSqliteRepository repo = com.policycenter.repository.PolicyCenterSqliteRepository.getInstance();

        for (PolicyLocation loc : period.getLocations()) {
            HazardIntelligence hi = repo.getHazardIntelligenceByLocation(loc.getPublicID());
            if (hi == null && loc.getAddressLine1() != null) {
                // Check by address lookup
                hi = repo.getHazardIntelligenceByLocation(loc.getAddressLine1());
            }
            if (hi != null) {
                if (hi.getWildfireScore() > 75) {
                    UWIssue issue = new UWIssue(
                        "uwi-hazard-wildfire-" + loc.getPublicID(),
                        "ExtremeWildfireHazard",
                        "Location " + loc.getLocationName() + " wildfire hazard score (" + hi.getWildfireScore() + "/100) exceeds 75 threshold",
                        "Bind"
                    );
                    issue.setLongDescription("HazardHub Intelligence score indicates extreme wildfire vulnerability zone. Senior UW sign-off required.");
                    issues.add(issue);
                    period.addUwIssue(issue);
                }
                if (hi.getFloodZone() != null && (hi.getFloodZone().contains("VE") || hi.getFloodZone().contains("AE"))) {
                    UWIssue issue = new UWIssue(
                        "uwi-hazard-flood-" + loc.getPublicID(),
                        "HighRiskFloodZone",
                        "Location " + loc.getLocationName() + " is situated in High-Risk Special Flood Hazard Area (" + hi.getFloodZone() + ")",
                        "Bind"
                    );
                    issue.setLongDescription("High-risk FEMA flood zone classification requires mandatory flood deductible endorsement and UW review.");
                    issues.add(issue);
                    period.addUwIssue(issue);
                }
            }
        }
    }

    public static boolean hasBlockingIssues(PolicyPeriod period, String stage) {
        if (period == null || period.getUwIssues() == null) return false;
        for (UWIssue issue : period.getUwIssues()) {
            if ("Open".equalsIgnoreCase(issue.getStatus())) {
                if (stage.equalsIgnoreCase(issue.getApprovalBlockingLevel()) || "Quote".equalsIgnoreCase(issue.getApprovalBlockingLevel())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean approveIssue(PolicyPeriod period, String issueKey, String user) {
        if (period == null || period.getUwIssues() == null) return false;
        for (UWIssue issue : period.getUwIssues()) {
            if (issue.getIssueKey().equalsIgnoreCase(issueKey)) {
                issue.setStatus("Approved");
                return true;
            }
        }
        return false;
    }
}
