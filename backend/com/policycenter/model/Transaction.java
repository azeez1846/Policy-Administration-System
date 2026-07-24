package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_transaction")
public class Transaction {

    @Id
    @Column(name = "public_id")
    private String publicID;

    @Column(name = "policy_period_id")
    private String policyPeriodID;

    @Column(name = "cost_id")
    private String costID;

    @Column(name = "amount")
    private double amount;

    @Column(name = "charged")
    private boolean charged;

    @Column(name = "written")
    private boolean written;

    @Column(name = "eff_date")
    private String effDate;

    @Column(name = "exp_date")
    private String expDate;

    @Column(name = "posted_date")
    private String postedDate;

    public Transaction() {}

    public Transaction(String publicID, String policyPeriodID, String costID, double amount, boolean charged, boolean written, String effDate, String expDate, String postedDate) {
        this.publicID = publicID;
        this.policyPeriodID = policyPeriodID;
        this.costID = costID;
        this.amount = amount;
        this.charged = charged;
        this.written = written;
        this.effDate = effDate;
        this.expDate = expDate;
        this.postedDate = postedDate;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getPolicyPeriodID() { return policyPeriodID; }
    public void setPolicyPeriodID(String policyPeriodID) { this.policyPeriodID = policyPeriodID; }

    public String getCostID() { return costID; }
    public void setCostID(String costID) { this.costID = costID; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public boolean isCharged() { return charged; }
    public void setCharged(boolean charged) { this.charged = charged; }

    public boolean isWritten() { return written; }
    public void setWritten(boolean written) { this.written = written; }

    public String getEffDate() { return effDate; }
    public void setEffDate(String effDate) { this.effDate = effDate; }

    public String getExpDate() { return expDate; }
    public void setExpDate(String expDate) { this.expDate = expDate; }

    public String getPostedDate() { return postedDate; }
    public void setPostedDate(String postedDate) { this.postedDate = postedDate; }
}
