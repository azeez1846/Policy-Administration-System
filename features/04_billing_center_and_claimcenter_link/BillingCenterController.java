package com.policycenter.controller;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/billing")
@CrossOrigin(origins = "*")
public class BillingCenterController {

    @GetMapping("/installment-schedule")
    public Map<String, Object> getInstallmentSchedule(@RequestParam(defaultValue = "12Pay") String plan, @RequestParam(defaultValue = "2400.0") double totalCost) {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> installments = new ArrayList<>();

        int numPayments = "12Pay".equalsIgnoreCase(plan) ? 12 : ("4Pay".equalsIgnoreCase(plan) ? 4 : 1);
        double installmentAmount = totalCost / numPayments;

        for (int i = 1; i <= numPayments; i++) {
            installments.add(Map.of(
                "installmentNum", i,
                "dueDate", "2026-" + String.format("%02d", ((i - 1) % 12) + 1) + "-15",
                "amount", Math.round(installmentAmount * 100.0) / 100.0,
                "status", i == 1 ? "Paid" : "Scheduled"
            ));
        }

        response.put("paymentPlan", plan);
        response.put("totalCost", totalCost);
        response.put("installments", installments);
        return response;
    }
}
