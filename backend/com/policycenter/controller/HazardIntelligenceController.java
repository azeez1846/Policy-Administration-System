package com.policycenter.controller;

import com.policycenter.model.HazardIntelligence;
import com.policycenter.repository.PolicyCenterSqliteRepository;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Guidewire Marketplace Accelerator Controller: HazardHub Property Risk & Environmental Geocoding
 */
@RestController
@RequestMapping("/api/marketplace/hazard")
@CrossOrigin(origins = "*")
public class HazardIntelligenceController {

    private final PolicyCenterSqliteRepository repo = PolicyCenterSqliteRepository.getInstance();

    @PostMapping("/enrich")
    public Map<String, Object> enrichLocation(@RequestBody Map<String, String> payload) {
        String locationId = payload.getOrDefault("locationId", "loc-" + UUID.randomUUID().toString().substring(0, 6));
        String buildingId = payload.getOrDefault("buildingId", "bldg-1");
        String addressLine = payload.getOrDefault("addressLine", "100 Industrial Parkway, Chicago, IL");
        String state = payload.getOrDefault("state", "IL").toUpperCase();

        // Deterministic simulation based on locationId/address to provide dynamic demonstration data
        int wildfireScore;
        String floodZone;
        double distanceToCoast;
        double roofScore;
        String hailIndex;
        String category;

        if (addressLine.toLowerCase().contains("california") || addressLine.toLowerCase().contains("canyon") || state.equals("CA")) {
            wildfireScore = 88;
            floodZone = "Zone X";
            distanceToCoast = 12.4;
            roofScore = 4.2;
            hailIndex = "Low";
            category = "Extreme Wildfire Exposure";
        } else if (addressLine.toLowerCase().contains("florida") || addressLine.toLowerCase().contains("coast") || state.equals("FL")) {
            wildfireScore = 25;
            floodZone = "Zone VE";
            distanceToCoast = 0.8;
            roofScore = 3.1;
            hailIndex = "Medium";
            category = "High Coastal Storm Surge";
        } else if (addressLine.toLowerCase().contains("texas") || state.equals("TX")) {
            wildfireScore = 45;
            floodZone = "Zone AE";
            distanceToCoast = 85.0;
            roofScore = 2.5;
            hailIndex = "Severe";
            category = "Severe Hail & Wind Exposure";
        } else {
            wildfireScore = 18;
            floodZone = "Zone X500";
            distanceToCoast = 340.0;
            roofScore = 4.5;
            hailIndex = "Low";
            category = "Standard Moderate Risk";
        }

        HazardIntelligence hi = new HazardIntelligence(
                "haz-" + UUID.randomUUID().toString().substring(0, 8),
                locationId,
                buildingId,
                addressLine,
                wildfireScore,
                floodZone,
                distanceToCoast,
                roofScore,
                hailIndex,
                category
        );
        hi.setEvaluatedAt(java.time.LocalDateTime.now().toString());

        repo.saveHazardIntelligence(hi);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("message", "Property environmental risk profile enriched via HazardHub Marketplace Integration");
        response.put("hazardIntelligence", hi);
        return response;
    }

    @GetMapping("/location/{locationId}")
    public Map<String, Object> getHazardByLocation(@PathVariable String locationId) {
        Map<String, Object> response = new HashMap<>();
        HazardIntelligence hi = repo.getHazardIntelligenceByLocation(locationId);
        if (hi != null) {
            response.put("found", true);
            response.put("hazardIntelligence", hi);
        } else {
            response.put("found", false);
            response.put("message", "No hazard record found for locationId: " + locationId);
        }
        return response;
    }

    @GetMapping("/list")
    public List<HazardIntelligence> getAllHazardRecords() {
        List<HazardIntelligence> list = repo.getAllHazardIntelligence();
        if (list.isEmpty()) {
            // Seed a default demo hazard record if empty
            HazardIntelligence demo = new HazardIntelligence("haz-demo-101", "accloc-1", "bldg-1",
                    "100 Industrial Parkway, Chicago, IL", 32, "Zone X", 420.0, 4.1, "Low", "Standard Risk");
            demo.setEvaluatedAt(java.time.LocalDateTime.now().toString());
            repo.saveHazardIntelligence(demo);
            list.add(demo);
        }
        return list;
    }
}
