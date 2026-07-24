package com.policycenter.test;

import com.policycenter.controller.GISRiskController;
import com.policycenter.gs.classes.rules.UWRulesEngine;
import com.policycenter.model.*;
import com.policycenter.service.CatastropheMoratoriumService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CatastropheMoratoriumTest {

    @Test
    @DisplayName("Feature 01: Test Catastrophe Moratorium & Dynamic PolicyHold Rules Engine")
    public void testCatastropheMoratoriumEngine() {
        CatastropheMoratoriumService service = CatastropheMoratoriumService.getInstance();
        assertNotNull(service);

        // Fetch active default moratoriums
        List<CatastropheMoratorium> moratoriums = service.getActiveMoratoriums();
        assertFalse(moratoriums.isEmpty());

        // Declare a test moratorium over Chicago (41.8781, -87.6298) with 25 mile radius
        CatastropheMoratorium m = new CatastropheMoratorium(
            "test-moratorium-chi",
            "Chicago Great Lakes Blizzard Moratorium",
            "Blizzard",
            41.8781, -87.6298,
            25.0,
            "2026-01-01",
            "2026-01-10",
            "ACTIVE",
            "underwriter",
            true, true
        );
        service.addMoratorium(m);

        // Test spatial Haversine distance calculation
        double dist = CatastropheMoratoriumService.calculateDistanceMiles(41.8781, -87.6298, 41.8781, -87.6298);
        assertEquals(0.0, dist, 0.001);

        // Check policy period location in IL (Chicago) triggers Moratorium UWIssue
        Account acc = new Account("acc-m1", "C990011", new Contact("c1", "Windy City Logistics", "Company", "w@c.com", "555-0011"), "Trucking");
        PolicyPeriod period = new PolicyPeriod("prd-m1", acc, acc.getAccountHolder(), "2026-01-01", "2027-01-01");
        PolicyLocation loc = new PolicyLocation("loc-chi-1", 1, "Chicago Central Depot", "100 S State St", "Chicago", "IL", "60603");
        loc.setLatitude(41.8781);
        loc.setLongitude(-87.6298);
        period.addLocation(loc);

        Building bldg = new Building("b1", 1, "Main Warehouse", "Joisted Masonry", 800000.0, 100000.0);
        PolicyLine line = new PolicyLine("l1", "CommercialProperty", "Property Line");
        line.addBuilding(bldg);
        period.addLine(line);

        List<UWIssue> issues = UWRulesEngine.evaluatePeriodRules(period);
        assertFalse(issues.isEmpty());

        boolean moratoriumHoldFound = issues.stream().anyMatch(i -> i.getIssueKey().contains("test-moratorium-chi"));
        assertTrue(moratoriumHoldFound, "Catastrophe Moratorium Underwriting Issue should be created for location within blizzard radius!");

        // Test REST Controller Endpoints
        GISRiskController controller = new GISRiskController();
        ResponseEntity<Map<String, Object>> checkRes = controller.checkCoordinates(Map.<String, Object>of("lat", 41.8781, "lng", -87.6298));
        assertEquals(200, checkRes.getStatusCode().value());
        Map<String, Object> checkBody = checkRes.getBody();
        assertNotNull(checkBody);
        assertTrue(Boolean.TRUE.equals(checkBody.get("isBlocked")));

        // Lift moratorium
        ResponseEntity<Map<String, Object>> liftRes = controller.liftMoratorium("test-moratorium-chi");
        assertEquals(200, liftRes.getStatusCode().value());
        Map<String, Object> liftBody = liftRes.getBody();
        assertNotNull(liftBody);
        assertEquals("SUCCESS", liftBody.get("status"));
    }
}
