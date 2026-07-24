package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_claim")
public class Claim {

    @Id
    @Column(name = "public_id")
    private String publicID;

    @Column(name = "claim_number")
    private String claimNumber;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "loss_date")
    private String lossDate;

    @Column(name = "cause_of_loss")
    private String causeOfLoss;

    @Column(name = "total_paid")
    private double totalPaid;

    @Column(name = "reserve_amount")
    private double reserveAmount;

    @Column(name = "claim_status")
    private String claimStatus;

    public Claim() {
        this.claimStatus = "Open";
    }

    public Claim(String publicID, String claimNumber, String accountNumber, String lossDate, String causeOfLoss, double totalPaid, double reserveAmount) {
        this.publicID = publicID;
        this.claimNumber = claimNumber;
        this.accountNumber = accountNumber;
        this.lossDate = lossDate;
        this.causeOfLoss = causeOfLoss;
        this.totalPaid = totalPaid;
        this.reserveAmount = reserveAmount;
        this.claimStatus = "Closed";
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getClaimNumber() { return claimNumber; }
    public void setClaimNumber(String claimNumber) { this.claimNumber = claimNumber; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getLossDate() { return lossDate; }
    public void setLossDate(String lossDate) { this.lossDate = lossDate; }

    public String getCauseOfLoss() { return causeOfLoss; }
    public void setCauseOfLoss(String causeOfLoss) { this.causeOfLoss = causeOfLoss; }

    public double getTotalPaid() { return totalPaid; }
    public void setTotalPaid(double totalPaid) { this.totalPaid = totalPaid; }

    public double getReserveAmount() { return reserveAmount; }
    public void setReserveAmount(double reserveAmount) { this.reserveAmount = reserveAmount; }

    public String getClaimStatus() { return claimStatus; }
    public void setClaimStatus(String claimStatus) { this.claimStatus = claimStatus; }
}
