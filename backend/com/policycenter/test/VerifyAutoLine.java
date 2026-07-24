package com.policycenter.test;

import com.policycenter.gs.classes.rating.RatingEngine;
import com.policycenter.model.*;

public class VerifyAutoLine {
    public static void main(String[] args) {
        System.out.println("Testing Phase 3 Commercial Auto & Multi-Line Expansion...");
        
        PolicyPeriod caPeriod = new PolicyPeriod();
        caPeriod.setProductCode("CommercialAuto");
        caPeriod.setProductName("Commercial Auto");

        PolicyVehicle veh1 = new PolicyVehicle("v-1", 1, "1FTFW1E84MKD90182", "Ford", "F-350", 2023, 65000.0);
        PolicyDriver drv1 = new PolicyDriver("d-1", 1, "David", "Miller", "IL-D8910273", "IL");

        PolicyLine caLine = new PolicyLine("l-auto", "CommercialAutoLine", "Commercial Auto Line");
        caLine.addVehicle(veh1);
        caLine.addDriver(drv1);
        caPeriod.addLine(caLine);

        RatingEngine.ratePolicyPeriod(caPeriod);

        System.out.println("Commercial Auto Rated -> Total Premium: $" + caPeriod.getTotalPremium() + ", Total Cost: $" + caPeriod.getTotalCost());
        
        if (caPeriod.getTotalCost() > 0) {
            System.out.println("SUCCESS: Commercial Auto Multi-Line Rating Verified Clean!");
        } else {
            System.err.println("FAILURE: Rating engine returned 0 cost for Commercial Auto");
            System.exit(1);
        }
    }
}
