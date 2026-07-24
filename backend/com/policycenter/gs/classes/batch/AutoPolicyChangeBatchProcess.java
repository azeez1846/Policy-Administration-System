package com.policycenter.gs.classes.batch;

import com.policycenter.gs.classes.job.PolicyChangeProcess;
import com.policycenter.model.*;
import com.policycenter.repository.PolicyCenterSqliteRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Emulates Guidewire PolicyCenter WorkQueue Batch Processor (AutoPolicyChangeBatchProcess.gs).
 *
 * Automatically scans all active Bound and Issued policies in the database,
 * initiates mid-term Policy Change transactions, applies coverage modifications,
 * rates prorated premium deltas, and issues Policy Change endorsements in bulk.
 */
public class AutoPolicyChangeBatchProcess {

    private static final PolicyCenterSqliteRepository repository = PolicyCenterSqliteRepository.getInstance();
    private static final List<Map<String, Object>> lastExecutionLogs = new ArrayList<>();
    private static final Map<String, Object> lastRunStats = new LinkedHashMap<>();

    public static synchronized Map<String, Object> executeBatchJob() {
        String startTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        List<Job> allJobs = repository.getAllJobs();

        List<Job> boundJobs = new ArrayList<>();
        for (Job j : allJobs) {
            if ("PolicyChange".equalsIgnoreCase(j.getJobType())) {
                continue; // Do not issue PolicyChange on top of another batch PolicyChange
            }
            String jobStatus = j.getJobStatus();
            String periodStatus = j.getPolicyPeriod() != null ? j.getPolicyPeriod().getStatus() : null;
            if ("Bound".equalsIgnoreCase(jobStatus) || "Issued".equalsIgnoreCase(jobStatus) ||
                "Bound".equalsIgnoreCase(periodStatus) || "Issued".equalsIgnoreCase(periodStatus)) {
                boundJobs.add(j);
            }
        }

        // If no bound primary jobs exist in DB, create sample bound policy
        if (boundJobs.isEmpty()) {
            Job j1 = repository.createSubmissionJob("C00010928", "CommercialProperty");
            j1.setJobStatus("Bound");
            if (j1.getPolicyPeriod() != null) j1.getPolicyPeriod().setStatus("Bound");
            repository.saveJob(j1);
            boundJobs.add(j1);
        }

        lastExecutionLogs.clear();
        int successCount = 0;
        int failedCount = 0;
        double totalProratedPremiumAdded = 0.0;

        for (Job targetJob : boundJobs) {
            try {
                PolicyPeriod period = targetJob.getPolicyPeriod();
                if (period == null) continue;

                // Create a child Policy Change Job
                String chgJobNum = "CHG-BATCH-" + (1000 + (int)(Math.random() * 9000));
                Job chgJob = new Job("job-" + chgJobNum, chgJobNum, "PolicyChange", period);
                chgJob.setJobStatus("Draft");
                chgJob.setCreateDate(LocalDate.now().toString());

                PolicyChangeProcess pcp = new PolicyChangeProcess(chgJob);
                pcp.startPolicyChange();

                // Apply automatic field modifications: Increase building limit by +$250,000
                double priorLimit = 1500000.0;
                if (!period.getBuildings().isEmpty()) {
                    priorLimit = period.getBuildings().get(0).getBuildingLimit();
                }
                double newLimit = priorLimit + 250000.0;

                Map<String, Object> quotePayload = new LinkedHashMap<>();
                quotePayload.put("jobNumber", chgJobNum);
                quotePayload.put("effectiveDate", LocalDate.now().toString());
                quotePayload.put("changeReason", "Automated Batch Inflation & Coverage Increase");
                quotePayload.put("buildingLimit", newLimit);
                quotePayload.put("constructionType", "Joisted Masonry");

                Map<String, Object> quoteRes = pcp.calculateDetailedQuote(quotePayload);
                double proratedDelta = (Double) quoteRes.getOrDefault("proratedDeltaPremium", 250.0);

                // Bind & Issue Policy Change
                pcp.bindPolicyChange();

                successCount++;
                totalProratedPremiumAdded += proratedDelta;

                Map<String, Object> logEntry = new LinkedHashMap<>();
                logEntry.put("parentJobNumber", targetJob.getJobNumber());
                logEntry.put("policyNumber", period.getPolicyNumber() != null ? period.getPolicyNumber() : "CP-8472910");
                logEntry.put("endorsementJobNumber", chgJobNum);
                logEntry.put("insuredName", (period.getAccount() != null && period.getAccount().getAccountHolder() != null) ? period.getAccount().getAccountHolder().getName() : "Acme Logistics Inc.");
                logEntry.put("priorBuildingLimit", priorLimit);
                logEntry.put("endorsedBuildingLimit", newLimit);
                logEntry.put("proratedDeltaPremium", proratedDelta);
                logEntry.put("status", "ISSUED & BOUND");
                logEntry.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

                lastExecutionLogs.add(logEntry);

            } catch (Exception e) {
                lastRunStats.put("lastError", e.getClass().getName() + ": " + e.getMessage());
                failedCount++;
            }
        }

        String endTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        lastRunStats.clear();
        lastRunStats.put("batchProcessType", "AutoPolicyChangeBatch");
        lastRunStats.put("status", "Completed");
        lastRunStats.put("startTime", startTime);
        lastRunStats.put("endTime", endTime);
        lastRunStats.put("policiesScanned", boundJobs.size());
        lastRunStats.put("endorsementsIssued", successCount);
        lastRunStats.put("failedCount", failedCount);
        lastRunStats.put("totalProratedPremiumWritten", Math.round(totalProratedPremiumAdded * 100.0) / 100.0);
        lastRunStats.put("executionLogs", lastExecutionLogs);

        // Update batch job entry in BatchSchedulerEngine
        BatchSchedulerEngine.runBatchJob("AutoPolicyChangeBatch");

        return lastRunStats;
    }

    public static Map<String, Object> getLastRunStats() {
        return lastRunStats;
    }

    public static List<Map<String, Object>> getLastExecutionLogs() {
        return lastExecutionLogs;
    }
}
