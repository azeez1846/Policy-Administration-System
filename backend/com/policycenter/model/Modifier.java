package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_modifier")
public class Modifier {

    @Id
    @Column(name = "public_id")
    private String publicID;

    @Column(name = "policy_period_id")
    private String policyPeriodID;

    @Column(name = "pattern_code")
    private String patternCode;

    @Column(name = "rate_factor")
    private double rateFactor;

    @Column(name = "justification")
    private String justification;

    @Column(name = "minimum_factor")
    private double minimumFactor;

    @Column(name = "maximum_factor")
    private double maximumFactor;

    public Modifier() {}

    public Modifier(String publicID, String policyPeriodID, String patternCode, double rateFactor, String justification, double minimumFactor, double maximumFactor) {
        this.publicID = publicID;
        this.policyPeriodID = policyPeriodID;
        this.patternCode = patternCode;
        this.rateFactor = rateFactor;
        this.justification = justification;
        this.minimumFactor = minimumFactor;
        this.maximumFactor = maximumFactor;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getPolicyPeriodID() { return policyPeriodID; }
    public void setPolicyPeriodID(String policyPeriodID) { this.policyPeriodID = policyPeriodID; }

    public String getPatternCode() { return patternCode; }
    public void setPatternCode(String patternCode) { this.patternCode = patternCode; }

    public double getRateFactor() { return rateFactor; }
    public void setRateFactor(double rateFactor) { this.rateFactor = rateFactor; }

    public String getJustification() { return justification; }
    public void setJustification(String justification) { this.justification = justification; }

    public double getMinimumFactor() { return minimumFactor; }
    public void setMinimumFactor(double minimumFactor) { this.minimumFactor = minimumFactor; }

    public double getMaximumFactor() { return maximumFactor; }
    public void setMaximumFactor(double maximumFactor) { this.maximumFactor = maximumFactor; }
}
