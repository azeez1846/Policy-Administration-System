package com.policycenter.model;

/**
 * Guidewire Marketplace Accelerator Entity: HazardIntelligence
 *
 * Persists HazardHub environmental risk scores, wildfire vulnerability,
 * flood zone classification, aerial roof condition ratings, and hail severity metrics.
 */
public class HazardIntelligence {
    private String id;
    private String locationId;
    private String buildingId;
    private String addressLine;
    private int wildfireScore;          // 0 - 100
    private String floodZone;           // Zone A, Zone AE, Zone X, Zone VE
    private double distanceToCoastMiles;
    private double roofConditionScore;  // 1.0 (Poor) - 5.0 (Excellent)
    private String hailSeverityIndex;   // Low, Medium, Severe
    private String riskCategory;        // Low, Moderate, High, Extreme
    private String evaluatedAt;

    public HazardIntelligence() {}

    public HazardIntelligence(String id, String locationId, String buildingId, String addressLine,
                              int wildfireScore, String floodZone, double distanceToCoastMiles,
                              double roofConditionScore, String hailSeverityIndex, String riskCategory) {
        this.id = id;
        this.locationId = locationId;
        this.buildingId = buildingId;
        this.addressLine = addressLine;
        this.wildfireScore = wildfireScore;
        this.floodZone = floodZone;
        this.distanceToCoastMiles = distanceToCoastMiles;
        this.roofConditionScore = roofConditionScore;
        this.hailSeverityIndex = hailSeverityIndex;
        this.riskCategory = riskCategory;
    }

    // --- Getters and Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getLocationId() { return locationId; }
    public void setLocationId(String locationId) { this.locationId = locationId; }
    public String getBuildingId() { return buildingId; }
    public void setBuildingId(String buildingId) { this.buildingId = buildingId; }
    public String getAddressLine() { return addressLine; }
    public void setAddressLine(String addressLine) { this.addressLine = addressLine; }
    public int getWildfireScore() { return wildfireScore; }
    public void setWildfireScore(int wildfireScore) { this.wildfireScore = wildfireScore; }
    public String getFloodZone() { return floodZone; }
    public void setFloodZone(String floodZone) { this.floodZone = floodZone; }
    public double getDistanceToCoastMiles() { return distanceToCoastMiles; }
    public void setDistanceToCoastMiles(double distanceToCoastMiles) { this.distanceToCoastMiles = distanceToCoastMiles; }
    public double getRoofConditionScore() { return roofConditionScore; }
    public void setRoofConditionScore(double roofConditionScore) { this.roofConditionScore = roofConditionScore; }
    public String getHailSeverityIndex() { return hailSeverityIndex; }
    public void setHailSeverityIndex(String hailSeverityIndex) { this.hailSeverityIndex = hailSeverityIndex; }
    public String getRiskCategory() { return riskCategory; }
    public void setRiskCategory(String riskCategory) { this.riskCategory = riskCategory; }
    public String getEvaluatedAt() { return evaluatedAt; }
    public void setEvaluatedAt(String evaluatedAt) { this.evaluatedAt = evaluatedAt; }
}
