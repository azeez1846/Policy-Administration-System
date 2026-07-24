package com.policycenter.model;

public class PolicyTerm {
    private String publicID;
    private int termNumber;
    private String effectiveDate;
    private String expirationDate;
    private String termStatus; // Active, Expired, Cancelled, Renewed

    public PolicyTerm() {}

    public PolicyTerm(String publicID, int termNumber, String effectiveDate, String expirationDate) {
        this.publicID = publicID;
        this.termNumber = termNumber;
        this.effectiveDate = effectiveDate;
        this.expirationDate = expirationDate;
        this.termStatus = "Active";
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public int getTermNumber() { return termNumber; }
    public void setTermNumber(int termNumber) { this.termNumber = termNumber; }

    public String getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(String effectiveDate) { this.effectiveDate = effectiveDate; }

    public String getExpirationDate() { return expirationDate; }
    public void setExpirationDate(String expirationDate) { this.expirationDate = expirationDate; }

    public String getTermStatus() { return termStatus; }
    public void setTermStatus(String termStatus) { this.termStatus = termStatus; }
}
