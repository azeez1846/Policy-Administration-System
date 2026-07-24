package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_policyversion")
public class PolicyVersion {

    @Id
    @Column(name = "version_id")
    private String versionID;

    @Column(name = "policy_number")
    private String policyNumber;

    @Column(name = "sequence_number")
    private int sequenceNumber;

    @Column(name = "effective_date")
    private String effectiveDate;

    @Column(name = "job_type")
    private String jobType;

    @Column(name = "description")
    private String description;

    @Column(name = "is_oos")
    private boolean isOOS;

    public PolicyVersion() {}

    public PolicyVersion(String versionID, String policyNumber, int sequenceNumber, String effectiveDate, String jobType, String description, boolean isOOS) {
        this.versionID = versionID;
        this.policyNumber = policyNumber;
        this.sequenceNumber = sequenceNumber;
        this.effectiveDate = effectiveDate;
        this.jobType = jobType;
        this.description = description;
        this.isOOS = isOOS;
    }

    public String getVersionID() { return versionID; }
    public void setVersionID(String versionID) { this.versionID = versionID; }

    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }

    public int getSequenceNumber() { return sequenceNumber; }
    public void setSequenceNumber(int sequenceNumber) { this.sequenceNumber = sequenceNumber; }

    public String getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(String effectiveDate) { this.effectiveDate = effectiveDate; }

    public String getJobType() { return jobType; }
    public void setJobType(String jobType) { this.jobType = jobType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isOOS() { return isOOS; }
    public void setOOS(boolean OOS) { isOOS = OOS; }
}
