package com.policycenter.controller;

import com.policycenter.gs.classes.rules.UWRulesEngine;
import com.policycenter.model.Job;
import com.policycenter.repository.PolicyCenterSqliteRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/uw")
@CrossOrigin(origins = "*")
public class UWController {

    private final PolicyCenterSqliteRepository repository = PolicyCenterSqliteRepository.getInstance();

    @PostMapping("/evaluate")
    public Job evaluateUWRules(@RequestBody Map<String, String> payload) {
        String jobNumber = payload.get("jobNumber");
        Job job = repository.getJob(jobNumber);
        if (job != null && job.getPolicyPeriod() != null) {
            UWRulesEngine.evaluatePeriodRules(job.getPolicyPeriod());
            repository.saveJob(job);
            return job;
        }
        throw new IllegalArgumentException("Job not found: " + jobNumber);
    }

    @PostMapping("/approve-issue")
    public Job approveUWIssue(@RequestBody Map<String, String> payload) {
        String jobNumber = payload.get("jobNumber");
        String issueKey = payload.get("issueKey");
        Job job = repository.getJob(jobNumber);
        if (job != null && job.getPolicyPeriod() != null) {
            UWRulesEngine.approveIssue(job.getPolicyPeriod(), issueKey, "su");
            repository.saveJob(job);
            return job;
        }
        throw new IllegalArgumentException("Job or Issue not found: " + jobNumber);
    }
}
