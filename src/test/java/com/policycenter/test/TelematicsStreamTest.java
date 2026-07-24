package com.policycenter.test;

import com.policycenter.controller.TelematicsController;
import com.policycenter.model.TelematicsReading;
import com.policycenter.service.TelematicsStreamService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TelematicsStreamTest {

    @Test
    @DisplayName("Feature 04: Test IoT Telematics Live Stream & Safety Scoring Engine")
    public void testTelematicsEngine() {
        TelematicsStreamService service = TelematicsStreamService.getInstance();
        assertNotNull(service);

        // Fetch recent readings
        List<TelematicsReading> readings = service.getAllRecentReadings();
        assertFalse(readings.isEmpty());

        // Ingest high-risk ping (hard braking & speeding)
        TelematicsReading highRisk = new TelematicsReading(
            "tel-test-1",
            "1FTFW1E84MKD90182",
            "Ford F-350 Heavy Hauler #1",
            "Marcus Vance",
            "2026-07-24T12:00:00",
            82.0, // speeding >75 mph (-15)
            3,    // hard brakes (-19.5)
            2,    // rapid accel (-8)
            40,   // night driving (-8)
            0.0, 0.0, ""
        );

        TelematicsReading processed = service.ingestReading(highRisk);
        assertNotNull(processed);

        // Expected score: 100 - 19.5 - 8 - 15 - 8 = 49.5
        assertEquals(49.5, processed.getSafetyScore(), 0.1);
        assertEquals("HIGH_RISK", processed.getRiskGrade());
        assertEquals(0.15, processed.getPremiumAdjustmentFactor());

        // Test Fleet Analytics summary calculation
        Map<String, Object> fleetSummary = service.getFleetAnalyticsSummary();
        assertNotNull(fleetSummary);
        assertTrue((Integer) fleetSummary.get("monitoredVehiclesCount") > 0);

        // Test REST Controller
        TelematicsController controller = new TelematicsController();
        Map<String, Object> restAnalytics = controller.getFleetAnalytics();
        assertNotNull(restAnalytics);
        assertNotNull(restAnalytics.get("averageFleetSafetyScore"));
    }
}
