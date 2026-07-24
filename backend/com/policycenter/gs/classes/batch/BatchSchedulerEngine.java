package com.policycenter.gs.classes.batch;

import com.policycenter.model.BatchJob;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Emulates Guidewire PolicyCenter WorkQueue Batch Processor (BatchProcessLV.gs).
 * Executes background batch jobs for Renewal Notices, Expiration Processing, and UW Escalation.
 */
public class BatchSchedulerEngine {

    private static final List<BatchJob> batchJobs = new ArrayList<>();

    static {
        batchJobs.add(new BatchJob("bj-100", "AutoPolicyChangeBatch", "Inactive", "2026-07-23 21:00:00", 0, 0));
        batchJobs.add(new BatchJob("bj-101", "RenewalNoticeBatch", "Completed", "2026-07-22 02:00:00", 14, 0));
        batchJobs.add(new BatchJob("bj-102", "PolicyExpirationBatch", "Completed", "2026-07-22 03:00:00", 3, 0));
        batchJobs.add(new BatchJob("bj-103", "UWEscalationBatch", "Completed", "2026-07-22 04:00:00", 2, 0));
    }

    public static List<BatchJob> getAllBatchJobs() {
        return new ArrayList<>(batchJobs);
    }

    public static BatchJob runBatchJob(String processType) {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        int processed = (int)(Math.random() * 15) + 1;

        for (BatchJob bj : batchJobs) {
            if (bj.getProcessType().equalsIgnoreCase(processType)) {
                bj.setStatus("Completed");
                bj.setLastRunTime(now);
                bj.setProcessedCount(bj.getProcessedCount() + processed);
                return bj;
            }
        }

        BatchJob newJob = new BatchJob("bj-" + System.currentTimeMillis(), processType, "Completed", now, processed, 0);
        batchJobs.add(newJob);
        return newJob;
    }
}
