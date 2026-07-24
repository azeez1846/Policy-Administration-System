package com.policycenter.test;

import com.policycenter.gs.classes.rating.RatingStudioEngine;
import com.policycenter.model.RateTableFactor;

import java.util.List;

public class VerifyRatingStudio {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println(" Guidewire PolicyCenter Rating Studio Test");
        System.out.println("=================================================");

        List<RateTableFactor> factors = RatingStudioEngine.getAllFactors();
        System.out.println("Fetched " + factors.size() + " rate table factors:");
        for (RateTableFactor f : factors) {
            System.out.println(" - [" + f.getLineCode() + "] Table: " + f.getTableCode() + " | Key: " + f.getParamKey() + " | Factor: " + f.getFactorValue() + "x");
        }

        System.out.println("\nTesting Live Factor Override for rf-101 (Territory 10)...");
        RateTableFactor updated = RatingStudioEngine.updateFactor("rf-101", 1.35);
        System.out.println("Updated Factor: " + updated.getParamKey() + " -> " + updated.getFactorValue() + "x");

        double currentFactor = RatingStudioEngine.getFactorMultiplier("TerritoryCode", "Territory 10", 1.0);
        if (Math.abs(currentFactor - 1.35) < 0.001) {
            System.out.println("\nSUCCESS: RatingStudioEngine factor override verified cleanly (1.35x)!");
        } else {
            System.err.println("\nFAILURE: Factor value mismatch! Expected 1.35, got " + currentFactor);
            System.exit(1);
        }
    }
}
