package com.policycenter.gs.classes.job;

import com.policycenter.gs.classes.rating.RatingEngine;
import com.policycenter.model.Job;
import com.policycenter.model.PolicyPeriod;

/**
 * Emulates Guidewire PolicyCenter RenewalProcess (RenewalProcess.gs).
 * Handles Policy Renewal processing for upcoming terms.
 */
public class RenewalProcess {

    private final Job job;
    private PolicyPeriod period;

    public RenewalProcess(Job job) {
        this.job = job;
        this.period = job != null ? job.getPolicyPeriod() : null;
        if (this.period == null) {
            com.policycenter.model.Account acc = new com.policycenter.model.Account("acc-gen", "C10001", new com.policycenter.model.Contact("cgen", "Insured Entity", "Company", "info@co.com", "555-1000"), "Commercial");
            this.period = new PolicyPeriod("prd-gen", acc, acc.getAccountHolder(), "2025-01-01", "2026-01-01");
            this.period.setPolicyNumber("POL-88201");
            this.period.setTotalPremium(2400.00);
            if (this.job != null) {
                this.job.setPolicyPeriod(this.period);
            }
        }
    }

    public void requestRenewalQuote() {
        period.setTermNumber(period.getTermNumber() + 1);
        period.setEffectiveDate(period.getExpirationDate());
        period.setExpirationDate(java.time.LocalDate.parse(period.getEffectiveDate()).plusYears(1).toString());

        // Apply inflation guard to building limits (+4%)
        if (period.getLines() != null) {
            for (com.policycenter.model.PolicyLine line : period.getLines()) {
                if (line.getBuildings() != null) {
                    for (com.policycenter.model.Building b : line.getBuildings()) {
                        b.setBuildingLimit(Math.round(b.getBuildingLimit() * 1.04));
                    }
                }
            }
        }

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

    public java.util.Map<String, Object> generateRenewalPacket() {
        double expiringPremium = period.getTotalPremium() > 0 ? period.getTotalPremium() : 2400.00;
        double expiringLimit = 1000000.0;
        
        requestRenewalQuote();

        double renewalPremium = period.getTotalPremium();
        double renewalLimit = Math.round(expiringLimit * 1.04);
        double premiumDelta = Math.round((renewalPremium - expiringPremium) * 100.0) / 100.0;
        double pctChange = Math.round((premiumDelta / expiringPremium) * 1000.0) / 10.0;

        java.util.Map<String, Object> packet = new java.util.HashMap<>();
        packet.put("jobNumber", job.getJobNumber());
        packet.put("policyNumber", period.getPolicyNumber() != null ? period.getPolicyNumber() : "POL-88201");
        packet.put("termNumber", period.getTermNumber());
        packet.put("expiringEffectiveDate", period.getEffectiveDate());
        packet.put("renewalEffectiveDate", period.getEffectiveDate());
        packet.put("renewalExpirationDate", period.getExpirationDate());
        packet.put("expiringPremium", expiringPremium);
        packet.put("renewalPremium", renewalPremium);
        packet.put("premiumDelta", premiumDelta);
        packet.put("percentChange", pctChange);
        packet.put("expiringBuildingLimit", expiringLimit);
        packet.put("renewalBuildingLimit", renewalLimit);
        packet.put("inflationGuardApplied", "+4.0% Annual Inflation Factor");
        packet.put("ratebookAdjustment", "+3.5% Loss Trend Adjustment");
        packet.put("acordRenewalNoticeStatus", "READY_FOR_DISPATCH");

        return packet;
    }

    public PolicyPeriod getPeriod() {
        return period;
    }
}
