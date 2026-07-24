package com.policycenter.model;

/**
 * Guidewire PolicyCenter OOTB Entity: Region
 *
 * Defines geographic underwriting regions used for territory assignment,
 * UW authority routing, and regulatory jurisdiction mapping. Regions can
 * represent states, multi-state zones, or custom carrier-defined territories.
 */
public class Region {
    private String publicId;
    private String regionCode;
    private String regionName;
    private String regionType;        // State, Zone, Territory, Custom
    private String states;            // Comma-separated state codes
    private String uwManagerId;       // User responsible for this region
    private boolean catastropheExposed;
    private String regulatoryZone;

    public Region() {}

    public Region(String publicId, String regionCode, String regionName, String regionType, String states) {
        this.publicId = publicId;
        this.regionCode = regionCode;
        this.regionName = regionName;
        this.regionType = regionType;
        this.states = states;
    }

    // --- Getters & Setters ---
    public String getPublicId() { return publicId; }
    public void setPublicId(String publicId) { this.publicId = publicId; }
    public String getRegionCode() { return regionCode; }
    public void setRegionCode(String regionCode) { this.regionCode = regionCode; }
    public String getRegionName() { return regionName; }
    public void setRegionName(String regionName) { this.regionName = regionName; }
    public String getRegionType() { return regionType; }
    public void setRegionType(String regionType) { this.regionType = regionType; }
    public String getStates() { return states; }
    public void setStates(String states) { this.states = states; }
    public String getUwManagerId() { return uwManagerId; }
    public void setUwManagerId(String uwManagerId) { this.uwManagerId = uwManagerId; }
    public boolean isCatastropheExposed() { return catastropheExposed; }
    public void setCatastropheExposed(boolean catastropheExposed) { this.catastropheExposed = catastropheExposed; }
    public String getRegulatoryZone() { return regulatoryZone; }
    public void setRegulatoryZone(String regulatoryZone) { this.regulatoryZone = regulatoryZone; }
}
