package com.policycenter.service;

import com.policycenter.model.Building;
import com.policycenter.model.PolicyLine;
import com.policycenter.model.PolicyPeriod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ============================================================================
 * MODULE 01: AI UNDERWRITING ASSISTANT & RISK ANALYTICS SERVICE
 * ============================================================================
 * 
 * DESCRIPTION:
 * This service implements the core AI Risk Assessment & Fraud Detection engine.
 * It analyzes PolicyPeriod entity graph details (Total Insured Value building limits,
 * structural age prior to 1980, automatic fire sprinkler presence, and line hazards)
 * to compute a normalized 0-100 AI Risk Index Score and Fraud Probability %.
 * 
 * KEY FUNCTIONS:
 * - assessPolicyPeriodRisk(period): Evaluates building limits, year built, and sprinklers.
 * - parseACORDDocument(rawText): Simulates AI OCR text parsing for ACORD 125/140 forms.
 * 
 * @author Guidewire PolicyCenter Team
 */
public class AIRiskAssessmentService {

    /**
     * Evaluates underwriting risk factors for a given PolicyPeriod entity.
     * 
     * @param period PolicyPeriod containing lines and building coverages
     * @return Map containing riskIndexScore, fraudProbabilityPct, decisionRecommendation, and risk factors
     */
    public static Map<String, Object> assessPolicyPeriodRisk(PolicyPeriod period) {
        Map<String, Object> result = new HashMap<>();

        double riskScore = 25.0; // Baseline initial risk score
        List<String> riskFactors = new ArrayList<>();
        List<String> aiRecommendations = new ArrayList<>();

        if (period != null) {
            String prodCode = period.getProductCode() != null ? period.getProductCode() : "CommercialProperty";

            if ("CommercialProperty".equalsIgnoreCase(prodCode)) {
                for (PolicyLine line : period.getLines()) {
                    for (Building b : line.getBuildings()) {
                        // Risk Weight A: High Building TIV Limit
                        if (b.getBuildingLimit() > 2000000.0) {
                            riskScore += 25.0;
                            riskFactors.add("High TIV Exposure: Building #" + b.getBuildingNum() + " Limit ($" + String.format("%.0f", b.getBuildingLimit()) + ") exceeds $2.0M AI threshold.");
                        } else if (b.getBuildingLimit() > 1000000.0) {
                            riskScore += 15.0;
                            riskFactors.add("Moderate TIV Exposure: Building #" + b.getBuildingNum() + " Limit exceeds $1.0M.");
                        }

                        // Risk Weight B: Structural Age (< 1980)
                        if (b.getYearBuilt() > 0 && b.getYearBuilt() < 1980) {
                            riskScore += 18.0;
                            riskFactors.add("Aging Structure: Building #" + b.getBuildingNum() + " constructed in " + b.getYearBuilt() + ".");
                        }

                        // Risk Weight C: Unsprinklered Premises
                        if (!b.isSprinklered()) {
                            riskScore += 12.0;
                            riskFactors.add("Unsprinklered Facility: Building #" + b.getBuildingNum() + " lacks active fire suppression.");
                        }
                    }
                }
            } else if ("CommercialAuto".equalsIgnoreCase(prodCode)) {
                riskScore += 20.0;
                riskFactors.add("Fleet Liability Risk: Commercial vehicle fleet operations evaluate higher bodily injury exposure.");
            } else if ("WorkersComp".equalsIgnoreCase(prodCode)) {
                riskScore += 15.0;
                riskFactors.add("Occupational Hazard Exposure: Class code 4771 manufacturing hazard exposure.");
            }
        }

        // Clamp risk score between 10.0 and 99.0
        riskScore = Math.min(Math.max(riskScore, 10.0), 99.0);
        double fraudProbability = Math.min(riskScore * 0.12, 28.5);

        String decision;
        String decisionBadgeClass;
        if (riskScore >= 70.0) {
            decision = "REFERRAL REQUIRED (Senior Underwriter Approval Needed)";
            decisionBadgeClass = "gw-badge-draft";
            aiRecommendations.add("Require engineering inspection report for fire & wiring safety.");
            aiRecommendations.add("Apply 15% facultative reinsurance cession or increase deductible.");
        } else if (riskScore >= 45.0) {
            decision = "STANDARD UW REVIEW";
            decisionBadgeClass = "gw-badge-quoted";
            aiRecommendations.add("Verify prior 3-year loss runs before binding.");
            aiRecommendations.add("Apply standard protective safeguard endorsement.");
        } else {
            decision = "AUTO-APPROVE ELIGIBLE (Straight-Through Processing)";
            decisionBadgeClass = "gw-badge-bound";
            aiRecommendations.add("Risk qualifies for instant binding and automated policy issuance.");
        }

        result.put("riskIndexScore", Math.round(riskScore * 10.0) / 10.0);
        result.put("fraudProbabilityPct", Math.round(fraudProbability * 10.0) / 10.0);
        result.put("decisionRecommendation", decision);
        result.put("decisionBadgeClass", decisionBadgeClass);
        result.put("identifiedRiskFactors", riskFactors);
        result.put("aiRecommendations", aiRecommendations);

        return result;
    }

    /**
     * Simulates ACORD document OCR text extraction.
     */
    public static Map<String, Object> parseACORDDocument(String rawText) {
        Map<String, Object> extracted = new HashMap<>();
        extracted.put("companyName", "Titan Global Operations Inc");
        extracted.put("fein", "88-9911223");
        extracted.put("industryCode", "Freight & Warehousing");
        extracted.put("addressLine1", "1200 Logistics Blvd");
        extracted.put("city", "Chicago");
        extracted.put("state", "IL");
        extracted.put("postalCode", "60611");
        extracted.put("buildingLimit", 1500000.0);
        extracted.put("contentsLimit", 350000.0);
        extracted.put("constructionType", "Joisted Masonry");
        extracted.put("yearBuilt", 1995);
        extracted.put("sprinklered", true);
        extracted.put("priorLossCount", 1);
        extracted.put("priorLossAmount", 12500.0);
        return extracted;
    }
}
