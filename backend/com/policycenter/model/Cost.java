package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_cost")
public class Cost {

    @Id
    @Column(name = "public_id")
    private String publicID;

    @Column(name = "cost_type")
    private String costType;

    @Column(name = "description")
    private String description;

    @Column(name = "actual_amount")
    private double actualAmount;

    @Column(name = "proration_factor")
    private double prorationFactor;

    @Column(name = "charge_pattern")
    private String chargePattern;

    @Column(name = "rate_amount")
    private double rateAmount;

    public Cost() {
        this.prorationFactor = 1.0;
        this.chargePattern = "Premium";
    }

    public Cost(String publicID, String costType, String description, double actualAmount) {
        this.publicID = publicID;
        this.costType = costType;
        this.description = description;
        this.actualAmount = actualAmount;
        this.prorationFactor = 1.0;
        this.chargePattern = "Premium";
        this.rateAmount = actualAmount;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getCostType() { return costType; }
    public void setCostType(String costType) { this.costType = costType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getActualAmount() { return actualAmount; }
    public void setActualAmount(double actualAmount) { this.actualAmount = actualAmount; }

    public double getActualTermAmount() { return actualAmount; }
    public void setActualTermAmount(double amount) { this.actualAmount = amount; }

    public double getProrationFactor() { return prorationFactor; }
    public void setProrationFactor(double prorationFactor) { this.prorationFactor = prorationFactor; }

    public String getChargePattern() { return chargePattern; }
    public void setChargePattern(String chargePattern) { this.chargePattern = chargePattern; }

    public double getRateAmount() { return rateAmount; }
    public void setRateAmount(double rateAmount) { this.rateAmount = rateAmount; }
}
