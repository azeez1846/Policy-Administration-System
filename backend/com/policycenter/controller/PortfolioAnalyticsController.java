package com.policycenter.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class PortfolioAnalyticsController {

    @GetMapping("/portfolio")
    public Map<String, Object> getPortfolioAnalytics() {
        return Map.of(
            "grossWrittenPremium", 14850000.0,
            "boundPolicyCount", 1240,
            "overallLossRatio", 44.2,
            "conversionRatePct", 68.5,
            "avgTurnaroundDays", 1.8,
            "lineBreakdown", List.of(
                Map.of("line", "Commercial Property", "gwp", 8200000.0, "lossRatio", 41.5, "policies", 620),
                Map.of("line", "Commercial Auto", "gwp", 4100000.0, "lossRatio", 52.8, "policies", 380),
                Map.of("line", "Workers' Compensation", "gwp", 2550000.0, "lossRatio", 38.0, "policies", 240)
            )
        );
    }
}
