package com.policycenter.controller;

import com.policycenter.gs.classes.job.*;
import com.policycenter.model.Job;
import com.policycenter.repository.PolicyCenterSqliteRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
public class JobController {

    private final PolicyCenterSqliteRepository repository = PolicyCenterSqliteRepository.getInstance();

    @GetMapping
    public List<Job> getAllJobs() {
        return repository.getAllJobs();
    }

    @GetMapping("/{jobNumber}")
    public Job getJob(@PathVariable String jobNumber) {
        return repository.getJob(jobNumber);
    }

    @PostMapping
    public Job createSubmission(@RequestBody Map<String, String> payload) {
        String accountNumber = payload.get("accountNumber");
        String prodCode = payload.getOrDefault("productCode", "CommercialProperty");
        Job job = repository.createSubmissionJob(accountNumber, prodCode);
        return job;
    }

    @PostMapping("/quote")
    public Job requestQuote(@RequestBody Map<String, String> payload) throws Exception {
        String jobNumber = payload.get("jobNumber");
        Job job = repository.getJob(jobNumber);
        if (job != null) {
            SubmissionProcess process = new SubmissionProcess(job);
            process.requestQuote();
            repository.saveJob(job);
            return job;
        }
        throw new IllegalArgumentException("Job not found: " + jobNumber);
    }

    @PostMapping("/bind")
    public Job bindAndIssue(@RequestBody Map<String, String> payload) throws Exception {
        String jobNumber = payload.get("jobNumber");
        Job job = repository.getJob(jobNumber);
        if (job != null) {
            SubmissionProcess process = new SubmissionProcess(job);
            process.bindAndIssue();
            repository.saveJob(job);
            return job;
        }
        throw new IllegalArgumentException("Job not found: " + jobNumber);
    }

    @PostMapping("/endorse")
    public Job startPolicyChange(@RequestBody Map<String, String> payload) {
        String jobNumber = payload.get("jobNumber");
        Job job = repository.getJob(jobNumber);
        if (job != null) {
            PolicyChangeProcess process = new PolicyChangeProcess(job);
            process.startPolicyChange();
            repository.saveJob(job);
            return job;
        }
        throw new IllegalArgumentException("Job not found: " + jobNumber);
    }

    @PostMapping("/renew")
    public Job executeRenewal(@RequestBody Map<String, String> payload) {
        String jobNumber = payload.get("jobNumber");
        Job job = repository.getJob(jobNumber);
        if (job != null) {
            RenewalProcess process = new RenewalProcess(job);
            process.bindRenewal();
            repository.saveJob(job);
            return job;
        }
        throw new IllegalArgumentException("Job not found: " + jobNumber);
    }

    @PostMapping("/cancel")
    public Job cancelPolicy(@RequestBody Map<String, String> payload) {
        String jobNumber = payload.get("jobNumber");
        String cancelType = payload.getOrDefault("cancelType", "ProRata");
        String reason = payload.getOrDefault("reason", "Customer Request");
        Job job = repository.getJob(jobNumber);
        if (job != null) {
            CancellationProcess process = new CancellationProcess(job);
            process.cancelPolicy(cancelType, null, reason);
            repository.saveJob(job);
            return job;
        }
        throw new IllegalArgumentException("Job not found: " + jobNumber);
    }

    @PostMapping("/reinstate")
    public Job reinstatePolicy(@RequestBody Map<String, String> payload) {
        String jobNumber = payload.get("jobNumber");
        String reason = payload.getOrDefault("reason", "Underwriting Approval");
        Job job = repository.getJob(jobNumber);
        if (job != null) {
            ReinstatementProcess process = new ReinstatementProcess(job);
            process.reinstatePolicy(reason);
            repository.saveJob(job);
            return job;
        }
        throw new IllegalArgumentException("Job not found: " + jobNumber);
    }
}
