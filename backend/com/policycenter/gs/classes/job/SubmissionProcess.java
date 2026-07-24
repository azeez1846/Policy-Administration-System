package com.policycenter.gs.classes.job;

import com.policycenter.gs.classes.rating.RatingEngine;
import com.policycenter.gs.classes.rules.UWRulesEngine;
import com.policycenter.gs.enhancements.PolicyPeriodEnhancement;
import com.policycenter.model.Job;
import com.policycenter.model.PolicyPeriod;

/**
 * Emulates Guidewire PolicyCenter SubmissionProcess (SubmissionProcess.gs).
 * Manages state machine transitions for Submissions with Underwriting Rule checks.
 */
public class SubmissionProcess {

    private final Job job;
    private final PolicyPeriod period;

    public SubmissionProcess(Job job) {
        this.job = job;
        this.period = job.getPolicyPeriod();
    }

    public void requestQuote() throws Exception {
        if (!PolicyPeriodEnhancement.isQuoteable(period)) {
            throw new IllegalStateException("Policy period failed validation level 'Quoteable'. Ensure building details and limits are populated.");
        }

        // Evaluate Underwriting Rules
        UWRulesEngine.evaluatePeriodRules(period);
        if (UWRulesEngine.hasBlockingIssues(period, "Quote")) {
            throw new IllegalStateException("Cannot Request Quote: Open Underwriting Issues exist that block Quote execution. Please approve issues in Step 4 (Risk Analysis).");
        }

        // Run rating engine
        RatingEngine.ratePolicyPeriod(period);
        period.setStatus("Quoted");
    }

    public void bindAndIssue() throws Exception {
        if (!"Quoted".equalsIgnoreCase(period.getStatus()) || period.getTotalCost() <= 0) {
            RatingEngine.ratePolicyPeriod(period);
            period.setStatus("Quoted");
        }

        if (UWRulesEngine.hasBlockingIssues(period, "Bind")) {
            throw new IllegalStateException("Cannot Bind Policy: Open Underwriting Issues exist that block Bind execution. Please approve issues in Step 4 (Risk Analysis).");
        }

        String policyNum = PolicyPeriodEnhancement.generatePolicyNumber(period);
        period.setPolicyNumber(policyNum);
        period.setStatus("Bound");
        job.setCloseDate(java.time.LocalDate.now().toString());

        // Add policy number to account
        if (period.getAccount() != null) {
            period.getAccount().addPolicyNumber(policyNum);
        }
    }

    public PolicyPeriod getPeriod() {
        return period;
    }
}
