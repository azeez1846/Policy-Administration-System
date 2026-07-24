package com.policycenter.model;

/**
 * Guidewire PolicyCenter OOTB Entity: TaxSurcharge
 *
 * Represents state/municipal tax and surcharge line items applied to a policy
 * period. Includes fire tax, EMPA surcharges, terrorism surcharges, and other
 * regulatory assessments. These are calculated during rating and appear as
 * separate line items in the premium breakdown.
 */
public class TaxSurcharge {
    private String publicId;
    private String periodId;
    private String taxType;           // StatePremiumTax, FireTax, StampingFee, TerrorismSurcharge, EMPA
    private String jurisdiction;      // State code
    private double taxRate;           // Percentage
    private double taxableAmount;     // Base premium amount the tax applies to
    private double taxAmount;         // Calculated tax
    private String description;
    private boolean overridden;
    private double overrideAmount;

    public TaxSurcharge() {}

    public TaxSurcharge(String publicId, String periodId, String taxType,
                        String jurisdiction, double taxRate, double taxableAmount, double taxAmount) {
        this.publicId = publicId;
        this.periodId = periodId;
        this.taxType = taxType;
        this.jurisdiction = jurisdiction;
        this.taxRate = taxRate;
        this.taxableAmount = taxableAmount;
        this.taxAmount = taxAmount;
    }

    // --- Getters & Setters ---
    public String getPublicId() { return publicId; }
    public void setPublicId(String publicId) { this.publicId = publicId; }
    public String getPeriodId() { return periodId; }
    public void setPeriodId(String periodId) { this.periodId = periodId; }
    public String getTaxType() { return taxType; }
    public void setTaxType(String taxType) { this.taxType = taxType; }
    public String getJurisdiction() { return jurisdiction; }
    public void setJurisdiction(String jurisdiction) { this.jurisdiction = jurisdiction; }
    public double getTaxRate() { return taxRate; }
    public void setTaxRate(double taxRate) { this.taxRate = taxRate; }
    public double getTaxableAmount() { return taxableAmount; }
    public void setTaxableAmount(double taxableAmount) { this.taxableAmount = taxableAmount; }
    public double getTaxAmount() { return taxAmount; }
    public void setTaxAmount(double taxAmount) { this.taxAmount = taxAmount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isOverridden() { return overridden; }
    public void setOverridden(boolean overridden) { this.overridden = overridden; }
    public double getOverrideAmount() { return overrideAmount; }
    public void setOverrideAmount(double overrideAmount) { this.overrideAmount = overrideAmount; }
}
