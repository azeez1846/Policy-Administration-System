package com.policycenter.test;

import com.policycenter.gs.classes.batch.BatchSchedulerEngine;
import com.policycenter.model.BatchJob;

import java.util.List;

public class VerifyBatchScheduler {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println(" Guidewire PolicyCenter Batch Scheduler Test");
        System.out.println("=================================================");

        List<BatchJob> jobs = BatchSchedulerEngine.getAllBatchJobs();
        System.out.println("Fetched " + jobs.size() + " registered WorkQueue batch jobs:");
        for (BatchJob j : jobs) {
            System.out.println(" - " + j.getProcessType() + " | Status: " + j.getStatus() + " | Processed: " + j.getProcessedCount() + " | Last Run: " + j.getLastRunTime());
        }

        System.out.println("\nExecuting RenewalNoticeBatch process...");
        BatchJob runResult = BatchSchedulerEngine.runBatchJob("RenewalNoticeBatch");
        System.out.println("Batch Executed: " + runResult.getProcessType() + " | Total Processed: " + runResult.getProcessedCount());

        if ("Completed".equalsIgnoreCase(runResult.getStatus()) && runResult.getProcessedCount() > 14) {
            System.out.println("\nSUCCESS: WorkQueue Batch Process RenewalNoticeBatch executed cleanly!");
        } else {
            System.err.println("\nFAILURE: Batch process execution failed!");
            System.exit(1);
        }
    }
}
