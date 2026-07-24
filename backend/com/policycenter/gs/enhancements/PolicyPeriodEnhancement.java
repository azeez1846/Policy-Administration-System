package com.policycenter.gs.enhancements;

import com.policycenter.model.Building;
import com.policycenter.model.PolicyLine;
import com.policycenter.model.PolicyPeriod;

/**
 * Emulates Guidewire Gosu Entity Enhancement on PolicyPeriod (PolicyPeriodEnhancement.gsx).
 */
public class PolicyPeriodEnhancement {

    public static boolean isQuoteable(PolicyPeriod period) {
        if (period == null) return false;
        if (period.getAccount() == null || period.getPrimaryNamedInsured() == null) return false;
        if (period.getLines() == null || period.getLines().isEmpty()) return false;
        
        for (PolicyLine line : period.getLines()) {
            if (line.getBuildings() == null || line.getBuildings().isEmpty()) return false;
            for (Building b : line.getBuildings()) {
                if (b.getBuildingLimit() <= 0) return false;
            }
        }
        return true;
    }

    public static boolean isBindable(PolicyPeriod period) {
        return "Quoted".equalsIgnoreCase(period.getStatus()) && period.getTotalCost() > 0;
    }

    public static String generatePolicyNumber(PolicyPeriod period) {
        String prefix = "POL-";
        if ("CommercialProperty".equalsIgnoreCase(period.getProductCode())) {
            prefix = "CP-";
        } else if ("PersonalAuto".equalsIgnoreCase(period.getProductCode())) {
            prefix = "PA-";
        }
        long randomNum = (long) (Math.random() * 9000000L) + 1000000L;
        return prefix + randomNum;
    }
}
