package com.policycenter.gs.classes.job;

import com.policycenter.model.Cost;
import com.policycenter.model.Job;
import com.policycenter.model.PolicyPeriod;

/**
 * Emulates Guidewire PolicyCenter CancellationProcess (CancellationProcess.gs).
 * Handles Policy Cancellations (Pro-Rata, Flat, Short-Rate).
 */
public class CancellationProcess {

    private Job job;
    private PolicyPeriod period;

    public CancellationProcess(Job job) {
        this.job = job;
        this.period = job.getPolicyPeriod();
    }

    public double cancelPolicy(String cancellationType, String cancelDate, String reason) {
        period.setStatus("Cancelled");
        period.setCancellationDate(cancelDate != null ? cancelDate : java.time.LocalDate.now().toString());
        job.setJobStatus("Closed");
        job.setCloseDate(java.time.LocalDate.now().toString());

        double originalCost = period.getTotalCost();
        double refundAmount = 0.0;

        if ("Flat".equalsIgnoreCase(cancellationType)) {
            refundAmount = originalCost; // 100% refund
        } else if ("ShortRate".equalsIgnoreCase(cancellationType)) {
            refundAmount = originalCost * 0.40; // 40% refund after short-rate penalty
        } else {
            // Pro-Rata (default)
            refundAmount = originalCost * 0.50; // 50% pro-rata refund
        }

        Cost refundCost = new Cost("cost-cnc-" + System.currentTimeMillis(), "Tax", "Cancellation Refund (" + cancellationType + " - " + reason + ")", -refundAmount);
        period.getCosts().add(refundCost);
        period.setTotalCost(originalCost - refundAmount);

        return refundAmount;
    }

    public PolicyPeriod getPeriod() {
        return period;
    }
}
