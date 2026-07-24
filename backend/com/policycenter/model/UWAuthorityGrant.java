package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_uwauthoritygrant")
public class UWAuthorityGrant {

    @Id
    @Column(name = "public_id")
    private String publicID;

    @Column(name = "profile_id")
    private String profileID;

    @Column(name = "issue_type")
    private String issueType;

    @Column(name = "value_operand")
    private String valueOperand;

    @Column(name = "reference_value")
    private String referenceValue;

    @Column(name = "approved")
    private boolean approved;

    public UWAuthorityGrant() {}

    public UWAuthorityGrant(String publicID, String profileID, String issueType, String valueOperand, String referenceValue, boolean approved) {
        this.publicID = publicID;
        this.profileID = profileID;
        this.issueType = issueType;
        this.valueOperand = valueOperand;
        this.referenceValue = referenceValue;
        this.approved = approved;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getProfileID() { return profileID; }
    public void setProfileID(String profileID) { this.profileID = profileID; }

    public String getIssueType() { return issueType; }
    public void setIssueType(String issueType) { this.issueType = issueType; }

    public String getValueOperand() { return valueOperand; }
    public void setValueOperand(String valueOperand) { this.valueOperand = valueOperand; }

    public String getReferenceValue() { return referenceValue; }
    public void setReferenceValue(String referenceValue) { this.referenceValue = referenceValue; }

    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
}
