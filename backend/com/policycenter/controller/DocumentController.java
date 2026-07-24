package com.policycenter.controller;

import com.policycenter.gs.classes.forms.DocumentGenerator;
import com.policycenter.gs.classes.forms.FormsInferenceEngine;
import com.policycenter.model.Job;
import com.policycenter.model.PolicyPeriod;
import com.policycenter.repository.PolicyCenterSqliteRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "*")
public class DocumentController {

    private final PolicyCenterSqliteRepository repository = PolicyCenterSqliteRepository.getInstance();

    @GetMapping(value = "/dec-page", produces = MediaType.TEXT_HTML_VALUE)
    public String getPolicyDecPage(@RequestParam(name = "job", defaultValue = "SUB-5001") String jobNumber) {
        Job job = repository.getJob(jobNumber);
        PolicyPeriod period = job != null ? job.getPolicyPeriod() : null;
        if (period != null) {
            FormsInferenceEngine.inferPolicyForms(period);
        }
        return DocumentGenerator.generatePolicyDecPageHtml(period);
    }
}
