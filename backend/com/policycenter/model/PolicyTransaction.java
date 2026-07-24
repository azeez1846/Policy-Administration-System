package com.policycenter.model;

public class PolicyTransaction {
    private String publicID;
    private String costID;
    private String postedDate;
    private double writtenAmount;
    private double chargedAmount;
    private String effDate;
    private String expDate;

    public PolicyTransaction() {}

    public PolicyTransaction(String publicID, String costID, double amount, String effDate, String expDate) {
        this.publicID = publicID;
        this.costID = costID;
        this.writtenAmount = amount;
        this.chargedAmount = amount;
        this.effDate = effDate;
        this.expDate = expDate;
        this.postedDate = java.time.LocalDate.now().toString();
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getCostID() { return costID; }
    public void setCostID(String costID) { this.costID = costID; }

    public String getPostedDate() { return postedDate; }
    public void setPostedDate(String postedDate) { this.postedDate = postedDate; }

    public double getWrittenAmount() { return writtenAmount; }
    public void setWrittenAmount(double writtenAmount) { this.writtenAmount = writtenAmount; }

    public double getChargedAmount() { return chargedAmount; }
    public void setChargedAmount(double chargedAmount) { this.chargedAmount = chargedAmount; }

    public String getEffDate() { return effDate; }
    public void setEffDate(String effDate) { this.effDate = effDate; }

    public String getExpDate() { return expDate; }
    public void setExpDate(String expDate) { this.expDate = expDate; }
}
