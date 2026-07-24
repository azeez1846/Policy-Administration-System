package com.policycenter.model;

/**
 * Guidewire PolicyCenter OOTB Entity: PolicyCommission
 *
 * Represents the commission calculation for a specific cost on a policy period.
 * Tracks producer commission rates, amounts, and commission plans. Each Cost
 * can have one or more commission entries (primary producer, sub-producer).
 */
public class PolicyCommission {
    private String publicId;
    private String periodId;
    private String costId;
    private String producerCodeId;
    private String commissionPlan;    // Standard, Contingent, Supplemental, Override
    private double commissionRate;    // Percentage (e.g., 15.0 = 15%)
    private double commissionAmount;
    private String role;              // Primary, SubProducer, Referring
    private String paymentStatus;     // Pending, Paid, Clawed-Back
    private String effectiveDate;

    public PolicyCommission() {}

    public PolicyCommission(String publicId, String periodId, String costId,
                            String producerCodeId, double commissionRate, double commissionAmount) {
        this.publicId = publicId;
        this.periodId = periodId;
        this.costId = costId;
        this.producerCodeId = producerCodeId;
        this.commissionRate = commissionRate;
        this.commissionAmount = commissionAmount;
    }

    // --- Getters & Setters ---
    public String getPublicId() { return publicId; }
    public void setPublicId(String publicId) { this.publicId = publicId; }
    public String getPeriodId() { return periodId; }
    public void setPeriodId(String periodId) { this.periodId = periodId; }
    public String getCostId() { return costId; }
    public void setCostId(String costId) { this.costId = costId; }
    public String getProducerCodeId() { return producerCodeId; }
    public void setProducerCodeId(String producerCodeId) { this.producerCodeId = producerCodeId; }
    public String getCommissionPlan() { return commissionPlan; }
    public void setCommissionPlan(String commissionPlan) { this.commissionPlan = commissionPlan; }
    public double getCommissionRate() { return commissionRate; }
    public void setCommissionRate(double commissionRate) { this.commissionRate = commissionRate; }
    public double getCommissionAmount() { return commissionAmount; }
    public void setCommissionAmount(double commissionAmount) { this.commissionAmount = commissionAmount; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(String effectiveDate) { this.effectiveDate = effectiveDate; }
}
