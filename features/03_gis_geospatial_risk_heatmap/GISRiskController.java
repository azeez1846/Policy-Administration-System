package com.policycenter.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gis")
@CrossOrigin(origins = "*")
public class GISRiskController {

    @GetMapping("/exposures")
    public List<Map<String, Object>> getGeospatialExposures() {
        return List.of(
            Map.of("id", "loc-101", "name", "Chicago HQ Warehouse", "lat", 41.8781, "lng", -87.6298, "tiv", 12500000.0, "riskCategory", "High TIV", "state", "IL"),
            Map.of("id", "loc-102", "name", "Miami Coastal Distribution Center", "lat", 25.7617, "lng", -80.1918, "tiv", 8500000.0, "riskCategory", "Hurricane Zone 4", "state", "FL"),
            Map.of("id", "loc-103", "name", "Houston Refinery Annex", "lat", 29.7604, "lng", -95.3698, "tiv", 14200000.0, "riskCategory", "Flood Zone A", "state", "TX"),
            Map.of("id", "loc-104", "name", "Los Angeles Fleet Operations", "lat", 34.0522, "lng", -118.2437, "tiv", 6200000.0, "riskCategory", "Earthquake Fault Line", "state", "CA"),
            Map.of("id", "loc-105", "name", "New York Corporate Office", "lat", 40.7128, "lng", -74.0060, "tiv", 22000000.0, "riskCategory", "Commercial Retail", "state", "NY")
        );
    }
}
