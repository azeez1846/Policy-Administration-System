package com.policycenter.controller;

import com.policycenter.model.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/financials")
@CrossOrigin(origins = "*")
public class FinancialsController {

    @GetMapping("/payment-plans")
    public List<PaymentPlan> getPaymentPlans() {
        // Return pre-configured payment plans
        List<PaymentPlan> plans = new ArrayList<>();
        plans.add(new PaymentPlan("plan-full", "Full Pay", "DirectBill", 1.0, 0.0, 1));
        plans.add(new PaymentPlan("plan-4pay", "Quarterly 4-Pay", "DirectBill", 0.25, 5.0, 4));
        plans.add(new PaymentPlan("plan-monthly", "Monthly Installments", "DirectBill", 0.10, 3.0, 12));
        return plans;
    }

    @GetMapping("/transactions")
    public List<Transaction> getTransactions() {
        List<Transaction> list = new ArrayList<>();
        list.add(new Transaction("tx-101", "period-5001", "cost-101", 2400.00, true, true, "2026-01-01", "2027-01-01", "2026-07-23"));
        return list;
    }
}
