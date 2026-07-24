package com.policycenter.gs.classes.job;

import com.policycenter.model.Job;
import com.policycenter.model.PolicyPeriod;

/**
 * Emulates Guidewire PolicyCenter ReinstatementProcess (ReinstatementProcess.gs).
 * Handles Policy Reinstatement to restore cancelled policies.
 */
public class ReinstatementProcess {

    private Job job;
    private PolicyPeriod period;

    public ReinstatementProcess(Job job) {
        this.job = job;
        this.period = job.getPolicyPeriod();
    }

    public void reinstatePolicy(String reason) {
        if (!"Cancelled".equalsIgnoreCase(period.getStatus())) {
            throw new IllegalStateException("Only cancelled policies can be reinstated.");
        }
        period.setStatus("Bound");
        period.setCancellationDate(null);
        job.setJobStatus("Bound");
        job.setCloseDate(java.time.LocalDate.now().toString());
    }

    public PolicyPeriod getPeriod() {
        return period;
    }
}
