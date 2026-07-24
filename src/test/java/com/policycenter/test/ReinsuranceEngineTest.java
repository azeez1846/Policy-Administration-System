package com.policycenter.test;

import com.policycenter.controller.ReinsuranceController;
import com.policycenter.gs.classes.reinsurance.ReinsuranceEngine;
import com.policycenter.model.ReinsuranceTreaty;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ReinsuranceEngineTest {

    @Test
    @DisplayName("Feature 02: Test Automated Reinsurance Treaty & Layer Cession Engine")
    public void testReinsuranceCessionEngine() {
        // Test treaty listing
        List<ReinsuranceTreaty> treaties = ReinsuranceEngine.getAllTreaties();
        assertFalse(treaties.isEmpty());

        // Add custom Facultative Treaty
        ReinsuranceTreaty customTreaty = new ReinsuranceTreaty(
            "ri-test-1",
            "Catastrophe Excess Layer 2026",
            "Facultative",
            10.0,
            2000000.0,
            "Berkshire Hathaway Re"
        );
        ReinsuranceEngine.addTreaty(customTreaty);

        // Calculate cessions for $10,000 gross premium and $6,000,000 building limit
        Map<String, Object> cession = ReinsuranceEngine.calculateCession(10000.0, 6000000.0);
        assertNotNull(cession);

        double grossPrem = (Double) cession.get("grossPremium");
        double totalCededPrem = (Double) cession.get("cededPremium");
        double netPrem = (Double) cession.get("netRetainedPremium");
        double totalCededPct = (Double) cession.get("totalCededPercentage");

        assertEquals(10000.0, grossPrem);
        assertTrue(totalCededPct > 0.0);
        assertEquals(grossPrem - totalCededPrem, netPrem, 0.01);

        List<?> layerStack = (List<?>) cession.get("layerStack");
        assertNotNull(layerStack);
        assertFalse(layerStack.isEmpty());

        // Test REST Controller
        ReinsuranceController controller = new ReinsuranceController();
        Map<String, Object> restResult = controller.calculateCession(Map.<String, Object>of("grossPremium", "5000.00", "buildingLimit", "2500000.00"));
        assertNotNull(restResult);
        assertEquals(5000.0, ((Number) restResult.get("grossPremium")).doubleValue());
    }
}
