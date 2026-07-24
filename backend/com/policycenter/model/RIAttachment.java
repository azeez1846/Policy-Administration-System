package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_riattachment")
public class RIAttachment {

    @Id
    @Column(name = "public_id")
    private String publicID;

    @Column(name = "risk_id")
    private String riskID;

    @Column(name = "treaty_id")
    private String treatyID;

    @Column(name = "attachment_type")
    private String attachmentType;

    @Column(name = "ceded_share")
    private double cededShare;

    public RIAttachment() {}

    public RIAttachment(String publicID, String riskID, String treatyID, String attachmentType, double cededShare) {
        this.publicID = publicID;
        this.riskID = riskID;
        this.treatyID = treatyID;
        this.attachmentType = attachmentType;
        this.cededShare = cededShare;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getRiskID() { return riskID; }
    public void setRiskID(String riskID) { this.riskID = riskID; }

    public String getTreatyID() { return treatyID; }
    public void setTreatyID(String treatyID) { this.treatyID = treatyID; }

    public String getAttachmentType() { return attachmentType; }
    public void setAttachmentType(String attachmentType) { this.attachmentType = attachmentType; }

    public double getCededShare() { return cededShare; }
    public void setCededShare(double cededShare) { this.cededShare = cededShare; }
}
