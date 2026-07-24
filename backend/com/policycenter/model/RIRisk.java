package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_ririsk")
public class RIRisk {

    @Id
    @Column(name = "public_id")
    private String publicID;

    @Column(name = "policy_period_id")
    private String policyPeriodID;

    @Column(name = "risk_number")
    private String riskNumber;

    @Column(name = "total_insured_value")
    private double totalInsuredValue;

    @Column(name = "probable_maximum_loss")
    private double probableMaximumLoss;

    @Column(name = "status")
    private String status;

    public RIRisk() {}

    public RIRisk(String publicID, String policyPeriodID, String riskNumber, double totalInsuredValue, double probableMaximumLoss, String status) {
        this.publicID = publicID;
        this.policyPeriodID = policyPeriodID;
        this.riskNumber = riskNumber;
        this.totalInsuredValue = totalInsuredValue;
        this.probableMaximumLoss = probableMaximumLoss;
        this.status = status;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getPolicyPeriodID() { return policyPeriodID; }
    public void setPolicyPeriodID(String policyPeriodID) { this.policyPeriodID = policyPeriodID; }

    public String getRiskNumber() { return riskNumber; }
    public void setRiskNumber(String riskNumber) { this.riskNumber = riskNumber; }

    public double getTotalInsuredValue() { return totalInsuredValue; }
    public void setTotalInsuredValue(double totalInsuredValue) { this.totalInsuredValue = totalInsuredValue; }

    public double getProbableMaximumLoss() { return probableMaximumLoss; }
    public void setProbableMaximumLoss(double probableMaximumLoss) { this.probableMaximumLoss = probableMaximumLoss; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
