package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_job")
public class Job {

    @Id
    @Column(name = "job_number")
    private String jobNumber;

    @Column(name = "job_type")
    private String jobType;

    @Column(name = "job_status")
    private String jobStatus;

    @Column(name = "create_date")
    private String createDate;

    @Column(name = "close_date")
    private String closeDate;

    @Column(name = "underwriter_id")
    private String underwriterID;

    @Column(name = "producer_code")
    private String producerCode;

    @Transient
    private PolicyPeriod policyPeriod;

    public Job() {}

    public Job(String jobNumber, String jobType, PolicyPeriod policyPeriod) {
        this.jobNumber = jobNumber;
        this.jobType = jobType;
        this.jobStatus = "Draft";
        this.createDate = java.time.LocalDate.now().toString();
        this.policyPeriod = policyPeriod;
        this.producerCode = "301-009281";
    }

    public Job(String publicID, String jobNumber, String jobType, PolicyPeriod policyPeriod) {
        this.jobNumber = jobNumber;
        this.jobType = jobType;
        this.jobStatus = "Draft";
        this.createDate = java.time.LocalDate.now().toString();
        this.policyPeriod = policyPeriod;
        this.producerCode = "301-009281";
    }

    public String getJobNumber() { return jobNumber; }
    public void setJobNumber(String jobNumber) { this.jobNumber = jobNumber; }

    public String getPublicID() { return jobNumber; }
    public void setPublicID(String publicID) { this.jobNumber = publicID; }

    public String getJobType() { return jobType; }
    public void setJobType(String jobType) { this.jobType = jobType; }

    public String getJobStatus() { return jobStatus; }
    public void setJobStatus(String jobStatus) { this.jobStatus = jobStatus; }

    public String getCreateDate() { return createDate; }
    public void setCreateDate(String createDate) { this.createDate = createDate; }

    public String getCloseDate() { return closeDate; }
    public void setCloseDate(String closeDate) { this.closeDate = closeDate; }

    public String getUnderwriterID() { return underwriterID; }
    public void setUnderwriterID(String underwriterID) { this.underwriterID = underwriterID; }

    public String getProducerCode() { return producerCode; }
    public void setProducerCode(String producerCode) { this.producerCode = producerCode; }

    public PolicyPeriod getPolicyPeriod() { return policyPeriod; }
    public void setPolicyPeriod(PolicyPeriod policyPeriod) { this.policyPeriod = policyPeriod; }
}
