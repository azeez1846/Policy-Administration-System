package com.policycenter.model;

/**
 * Guidewire PolicyCenter OOTB Entity: ProductModel
 *
 * Defines the insurance product configuration — the lines of business available,
 * their availability by jurisdiction, and the product-level settings. In production
 * PolicyCenter, this maps to the Product Model Designer (PMD) configuration.
 */
public class ProductModel {
    private String publicId;
    private String productCode;       // CommercialProperty, CommercialAuto, WorkersComp, GeneralLiability
    private String productName;
    private String productAbbrev;     // CP, CA, WC, GL
    private String policyLinePattern; // CPLine, CALine, WCLine, GLLine
    private String availableJurisdictions; // Comma-separated state codes or "ALL"
    private String effectiveDate;
    private String expirationDate;
    private String status;            // Active, Draft, Retired
    private boolean renewalEnabled;
    private boolean cancellationEnabled;
    private boolean auditEnabled;
    private String defaultPaymentPlan;

    public ProductModel() {}

    public ProductModel(String publicId, String productCode, String productName,
                        String productAbbrev, String policyLinePattern, String status) {
        this.publicId = publicId;
        this.productCode = productCode;
        this.productName = productName;
        this.productAbbrev = productAbbrev;
        this.policyLinePattern = policyLinePattern;
        this.status = status;
    }

    // --- Getters & Setters ---
    public String getPublicId() { return publicId; }
    public void setPublicId(String publicId) { this.publicId = publicId; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getProductAbbrev() { return productAbbrev; }
    public void setProductAbbrev(String productAbbrev) { this.productAbbrev = productAbbrev; }
    public String getPolicyLinePattern() { return policyLinePattern; }
    public void setPolicyLinePattern(String policyLinePattern) { this.policyLinePattern = policyLinePattern; }
    public String getAvailableJurisdictions() { return availableJurisdictions; }
    public void setAvailableJurisdictions(String availableJurisdictions) { this.availableJurisdictions = availableJurisdictions; }
    public String getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(String effectiveDate) { this.effectiveDate = effectiveDate; }
    public String getExpirationDate() { return expirationDate; }
    public void setExpirationDate(String expirationDate) { this.expirationDate = expirationDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean isRenewalEnabled() { return renewalEnabled; }
    public void setRenewalEnabled(boolean renewalEnabled) { this.renewalEnabled = renewalEnabled; }
    public boolean isCancellationEnabled() { return cancellationEnabled; }
    public void setCancellationEnabled(boolean cancellationEnabled) { this.cancellationEnabled = cancellationEnabled; }
    public boolean isAuditEnabled() { return auditEnabled; }
    public void setAuditEnabled(boolean auditEnabled) { this.auditEnabled = auditEnabled; }
    public String getDefaultPaymentPlan() { return defaultPaymentPlan; }
    public void setDefaultPaymentPlan(String defaultPaymentPlan) { this.defaultPaymentPlan = defaultPaymentPlan; }
}
