package com.policycenter.controller;

import com.policycenter.gs.classes.job.PolicyChangeProcess;
import com.policycenter.model.Job;
import com.policycenter.model.PolicyPeriod;
import com.policycenter.repository.PolicyCenterSqliteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

/**
 * REST API Controller: Policy Change (Mid-Term Endorsement) Transaction Engine
 *
 * Exposes endpoints for initiating, quoting prorated delta premium, evaluating
 * underwriting authority issues, binding, and generating endorsement documents.
 */
@RestController
@RequestMapping("/api/policy-change")
@CrossOrigin(origins = "*")
public class PolicyChangeController {

    private final PolicyCenterSqliteRepository repository = PolicyCenterSqliteRepository.getInstance();

    /**
     * POST /api/policy-change/start
     * Initiates a Policy Change transaction for a bound/issued policy.
     */
    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startPolicyChange(@RequestBody Map<String, Object> payload) {
        String targetJobNum = payload.containsKey("jobNumber") ? payload.get("jobNumber").toString() : null;
        String policyNum = payload.containsKey("policyNumber") ? payload.get("policyNumber").toString() : null;

        Job existingJob = null;
        if (targetJobNum != null && !targetJobNum.isEmpty()) {
            existingJob = repository.getJob(targetJobNum);
        }

        if (existingJob == null && policyNum != null && !policyNum.isEmpty()) {
            List<Job> allJobs = repository.getAllJobs();
            for (Job j : allJobs) {
                if (j.getPolicyPeriod() != null && policyNum.equalsIgnoreCase(j.getPolicyPeriod().getPolicyNumber())) {
                    existingJob = j;
                    break;
                }
            }
        }

        if (existingJob == null) {
            // Fallback: create a fresh policy change job for demo account
            existingJob = repository.createSubmissionJob("C00010928", "CommercialProperty");
        }

        // Create a Policy Change child/transaction job
        String chgJobNum = "CHG-" + (10000 + (int)(Math.random() * 90000));
        PolicyPeriod periodCopy = existingJob.getPolicyPeriod();
        if (periodCopy == null) {
            periodCopy = repository.createSubmissionJob("C00010928", "CommercialProperty").getPolicyPeriod();
        }

        Job chgJob = new Job("job-" + chgJobNum, chgJobNum, "PolicyChange", periodCopy);
        chgJob.setJobStatus("Draft");
        chgJob.setCreateDate(LocalDate.now().toString());
        chgJob.setUnderwriterID("su");
        chgJob.setProducerCode("PROD-1001");

        PolicyChangeProcess process = new PolicyChangeProcess(chgJob);
        process.startPolicyChange();
        repository.saveJob(chgJob);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("jobNumber", chgJob.getJobNumber());
        response.put("jobType", "PolicyChange");
        response.put("status", "Draft");
        response.put("effectiveDate", payload.getOrDefault("effectiveDate", LocalDate.now().toString()));
        response.put("changeReason", payload.getOrDefault("changeReason", "Increase Building Coverage Limit"));
        response.put("policyNumber", periodCopy.getPolicyNumber() != null ? periodCopy.getPolicyNumber() : "CP-8472910");
        response.put("accountHolder", (periodCopy.getAccount() != null && periodCopy.getAccount().getAccountHolder() != null) ? periodCopy.getAccount().getAccountHolder().getName() : "Acme Enterprise");
        response.put("policyPeriod", periodCopy);

        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/policy-change/quote
     * Calculates prorated premium delta, updated building limits, tax delta, and UW referral issues.
     */
    @PostMapping("/quote")
    public ResponseEntity<Map<String, Object>> quotePolicyChange(@RequestBody Map<String, Object> payload) {
        String jobNumber = payload.containsKey("jobNumber") ? payload.get("jobNumber").toString() : "SUB-5001";
        Job job = repository.getJob(jobNumber);

        if (job == null) {
            job = repository.createSubmissionJob("C00010928", "CommercialProperty");
            job.setJobNumber(jobNumber);
        }

        PolicyChangeProcess process = new PolicyChangeProcess(job);
        Map<String, Object> quoteResult = process.calculateDetailedQuote(payload);
        repository.saveJob(job);

        return ResponseEntity.ok(quoteResult);
    }

    /**
     * POST /api/policy-change/bind
     * Binds and issues the Policy Change transaction, updating SQLite persistence and version history.
     */
    @PostMapping("/bind")
    public ResponseEntity<Map<String, Object>> bindPolicyChange(@RequestBody Map<String, Object> payload) {
        String jobNumber = payload.containsKey("jobNumber") ? payload.get("jobNumber").toString() : "SUB-5001";
        Job job = repository.getJob(jobNumber);

        if (job == null) {
            job = repository.createSubmissionJob("C00010928", "CommercialProperty");
            job.setJobNumber(jobNumber);
        }

        PolicyChangeProcess process = new PolicyChangeProcess(job);
        PolicyPeriod boundPeriod = process.bindPolicyChange();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("jobNumber", job.getJobNumber());
        result.put("policyNumber", boundPeriod.getPolicyNumber() != null ? boundPeriod.getPolicyNumber() : "CP-8472910");
        result.put("jobStatus", "Bound");
        result.put("periodStatus", "Bound");
        result.put("boundDate", LocalDate.now().toString());
        result.put("message", "Policy Change (Endorsement) successfully bound and issued into active Policy Ledger.");
        result.put("policyPeriod", boundPeriod);

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/policy-change/document/{jobNumber}
     * Returns an HTML ACORD / Policy Change Endorsement document for viewing and printing.
     */
    @GetMapping("/document/{jobNumber}")
    public ResponseEntity<String> getEndorsementDocument(@PathVariable String jobNumber) {
        Job job = repository.getJob(jobNumber);
        PolicyPeriod period = (job != null && job.getPolicyPeriod() != null) ? job.getPolicyPeriod() : null;

        String policyNum = period != null && period.getPolicyNumber() != null ? period.getPolicyNumber() : "CP-8472910";
        String insured = (period != null && period.getAccount() != null && period.getAccount().getAccountHolder() != null) ? period.getAccount().getAccountHolder().getName() : "Acme Logistics Inc.";
        String prodName = period != null && period.getProductName() != null ? period.getProductName() : "Commercial Property";
        double bldgLimit = (period != null && !period.getBuildings().isEmpty()) ? period.getBuildings().get(0).getBuildingLimit() : 1500000.0;
        double totalCost = period != null ? period.getTotalCost() : 2450.0;

        String html = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Policy Change Endorsement - %s</title>
                <style>
                    body { font-family: 'Segoe UI', Arial, sans-serif; margin: 40px; color: #1E293B; background: #F8FAFC; }
                    .container { background: #FFFFFF; border: 1px solid #CBD5E1; padding: 40px; border-radius: 8px; max-width: 800px; margin: 0 auto; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05); }
                    .header { border-bottom: 3px solid #0284C7; padding-bottom: 16px; margin-bottom: 24px; display: flex; justify-content: space-between; align-items: flex-start; }
                    .header h1 { margin: 0; color: #0369A1; font-size: 22px; text-transform: uppercase; letter-spacing: 0.5px; }
                    .header .sub { font-size: 12px; color: #64748B; margin-top: 4px; }
                    .meta-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; background: #F1F5F9; padding: 16px; border-radius: 6px; font-size: 13px; margin-bottom: 24px; }
                    .meta-grid div strong { color: #334155; }
                    table { width: 100%%; border-collapse: collapse; margin-top: 16px; font-size: 13px; }
                    th, td { border: 1px solid #E2E8F0; padding: 10px; text-align: left; }
                    th { background: #0284C7; color: #FFFFFF; font-weight: 600; }
                    .footer { margin-top: 32px; border-top: 1px solid #E2E8F0; padding-top: 16px; font-size: 11px; color: #94A3B8; text-align: center; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <div>
                            <h1>Guidewire PolicyCenter</h1>
                            <div class="sub">POLICY CHANGE ENDORSEMENT NOTICE (ACORD 25 COMPLIANT)</div>
                        </div>
                        <div style="text-align:right;">
                            <strong style="color:#0284C7;">JOB #: %s</strong><br>
                            <span style="font-size:12px; color:#64748B;">Date: %s</span>
                        </div>
                    </div>

                    <div class="meta-grid">
                        <div><strong>Named Insured:</strong> %s</div>
                        <div><strong>Policy Number:</strong> %s</div>
                        <div><strong>Product Line:</strong> %s</div>
                        <div><strong>Transaction Type:</strong> Mid-Term Policy Change</div>
                        <div><strong>Effective Date of Change:</strong> %s</div>
                        <div><strong>Status:</strong> BOUND & ISSUED</div>
                    </div>

                    <h3>Endorsement Schedule & Revised Coverage Limits</h3>
                    <table>
                        <thead>
                            <tr>
                                <th>Schedule Item / Coverable</th>
                                <th>Prior Limit ($)</th>
                                <th>Revised Endorsed Limit ($)</th>
                                <th>Net Change ($)</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>Commercial Building #1 (hq)</td>
                                <td>$1,000,000.00</td>
                                <td>$%s</td>
                                <td style="color:#059669; font-weight:700;">+ $500,000.00</td>
                            </tr>
                            <tr>
                                <td>Business Personal Property</td>
                                <td>$250,000.00</td>
                                <td>$250,000.00</td>
                                <td>$0.00</td>
                            </tr>
                        </tbody>
                    </table>

                    <div style="margin-top:24px; padding:16px; background:#ECFDF5; border:1px solid #A7F3D0; border-radius:6px;">
                        <h4 style="margin:0 0 6px 0; color:#065F46;">Prorated Premium Adjustment Summary</h4>
                        <p style="margin:0; font-size:13px; color:#047857;">
                            Original Term Premium: <strong>$2,400.00</strong> | Revised Annual Premium: <strong>$%s</strong><br>
                            Term Days Remaining: <strong>182 / 365 days</strong> (Proration Factor: 0.4986)<br>
                            <strong>Net Additional Premium Charged: +$350.00</strong> (Taxes & Fees: +$17.50)
                        </p>
                    </div>

                    <div class="footer">
                        Guidewire PolicyCenter Sandbox v1.0 • Authorized Policy Endorsement Document • Issued by Underwriting Authority
                    </div>
                </div>
            </body>
            </html>
        """.formatted(
            policyNum,
            job != null ? job.getJobNumber() : "CHG-5001",
            LocalDate.now().toString(),
            insured,
            policyNum,
            prodName,
            LocalDate.now().toString(),
            String.format("%,.2f", bldgLimit),
            String.format("%,.2f", totalCost)
        );

        return ResponseEntity.ok(html);
    }
}
