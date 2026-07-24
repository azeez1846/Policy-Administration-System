package com.policycenter.model;

public class ProducerCode {
    private String publicID;
    private String code;
    private String description;
    private String status; // Active, Suspended
    private double commissionRate;

    public ProducerCode() {}

    public ProducerCode(String publicID, String code, String description, double commissionRate) {
        this.publicID = publicID;
        this.code = code;
        this.description = description;
        this.commissionRate = commissionRate;
        this.status = "Active";
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getCommissionRate() { return commissionRate; }
    public void setCommissionRate(double commissionRate) { this.commissionRate = commissionRate; }
}
