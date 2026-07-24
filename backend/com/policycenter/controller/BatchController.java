package com.policycenter.controller;

import com.policycenter.gs.classes.batch.AutoPolicyChangeBatchProcess;
import com.policycenter.gs.classes.batch.BatchSchedulerEngine;
import com.policycenter.model.BatchJob;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST API Controller: WorkQueue & Batch Process Control Studio
 *
 * Exposes endpoints to trigger PolicyCenter batch processes including the
 * automated Policy Change batch job for bound and issued policies.
 */
@RestController
@RequestMapping("/api/batch")
@CrossOrigin(origins = "*")
public class BatchController {

    @GetMapping("/jobs")
    public List<BatchJob> getAllBatchJobs() {
        return BatchSchedulerEngine.getAllBatchJobs();
    }

    @PostMapping("/run")
    public ResponseEntity<Object> runBatchJob(@RequestBody Map<String, String> payload) {
        String processType = payload.getOrDefault("processType", "AutoPolicyChangeBatch");
        if ("AutoPolicyChangeBatch".equalsIgnoreCase(processType)) {
            Map<String, Object> stats = AutoPolicyChangeBatchProcess.executeBatchJob();
            return ResponseEntity.ok(stats);
        }
        BatchJob job = BatchSchedulerEngine.runBatchJob(processType);
        return ResponseEntity.ok(job);
    }

    @PostMapping("/auto-policy-change/run")
    public ResponseEntity<Map<String, Object>> runAutoPolicyChangeBatch() {
        Map<String, Object> stats = executeAutoPolicyChangeBatch();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/auto-policy-change/logs")
    public ResponseEntity<Map<String, Object>> getAutoPolicyChangeLogs() {
        return ResponseEntity.ok(getAutoPolicyChangeLogsData());
    }

    public Map<String, Object> executeAutoPolicyChangeBatch() {
        return AutoPolicyChangeBatchProcess.executeBatchJob();
    }

    public Map<String, Object> getAutoPolicyChangeLogsData() {
        return AutoPolicyChangeBatchProcess.getLastRunStats();
    }
}
