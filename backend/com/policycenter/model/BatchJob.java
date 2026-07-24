package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_batchjob")
public class BatchJob {

    @Id
    @Column(name = "job_id")
    private String jobID;

    @Column(name = "process_type")
    private String processType;

    @Column(name = "status")
    private String status;

    @Column(name = "last_run_time")
    private String lastRunTime;

    @Column(name = "processed_count")
    private int processedCount;

    @Column(name = "failed_count")
    private int failedCount;

    public BatchJob() {}

    public BatchJob(String jobID, String processType, String status, String lastRunTime, int processedCount, int failedCount) {
        this.jobID = jobID;
        this.processType = processType;
        this.status = status;
        this.lastRunTime = lastRunTime;
        this.processedCount = processedCount;
        this.failedCount = failedCount;
    }

    public String getJobID() { return jobID; }
    public void setJobID(String jobID) { this.jobID = jobID; }

    public String getProcessType() { return processType; }
    public void setProcessType(String processType) { this.processType = processType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLastRunTime() { return lastRunTime; }
    public void setLastRunTime(String lastRunTime) { this.lastRunTime = lastRunTime; }

    public int getProcessedCount() { return processedCount; }
    public void setProcessedCount(int processedCount) { this.processedCount = processedCount; }

    public int getFailedCount() { return failedCount; }
    public void setFailedCount(int failedCount) { this.failedCount = failedCount; }
}
