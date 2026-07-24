package com.policycenter.model;

/**
 * Guidewire PolicyCenter OOTB Entity: Jurisdiction
 *
 * Represents a state or province with its regulatory rules, filing requirements,
 * and compliance constraints. Used by the product model to determine which
 * coverages are mandatory, which rate filings apply, and regulatory reporting needs.
 */
public class Jurisdiction {
    private String publicId;
    private String stateCode;         // Two-letter code (IL, CA, FL, NY, TX)
    private String stateName;
    private String country;           // US, CA
    private String regulatoryBody;    // DOI name
    private boolean fileAndUse;       // Filing requirement type
    private boolean priorApproval;
    private double defaultPremiumTaxRate;
    private boolean nfipParticipant;  // National Flood Insurance Program
    private String residualMarket;    // FAIR Plan, Assigned Risk, etc.
    private boolean catastropheExposed;
    private String timezone;

    public Jurisdiction() {}

    public Jurisdiction(String publicId, String stateCode, String stateName, String country,
                        double defaultPremiumTaxRate) {
        this.publicId = publicId;
        this.stateCode = stateCode;
        this.stateName = stateName;
        this.country = country;
        this.defaultPremiumTaxRate = defaultPremiumTaxRate;
    }

    // --- Getters & Setters ---
    public String getPublicId() { return publicId; }
    public void setPublicId(String publicId) { this.publicId = publicId; }
    public String getStateCode() { return stateCode; }
    public void setStateCode(String stateCode) { this.stateCode = stateCode; }
    public String getStateName() { return stateName; }
    public void setStateName(String stateName) { this.stateName = stateName; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getRegulatoryBody() { return regulatoryBody; }
    public void setRegulatoryBody(String regulatoryBody) { this.regulatoryBody = regulatoryBody; }
    public boolean isFileAndUse() { return fileAndUse; }
    public void setFileAndUse(boolean fileAndUse) { this.fileAndUse = fileAndUse; }
    public boolean isPriorApproval() { return priorApproval; }
    public void setPriorApproval(boolean priorApproval) { this.priorApproval = priorApproval; }
    public double getDefaultPremiumTaxRate() { return defaultPremiumTaxRate; }
    public void setDefaultPremiumTaxRate(double defaultPremiumTaxRate) { this.defaultPremiumTaxRate = defaultPremiumTaxRate; }
    public boolean isNfipParticipant() { return nfipParticipant; }
    public void setNfipParticipant(boolean nfipParticipant) { this.nfipParticipant = nfipParticipant; }
    public String getResidualMarket() { return residualMarket; }
    public void setResidualMarket(String residualMarket) { this.residualMarket = residualMarket; }
    public boolean isCatastropheExposed() { return catastropheExposed; }
    public void setCatastropheExposed(boolean catastropheExposed) { this.catastropheExposed = catastropheExposed; }
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
}
