package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_uwissue")
public class UWIssue {

    @Id
    @Column(name = "public_id")
    private String publicID;

    @Column(name = "issue_key")
    private String issueKey;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "long_description")
    private String longDescription;

    @Column(name = "approval_blocking_level")
    private String approvalBlockingLevel;

    @Column(name = "status")
    private String status;

    public UWIssue() {
        this.status = "Open";
    }

    public UWIssue(String publicID, String issueKey, String shortDescription, String approvalBlockingLevel) {
        this.publicID = publicID;
        this.issueKey = issueKey;
        this.shortDescription = shortDescription;
        this.approvalBlockingLevel = approvalBlockingLevel;
        this.status = "Open";
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getIssueKey() { return issueKey; }
    public void setIssueKey(String issueKey) { this.issueKey = issueKey; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getLongDescription() { return longDescription; }
    public void setLongDescription(String longDescription) { this.longDescription = longDescription; }

    public String getApprovalBlockingLevel() { return approvalBlockingLevel; }
    public void setApprovalBlockingLevel(String approvalBlockingLevel) { this.approvalBlockingLevel = approvalBlockingLevel; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
