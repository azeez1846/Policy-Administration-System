package com.policycenter.service;

import com.policycenter.model.TelematicsReading;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TelematicsStreamService {

    private static final TelematicsStreamService INSTANCE = new TelematicsStreamService();
    private final Map<String, List<TelematicsReading>> fleetReadings = new ConcurrentHashMap<>();

    public TelematicsStreamService() {
        initDefaultFleetReadings();
    }

    public static TelematicsStreamService getInstance() {
        return INSTANCE;
    }

    private void initDefaultFleetReadings() {
        if (fleetReadings.isEmpty()) {
            ingestReading(new TelematicsReading("tel-01", "1FTFW1E84MKD90182", "Ford F-350 Heavy Hauler #1", "Marcus Vance", LocalDateTime.now().minusMinutes(5).toString(), 62.5, 0, 1, 15, 94.0, -0.12, "EXCELLENT"));
            ingestReading(new TelematicsReading("tel-02", "1FTFW1E84MKD90182", "Ford F-350 Heavy Hauler #1", "Marcus Vance", LocalDateTime.now().minusMinutes(45).toString(), 68.0, 1, 0, 0, 91.5, -0.10, "EXCELLENT"));
            ingestReading(new TelematicsReading("tel-03", "2C4RC1BG4KR612948", "Freightliner Cascadia #4", "Elena Rostova", LocalDateTime.now().minusMinutes(12).toString(), 79.2, 4, 3, 45, 62.0, 0.18, "HIGH_RISK"));
            ingestReading(new TelematicsReading("tel-04", "3FA6P0H78HR394821", "Kenworth T680 Sleeper #2", "David Chen", LocalDateTime.now().minusMinutes(2).toString(), 58.0, 0, 0, 10, 98.0, -0.15, "EXCELLENT"));
        }
    }

    public TelematicsReading ingestReading(TelematicsReading reading) {
        if (reading.getReadingId() == null) {
            reading.setReadingId("tel-" + System.currentTimeMillis());
        }
        if (reading.getTimestamp() == null) {
            reading.setTimestamp(LocalDateTime.now().toString());
        }

        // Calculate safety score and adjustment factor dynamically
        double baseScore = 100.0;
        baseScore -= (reading.getHardBrakeEvents() * 6.5);
        baseScore -= (reading.getRapidAccelerationEvents() * 4.0);
        if (reading.getSpeedMph() > 75.0) baseScore -= 15.0;
        if (reading.getNightDrivingMinutes() > 30) baseScore -= 8.0;

        if (baseScore < 0) baseScore = 0.0;
        reading.setSafetyScore(Math.round(baseScore * 10.0) / 10.0);

        // Grade assignment
        if (baseScore >= 90.0) {
            reading.setRiskGrade("EXCELLENT");
            reading.setPremiumAdjustmentFactor(-0.15); // -15% credit
        } else if (baseScore >= 75.0) {
            reading.setRiskGrade("LOW_RISK");
            reading.setPremiumAdjustmentFactor(-0.05); // -5% credit
        } else if (baseScore >= 60.0) {
            reading.setRiskGrade("MODERATE_RISK");
            reading.setPremiumAdjustmentFactor(0.05); // +5% surcharge
        } else if (baseScore >= 40.0) {
            reading.setRiskGrade("HIGH_RISK");
            reading.setPremiumAdjustmentFactor(0.15); // +15% surcharge
        } else {
            reading.setRiskGrade("EXTREME_RISK");
            reading.setPremiumAdjustmentFactor(0.30); // +30% surcharge
        }

        fleetReadings.computeIfAbsent(reading.getVehicleVin(), k -> new ArrayList<>()).add(0, reading);
        return reading;
    }

    public List<TelematicsReading> getAllRecentReadings() {
        List<TelematicsReading> all = new ArrayList<>();
        for (List<TelematicsReading> list : fleetReadings.values()) {
            if (!list.isEmpty()) {
                all.add(list.get(0)); // latest reading per vehicle
            }
        }
        return all;
    }

    public Map<String, Object> getFleetAnalyticsSummary() {
        List<TelematicsReading> latestReadings = getAllRecentReadings();
        double sumScore = 0.0;
        int totalHardBrakes = 0;
        int totalRapidAcc = 0;

        for (TelematicsReading r : latestReadings) {
            sumScore += r.getSafetyScore();
            totalHardBrakes += r.getHardBrakeEvents();
            totalRapidAcc += r.getRapidAccelerationEvents();
        }

        double avgFleetScore = latestReadings.isEmpty() ? 85.0 : Math.round((sumScore / latestReadings.size()) * 10.0) / 10.0;
        double netDividendPct = avgFleetScore >= 85.0 ? -0.10 : (avgFleetScore < 65.0 ? 0.15 : 0.0);

        Map<String, Object> summary = new HashMap<>();
        summary.put("monitoredVehiclesCount", latestReadings.size());
        summary.put("averageFleetSafetyScore", avgFleetScore);
        summary.put("totalHardBrakeEvents", totalHardBrakes);
        summary.put("totalRapidAccelerationEvents", totalRapidAcc);
        summary.put("fleetDividendFactor", netDividendPct);
        summary.put("statusRecommendation", avgFleetScore >= 85.0 ? "QUALIFIES FOR 10% SAFETY DIVIDEND CREDIT" : "STANDARD FLEET RATING");
        summary.put("latestReadings", latestReadings);
        return summary;
    }
}
