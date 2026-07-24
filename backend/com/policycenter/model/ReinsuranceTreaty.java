package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_reinsurancetreaty")
public class ReinsuranceTreaty {

    @Id
    @Column(name = "treaty_id")
    private String treatyID;

    @Column(name = "treaty_name")
    private String treatyName;

    @Column(name = "treaty_type")
    private String treatyType;

    @Column(name = "ceded_percentage")
    private double cededPercentage;

    @Column(name = "attachment_point")
    private double attachmentPoint;

    @Column(name = "reinsurer_name")
    private String reinsurerName;

    public ReinsuranceTreaty() {}

    public ReinsuranceTreaty(String treatyID, String treatyName, String treatyType, double cededPercentage, double attachmentPoint, String reinsurerName) {
        this.treatyID = treatyID;
        this.treatyName = treatyName;
        this.treatyType = treatyType;
        this.cededPercentage = cededPercentage;
        this.attachmentPoint = attachmentPoint;
        this.reinsurerName = reinsurerName;
    }

    public String getTreatyID() { return treatyID; }
    public void setTreatyID(String treatyID) { this.treatyID = treatyID; }

    public String getTreatyName() { return treatyName; }
    public void setTreatyName(String treatyName) { this.treatyName = treatyName; }

    public String getTreatyType() { return treatyType; }
    public void setTreatyType(String treatyType) { this.treatyType = treatyType; }

    public double getCededPercentage() { return cededPercentage; }
    public void setCededPercentage(double cededPercentage) { this.cededPercentage = cededPercentage; }

    public double getAttachmentPoint() { return attachmentPoint; }
    public void setAttachmentPoint(double attachmentPoint) { this.attachmentPoint = attachmentPoint; }

    public String getReinsurerName() { return reinsurerName; }
    public void setReinsurerName(String reinsurerName) { this.reinsurerName = reinsurerName; }
}
