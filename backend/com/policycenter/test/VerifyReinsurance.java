package com.policycenter.test;

import com.policycenter.gs.classes.reinsurance.ReinsuranceEngine;

import java.util.Map;

public class VerifyReinsurance {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println(" Guidewire PolicyCenter Reinsurance Engine Test");
        System.out.println("=================================================");

        double grossPremium = 10000.00;
        double buildingLimit = 2500000.00;

        Map<String, Object> result = ReinsuranceEngine.calculateCession(grossPremium, buildingLimit);
        System.out.println("Gross Direct Premium: $" + result.get("grossPremium"));
        System.out.println("Building Limit: $" + result.get("buildingLimit"));
        System.out.println("Total Ceded Percentage: " + result.get("totalCededPercentage") + "%");
        System.out.println("Ceded Reinsurance Premium: $" + result.get("cededPremium"));
        System.out.println("Net Retained Premium: $" + result.get("netRetainedPremium"));

        double ceded = Double.parseDouble(result.get("cededPremium").toString());
        double net = Double.parseDouble(result.get("netRetainedPremium").toString());

        if (Math.abs(ceded - 6500.0) < 0.1 && Math.abs(net - 3500.0) < 0.1) {
            System.out.println("\nSUCCESS: Reinsurance Engine cessions verified cleanly (65% ceded / 35% net)!");
        } else {
            System.err.println("\nFAILURE: Reinsurance calculation mismatch!");
            System.exit(1);
        }
    }
}
