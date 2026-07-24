package com.policycenter.test;

import com.policycenter.gs.classes.billing.BillingSimulator;

import java.util.List;

public class VerifyBillingSimulator {
    public static void main(String[] args) {
        System.out.println("Testing Phase 5 BillingCenter Simulator & Payment Plans...");

        double totalCost = 2400.00;

        // 1. Full Pay Schedule
        List<BillingSimulator.Installment> fullPay = BillingSimulator.generateInstallmentSchedule(totalCost, "FullPay", "2026-07-23");
        System.out.println("1. Full Pay Plan -> Installments Count: " + fullPay.size() + ", Inception Amount: $" + fullPay.get(0).amount);

        // 2. 12-Pay Monthly Schedule
        List<BillingSimulator.Installment> twelvePay = BillingSimulator.generateInstallmentSchedule(totalCost, "TwelvePay", "2026-07-23");
        System.out.println("2. 12-Pay Monthly Plan -> Installments Count: " + twelvePay.size() + ", Downpayment: $" + twelvePay.get(0).amount + ", Monthly: $" + twelvePay.get(1).amount);

        // 3. Producer Commission
        double comm = BillingSimulator.calculateProducerCommission(totalCost, 0.15);
        System.out.println("3. Producer Commission (15%) -> $" + comm);

        if (fullPay.size() == 1 && twelvePay.size() == 12 && comm == 360.00) {
            System.out.println("SUCCESS: Phase 5 BillingCenter Simulator & Payment Plans Verified Clean!");
        } else {
            System.err.println("FAILURE: Billing simulator calculations failed validation.");
            System.exit(1);
        }
    }
}
