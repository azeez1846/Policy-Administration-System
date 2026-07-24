package com.policycenter.model;

/**
 * Guidewire PolicyCenter OOTB Entity: ClaimDetail
 *
 * Extended claim fields providing deeper integration with ClaimCenter.
 * Includes adjuster assignment, subrogation status, litigation tracking,
 * and reserve breakdowns. This supplements the basic Claim entity.
 */
public class ClaimDetail {
    private String publicId;
    private String claimNumber;
    private String policyNumber;
    private String claimStatus;       // Open, Closed, Reopened, Denied
    private String lossDate;
    private String reportedDate;
    private String closedDate;
    private String lossCause;
    private String lossType;          // PropertyDamage, BodilyInjury, Liability, AutoCollision
    private String adjusterName;
    private String adjusterPhone;
    private double incurredAmount;    // Paid + Reserves
    private double paidAmount;
    private double reserveAmount;
    private boolean subrogation;
    private String subrogationStatus; // None, Pending, InProgress, Recovered
    private double subrogationRecovery;
    private boolean litigation;
    private String litigationStatus;  // None, Filed, InTrial, Settled
    private String faultRating;       // AtFault, NotAtFault, Partial

    public ClaimDetail() {}

    public ClaimDetail(String publicId, String claimNumber, String policyNumber,
                       String claimStatus, String lossDate, String lossCause,
                       double paidAmount, double reserveAmount) {
        this.publicId = publicId;
        this.claimNumber = claimNumber;
        this.policyNumber = policyNumber;
        this.claimStatus = claimStatus;
        this.lossDate = lossDate;
        this.lossCause = lossCause;
        this.paidAmount = paidAmount;
        this.reserveAmount = reserveAmount;
        this.incurredAmount = paidAmount + reserveAmount;
    }

    // --- Getters & Setters ---
    public String getPublicId() { return publicId; }
    public void setPublicId(String publicId) { this.publicId = publicId; }
    public String getClaimNumber() { return claimNumber; }
    public void setClaimNumber(String claimNumber) { this.claimNumber = claimNumber; }
    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }
    public String getClaimStatus() { return claimStatus; }
    public void setClaimStatus(String claimStatus) { this.claimStatus = claimStatus; }
    public String getLossDate() { return lossDate; }
    public void setLossDate(String lossDate) { this.lossDate = lossDate; }
    public String getReportedDate() { return reportedDate; }
    public void setReportedDate(String reportedDate) { this.reportedDate = reportedDate; }
    public String getClosedDate() { return closedDate; }
    public void setClosedDate(String closedDate) { this.closedDate = closedDate; }
    public String getLossCause() { return lossCause; }
    public void setLossCause(String lossCause) { this.lossCause = lossCause; }
    public String getLossType() { return lossType; }
    public void setLossType(String lossType) { this.lossType = lossType; }
    public String getAdjusterName() { return adjusterName; }
    public void setAdjusterName(String adjusterName) { this.adjusterName = adjusterName; }
    public String getAdjusterPhone() { return adjusterPhone; }
    public void setAdjusterPhone(String adjusterPhone) { this.adjusterPhone = adjusterPhone; }
    public double getIncurredAmount() { return incurredAmount; }
    public void setIncurredAmount(double incurredAmount) { this.incurredAmount = incurredAmount; }
    public double getPaidAmount() { return paidAmount; }
    public void setPaidAmount(double paidAmount) { this.paidAmount = paidAmount; }
    public double getReserveAmount() { return reserveAmount; }
    public void setReserveAmount(double reserveAmount) { this.reserveAmount = reserveAmount; }
    public boolean isSubrogation() { return subrogation; }
    public void setSubrogation(boolean subrogation) { this.subrogation = subrogation; }
    public String getSubrogationStatus() { return subrogationStatus; }
    public void setSubrogationStatus(String subrogationStatus) { this.subrogationStatus = subrogationStatus; }
    public double getSubrogationRecovery() { return subrogationRecovery; }
    public void setSubrogationRecovery(double subrogationRecovery) { this.subrogationRecovery = subrogationRecovery; }
    public boolean isLitigation() { return litigation; }
    public void setLitigation(boolean litigation) { this.litigation = litigation; }
    public String getLitigationStatus() { return litigationStatus; }
    public void setLitigationStatus(String litigationStatus) { this.litigationStatus = litigationStatus; }
    public String getFaultRating() { return faultRating; }
    public void setFaultRating(String faultRating) { this.faultRating = faultRating; }
}
