package com.policycenter.controller;

import com.policycenter.gs.classes.billing.BillingSimulator;
import com.policycenter.model.Job;
import com.policycenter.repository.PolicyCenterSqliteRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/billing")
@CrossOrigin(origins = "*")
public class BillingController {

    private final PolicyCenterSqliteRepository repository = PolicyCenterSqliteRepository.getInstance();

    @PostMapping("/installments")
    public Map<String, Object> getBillingInstallments(@RequestBody Map<String, String> payload) {
        String jobNumber = payload.get("jobNumber");
        String plan = payload.getOrDefault("plan", "TwelvePay");
        Job job = repository.getJob(jobNumber);

        if (job != null && job.getPolicyPeriod() != null) {
            double totalCost = job.getPolicyPeriod().getTotalCost();
            List<BillingSimulator.Installment> insts = BillingSimulator.generateInstallmentSchedule(totalCost, plan, job.getPolicyPeriod().getEffectiveDate());
            double comm = BillingSimulator.calculateProducerCommission(totalCost, 0.15);

            Map<String, Object> res = new HashMap<>();
            res.put("totalCost", totalCost);
            res.put("commission", comm);
            res.put("plan", plan);
            res.put("installments", insts);
            return res;
        }
        throw new IllegalArgumentException("Job not found: " + jobNumber);
    }
}
