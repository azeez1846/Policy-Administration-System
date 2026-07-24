package com.policycenter.model;

/**
 * Guidewire PolicyCenter Model: TelematicsReading (IoT Commercial Fleet Ingestion)
 * Represents a live telematics data payload captured from connected vehicle sensors.
 */
public class TelematicsReading {
    private String readingId;
    private String vehicleVin;
    private String vehicleName;
    private String driverName;
    private String timestamp;
    private double speedMph;
    private int hardBrakeEvents;
    private int rapidAccelerationEvents;
    private int nightDrivingMinutes;
    private double safetyScore; // 0.0 to 100.0
    private double premiumAdjustmentFactor; // e.g. -0.15 (-15%) to +0.25 (+25%)
    private String riskGrade; // EXCELLENT, LOW_RISK, MODERATE_RISK, HIGH_RISK, EXTREME_RISK

    public TelematicsReading() {}

    public TelematicsReading(String readingId, String vehicleVin, String vehicleName, String driverName,
                             String timestamp, double speedMph, int hardBrakeEvents,
                             int rapidAccelerationEvents, int nightDrivingMinutes,
                             double safetyScore, double premiumAdjustmentFactor, String riskGrade) {
        this.readingId = readingId;
        this.vehicleVin = vehicleVin;
        this.vehicleName = vehicleName;
        this.driverName = driverName;
        this.timestamp = timestamp;
        this.speedMph = speedMph;
        this.hardBrakeEvents = hardBrakeEvents;
        this.rapidAccelerationEvents = rapidAccelerationEvents;
        this.nightDrivingMinutes = nightDrivingMinutes;
        this.safetyScore = safetyScore;
        this.premiumAdjustmentFactor = premiumAdjustmentFactor;
        this.riskGrade = riskGrade;
    }

    public String getReadingId() { return readingId; }
    public void setReadingId(String readingId) { this.readingId = readingId; }
    public String getVehicleVin() { return vehicleVin; }
    public void setVehicleVin(String vehicleVin) { this.vehicleVin = vehicleVin; }
    public String getVehicleName() { return vehicleName; }
    public void setVehicleName(String vehicleName) { this.vehicleName = vehicleName; }
    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public double getSpeedMph() { return speedMph; }
    public void setSpeedMph(double speedMph) { this.speedMph = speedMph; }
    public int getHardBrakeEvents() { return hardBrakeEvents; }
    public void setHardBrakeEvents(int hardBrakeEvents) { this.hardBrakeEvents = hardBrakeEvents; }
    public int getRapidAccelerationEvents() { return rapidAccelerationEvents; }
    public void setRapidAccelerationEvents(int rapidAccelerationEvents) { this.rapidAccelerationEvents = rapidAccelerationEvents; }
    public int getNightDrivingMinutes() { return nightDrivingMinutes; }
    public void setNightDrivingMinutes(int nightDrivingMinutes) { this.nightDrivingMinutes = nightDrivingMinutes; }
    public double getSafetyScore() { return safetyScore; }
    public void setSafetyScore(double safetyScore) { this.safetyScore = safetyScore; }
    public double getPremiumAdjustmentFactor() { return premiumAdjustmentFactor; }
    public void setPremiumAdjustmentFactor(double premiumAdjustmentFactor) { this.premiumAdjustmentFactor = premiumAdjustmentFactor; }
    public String getRiskGrade() { return riskGrade; }
    public void setRiskGrade(String riskGrade) { this.riskGrade = riskGrade; }
}
