package com.policycenter.model;

/**
 * Guidewire PolicyCenter Entity: CatastropheMoratorium (PolicyHold spatial extension)
 * Represents a geographic binding moratorium declared for catastrophe events.
 */
public class CatastropheMoratorium {
    private String id;
    private String name;
    private String catastropheType; // Hurricane, Wildfire, Flood, Earthquake, Tornado
    private double lat;
    private double lng;
    private double radiusMiles;
    private String effectiveDate;
    private String expirationDate;
    private String status; // ACTIVE, LIFTED
    private String createdBy;
    private boolean blocksQuote;
    private boolean blocksBind;

    public CatastropheMoratorium() {}

    public CatastropheMoratorium(String id, String name, String catastropheType, double lat, double lng,
                                 double radiusMiles, String effectiveDate, String expirationDate,
                                 String status, String createdBy, boolean blocksQuote, boolean blocksBind) {
        this.id = id;
        this.name = name;
        this.catastropheType = catastropheType;
        this.lat = lat;
        this.lng = lng;
        this.radiusMiles = radiusMiles;
        this.effectiveDate = effectiveDate;
        this.expirationDate = expirationDate;
        this.status = status;
        this.createdBy = createdBy;
        this.blocksQuote = blocksQuote;
        this.blocksBind = blocksBind;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCatastropheType() { return catastropheType; }
    public void setCatastropheType(String catastropheType) { this.catastropheType = catastropheType; }
    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }
    public double getLng() { return lng; }
    public void setLng(double lng) { this.lng = lng; }
    public double getRadiusMiles() { return radiusMiles; }
    public void setRadiusMiles(double radiusMiles) { this.radiusMiles = radiusMiles; }
    public String getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(String effectiveDate) { this.effectiveDate = effectiveDate; }
    public String getExpirationDate() { return expirationDate; }
    public void setExpirationDate(String expirationDate) { this.expirationDate = expirationDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public boolean isBlocksQuote() { return blocksQuote; }
    public void setBlocksQuote(boolean blocksQuote) { this.blocksQuote = blocksQuote; }
    public boolean isBlocksBind() { return blocksBind; }
    public void setBlocksBind(boolean blocksBind) { this.blocksBind = blocksBind; }
}
