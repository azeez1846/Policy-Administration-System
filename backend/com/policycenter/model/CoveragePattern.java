package com.policycenter.model;

/**
 * Guidewire PolicyCenter OOTB Entity: CoveragePattern
 *
 * Master catalog entry for a coverage type. Defines the default limits,
 * deductible options, availability by line of business, and whether the
 * coverage is mandatory or optional in a given jurisdiction.
 */
public class CoveragePattern {
    private String publicId;
    private String patternCode;       // CPBldgCov, CPBPPCov, CALiabCov, GLCGLCov
    private String name;
    private String description;
    private String policyLinePattern; // CPLine, CALine, WCLine, GLLine
    private String coverableType;     // Building, Vehicle, Location, Line, PolicyPeriod
    private double defaultLimit;
    private double defaultDeductible;
    private double minLimit;
    private double maxLimit;
    private boolean mandatory;
    private boolean electable;
    private String category;          // Property, Liability, AutoPhysicalDamage, MedPay

    public CoveragePattern() {}

    public CoveragePattern(String publicId, String patternCode, String name,
                           String policyLinePattern, String coverableType,
                           double defaultLimit, double defaultDeductible) {
        this.publicId = publicId;
        this.patternCode = patternCode;
        this.name = name;
        this.policyLinePattern = policyLinePattern;
        this.coverableType = coverableType;
        this.defaultLimit = defaultLimit;
        this.defaultDeductible = defaultDeductible;
    }

    // --- Getters & Setters ---
    public String getPublicId() { return publicId; }
    public void setPublicId(String publicId) { this.publicId = publicId; }
    public String getPatternCode() { return patternCode; }
    public void setPatternCode(String patternCode) { this.patternCode = patternCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPolicyLinePattern() { return policyLinePattern; }
    public void setPolicyLinePattern(String policyLinePattern) { this.policyLinePattern = policyLinePattern; }
    public String getCoverableType() { return coverableType; }
    public void setCoverableType(String coverableType) { this.coverableType = coverableType; }
    public double getDefaultLimit() { return defaultLimit; }
    public void setDefaultLimit(double defaultLimit) { this.defaultLimit = defaultLimit; }
    public double getDefaultDeductible() { return defaultDeductible; }
    public void setDefaultDeductible(double defaultDeductible) { this.defaultDeductible = defaultDeductible; }
    public double getMinLimit() { return minLimit; }
    public void setMinLimit(double minLimit) { this.minLimit = minLimit; }
    public double getMaxLimit() { return maxLimit; }
    public void setMaxLimit(double maxLimit) { this.maxLimit = maxLimit; }
    public boolean isMandatory() { return mandatory; }
    public void setMandatory(boolean mandatory) { this.mandatory = mandatory; }
    public boolean isElectable() { return electable; }
    public void setElectable(boolean electable) { this.electable = electable; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
