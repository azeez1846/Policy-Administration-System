package com.policycenter.model;

public class UWAuthorityProfile {
    private String publicID;
    private String profileName;
    private double maxBuildingLimit;
    private double maxTotalPremium;

    public UWAuthorityProfile() {}

    public UWAuthorityProfile(String publicID, String profileName, double maxBuildingLimit, double maxTotalPremium) {
        this.publicID = publicID;
        this.profileName = profileName;
        this.maxBuildingLimit = maxBuildingLimit;
        this.maxTotalPremium = maxTotalPremium;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getProfileName() { return profileName; }
    public void setProfileName(String profileName) { this.profileName = profileName; }

    public double getMaxBuildingLimit() { return maxBuildingLimit; }
    public void setMaxBuildingLimit(double maxBuildingLimit) { this.maxBuildingLimit = maxBuildingLimit; }

    public double getMaxTotalPremium() { return maxTotalPremium; }
    public void setMaxTotalPremium(double maxTotalPremium) { this.maxTotalPremium = maxTotalPremium; }
}
