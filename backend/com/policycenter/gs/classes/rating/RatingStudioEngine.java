package com.policycenter.gs.classes.rating;

import com.policycenter.model.RateTableFactor;

import java.util.ArrayList;
import java.util.List;

/**
 * Emulates Guidewire Product Model Rating Matrix Studio (RatingMatrixLV.gs).
 * Manages live rate table factors, multipliers, and actuarial rating overrides.
 */
public class RatingStudioEngine {

    private static final List<RateTableFactor> factors = new ArrayList<>();

    static {
        factors.add(new RateTableFactor("rf-101", "CommercialProperty", "TerritoryCode", "Territory 10", "Urban Metro", 1.25));
        factors.add(new RateTableFactor("rf-102", "CommercialProperty", "TerritoryCode", "Territory 20", "Suburban Area", 1.10));
        factors.add(new RateTableFactor("rf-103", "CommercialProperty", "ConstructionType", "Joisted Masonry", "JM-Class2", 1.15));
        factors.add(new RateTableFactor("rf-104", "CommercialProperty", "ConstructionType", "Frame", "FR-Class1", 1.45));
        factors.add(new RateTableFactor("rf-105", "CommercialAuto", "DriverAgeTier", "Age 16-21", "Tier 1", 1.85));
        factors.add(new RateTableFactor("rf-106", "CommercialAuto", "DriverAgeTier", "Age 25-65", "Tier 3", 1.00));
    }

    public static List<RateTableFactor> getAllFactors() {
        return new ArrayList<>(factors);
    }

    public static RateTableFactor updateFactor(String factorID, double newFactorValue) {
        for (RateTableFactor f : factors) {
            if (f.getFactorID().equalsIgnoreCase(factorID)) {
                f.setFactorValue(newFactorValue);
                return f;
            }
        }
        throw new IllegalArgumentException("Factor not found: " + factorID);
    }

    public static double getFactorMultiplier(String tableCode, String paramKey, double defaultVal) {
        for (RateTableFactor f : factors) {
            if (f.getTableCode().equalsIgnoreCase(tableCode) && f.getParamKey().equalsIgnoreCase(paramKey)) {
                return f.getFactorValue();
            }
        }
        return defaultVal;
    }
}
