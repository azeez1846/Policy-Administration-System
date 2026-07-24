package com.policycenter.controller;

import com.policycenter.model.Job;
import com.policycenter.repository.PolicyCenterSqliteRepository;
import com.policycenter.service.AIRiskAssessmentService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AIUWController {

    private final PolicyCenterSqliteRepository repository = PolicyCenterSqliteRepository.getInstance();

    @PostMapping("/assess-risk")
    public Map<String, Object> assessRisk(@RequestBody Map<String, String> payload) {
        String jobNumber = payload.get("jobNumber");
        Job job = repository.getJob(jobNumber);
        if (job != null && job.getPolicyPeriod() != null) {
            return AIRiskAssessmentService.assessPolicyPeriodRisk(job.getPolicyPeriod());
        }
        return AIRiskAssessmentService.assessPolicyPeriodRisk(null);
    }

    @PostMapping("/ocr-parse")
    public Map<String, Object> parseACORDDocument(@RequestBody Map<String, String> payload) {
        String rawText = payload.getOrDefault("rawText", "");
        return AIRiskAssessmentService.parseACORDDocument(rawText);
    }
}
