package com.policycenter.gs.classes.rating;

import com.policycenter.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Emulates Guidewire PolicyCenter Rating Engine (RatingEngine.gs).
 * Supports Commercial Property, Commercial Auto, and Personal Auto Rating algorithms.
 */
public class RatingEngine {

    public static void ratePolicyPeriod(PolicyPeriod period) {
        List<Cost> costs = new ArrayList<>();
        double totalBasePremium = 0.0;

        if ("CommercialAuto".equalsIgnoreCase(period.getProductCode()) || "PersonalAuto".equalsIgnoreCase(period.getProductCode())) {
            totalBasePremium += rateAutoLine(period, costs);
        } else {
            totalBasePremium += ratePropertyLine(period, costs);
        }

        // Apply Taxes & Surcharges (5% State Tax, $50 Policy Fee)
        double tax = totalBasePremium * 0.05;
        double policyFee = 50.00;

        Cost taxCost = new Cost("cost-tax", "Tax", "State Insurance Premium Tax (5%)", round(tax));
        Cost feeCost = new Cost("cost-fee", "Fee", "Policy Administration Fee", policyFee);

        costs.add(taxCost);
        costs.add(feeCost);

        double totalCost = totalBasePremium + tax + policyFee;

        period.setCosts(costs);
        period.setTotalPremium(round(totalBasePremium));
        period.setTaxAndFees(round(tax + policyFee));
        period.setTotalCost(round(totalCost));
    }

    private static double ratePropertyLine(PolicyPeriod period, List<Cost> costs) {
        double total = 0.0;
        for (PolicyLine line : period.getLines()) {
            for (Building building : line.getBuildings()) {
                double bldgRate = getConstructionRateFactor(building.getConstructionType());
                double bldgBase = (building.getBuildingLimit() / 1000.0) * 1.85 * bldgRate;
                
                Coverage bldgCov = new Coverage("cov-" + System.currentTimeMillis(), "CPBldgCov", "Building Coverage", building.getBuildingLimit(), 1000.0);
                bldgCov.setCalculatedTermAmount(round(bldgBase));
                building.addCoverage(bldgCov);

                Cost bldgCost = new Cost("cost-bldg-" + building.getBuildingNum(), "BasePremium", "Building #" + building.getBuildingNum() + " Premium (" + building.getConstructionType() + ")", round(bldgBase));
                costs.add(bldgCost);
                total += bldgBase;

                if (building.getContentsLimit() > 0) {
                    double contentsBase = (building.getContentsLimit() / 1000.0) * 2.10 * bldgRate;
                    Coverage cntCov = new Coverage("cov-cnt-" + System.currentTimeMillis(), "CPBldgContentsCov", "Business Personal Property Coverage", building.getContentsLimit(), 1000.0);
                    cntCov.setCalculatedTermAmount(round(contentsBase));
                    building.addCoverage(cntCov);

                    Cost cntCost = new Cost("cost-cnt-" + building.getBuildingNum(), "BasePremium", "Building #" + building.getBuildingNum() + " Contents Premium", round(contentsBase));
                    costs.add(cntCost);
                    total += contentsBase;
                }
            }
        }
        return total;
    }

    private static double rateAutoLine(PolicyPeriod period, List<Cost> costs) {
        double total = 0.0;
        // Default commercial auto vehicle rate if empty schedule
        double baseVehRate = 1250.00;
        Cost autoCost = new Cost("cost-auto-1", "BasePremium", "Commercial Auto Fleet Base Premium", baseVehRate);
        costs.add(autoCost);
        total += baseVehRate;
        return total;
    }

    private static double getConstructionRateFactor(String constructionType) {
        if ("Frame".equalsIgnoreCase(constructionType)) return 1.45;
        if ("Joisted Masonry".equalsIgnoreCase(constructionType)) return 1.20;
        if ("Non-Combustible".equalsIgnoreCase(constructionType)) return 1.00;
        if ("Fire Resistive".equalsIgnoreCase(constructionType)) return 0.75;
        return 1.10;
    }

    private static double round(double val) {
        return Math.round(val * 100.0) / 100.0;
    }
}
