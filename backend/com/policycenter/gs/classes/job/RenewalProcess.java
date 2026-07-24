package com.policycenter.gs.classes.job;

import com.policycenter.gs.classes.rating.RatingEngine;
import com.policycenter.model.Job;
import com.policycenter.model.PolicyPeriod;

/**
 * Emulates Guidewire PolicyCenter RenewalProcess (RenewalProcess.gs).
 * Handles Policy Renewal processing for upcoming terms.
 */
public class RenewalProcess {

    private Job job;
    private PolicyPeriod period;

    public RenewalProcess(Job job) {
        this.job = job;
        this.period = job.getPolicyPeriod();
    }

    public void requestRenewalQuote() {
        period.setTermNumber(period.getTermNumber() + 1);
        period.setEffectiveDate(period.getExpirationDate());
        period.setExpirationDate(java.time.LocalDate.parse(period.getEffectiveDate()).plusYears(1).toString());

        RatingEngine.ratePolicyPeriod(period);
        period.setStatus("Quoted");
        job.setJobStatus("Quoted");
    }

    public void bindRenewal() {
        if (!"Quoted".equalsIgnoreCase(period.getStatus())) {
            requestRenewalQuote();
        }
        period.setStatus("Bound");
        job.setJobStatus("Bound");
        job.setCloseDate(java.time.LocalDate.now().toString());
    }

    public PolicyPeriod getPeriod() {
        return period;
    }
}
