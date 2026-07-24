package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_riprogram")
public class RIProgram {

    @Id
    @Column(name = "public_id")
    private String publicID;

    @Column(name = "name")
    private String name;

    @Column(name = "effective_date")
    private String effectiveDate;

    @Column(name = "expiration_date")
    private String expirationDate;

    @Column(name = "status")
    private String status;

    @Column(name = "single_risk_limit")
    private double singleRiskLimit;

    public RIProgram() {}

    public RIProgram(String publicID, String name, String effectiveDate, String expirationDate, String status, double singleRiskLimit) {
        this.publicID = publicID;
        this.name = name;
        this.effectiveDate = effectiveDate;
        this.expirationDate = expirationDate;
        this.status = status;
        this.singleRiskLimit = singleRiskLimit;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(String effectiveDate) { this.effectiveDate = effectiveDate; }

    public String getExpirationDate() { return expirationDate; }
    public void setExpirationDate(String expirationDate) { this.expirationDate = expirationDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getSingleRiskLimit() { return singleRiskLimit; }
    public void setSingleRiskLimit(double singleRiskLimit) { this.singleRiskLimit = singleRiskLimit; }
}
