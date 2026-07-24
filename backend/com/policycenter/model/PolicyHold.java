package com.policycenter.model;

/**
 * Guidewire PolicyCenter OOTB Entity: PolicyHold
 *
 * Represents a hold placed on a policy that prevents certain transactions.
 * Holds can be regulatory (DOI compliance), billing (non-payment), claims
 * (open litigation), or manual (UW review). A policy with an active hold
 * cannot be renewed, endorsed, or cancelled until the hold is released.
 */
public class PolicyHold {
    private String publicId;
    private String periodId;
    private String holdType;          // Regulatory, Billing, Claims, ManualUW, Fraud
    private String reason;
    private String status;            // Active, Released, Expired
    private String placedDate;
    private String releasedDate;
    private String placedByUserId;
    private String releasedByUserId;
    private boolean blocksRenewal;
    private boolean blocksEndorsement;
    private boolean blocksCancellation;
    private boolean blocksReinstatement;

    public PolicyHold() {}

    public PolicyHold(String publicId, String periodId, String holdType,
                      String reason, String status, String placedDate) {
        this.publicId = publicId;
        this.periodId = periodId;
        this.holdType = holdType;
        this.reason = reason;
        this.status = status;
        this.placedDate = placedDate;
    }

    // --- Getters & Setters ---
    public String getPublicId() { return publicId; }
    public void setPublicId(String publicId) { this.publicId = publicId; }
    public String getPeriodId() { return periodId; }
    public void setPeriodId(String periodId) { this.periodId = periodId; }
    public String getHoldType() { return holdType; }
    public void setHoldType(String holdType) { this.holdType = holdType; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPlacedDate() { return placedDate; }
    public void setPlacedDate(String placedDate) { this.placedDate = placedDate; }
    public String getReleasedDate() { return releasedDate; }
    public void setReleasedDate(String releasedDate) { this.releasedDate = releasedDate; }
    public String getPlacedByUserId() { return placedByUserId; }
    public void setPlacedByUserId(String placedByUserId) { this.placedByUserId = placedByUserId; }
    public String getReleasedByUserId() { return releasedByUserId; }
    public void setReleasedByUserId(String releasedByUserId) { this.releasedByUserId = releasedByUserId; }
    public boolean isBlocksRenewal() { return blocksRenewal; }
    public void setBlocksRenewal(boolean blocksRenewal) { this.blocksRenewal = blocksRenewal; }
    public boolean isBlocksEndorsement() { return blocksEndorsement; }
    public void setBlocksEndorsement(boolean blocksEndorsement) { this.blocksEndorsement = blocksEndorsement; }
    public boolean isBlocksCancellation() { return blocksCancellation; }
    public void setBlocksCancellation(boolean blocksCancellation) { this.blocksCancellation = blocksCancellation; }
    public boolean isBlocksReinstatement() { return blocksReinstatement; }
    public void setBlocksReinstatement(boolean blocksReinstatement) { this.blocksReinstatement = blocksReinstatement; }
}
