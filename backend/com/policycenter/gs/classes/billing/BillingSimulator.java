package com.policycenter.gs.classes.billing;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Emulates Guidewire BillingCenter Simulator (BillingSystemPlugin.gs / PaymentPlan.gs).
 * Calculates installment schedules, binder downpayments, and producer commissions.
 */
public class BillingSimulator {

    public static class Installment {
        public int installmentNum;
        public String dueDate;
        public double amount;
        public String description;

        public Installment(int installmentNum, String dueDate, double amount, String description) {
            this.installmentNum = installmentNum;
            this.dueDate = dueDate;
            this.amount = round(amount);
            this.description = description;
        }
    }

    public static List<Installment> generateInstallmentSchedule(double totalCost, String paymentPlan, String effectiveDate) {
        List<Installment> schedule = new ArrayList<>();
        LocalDate eff = (effectiveDate != null && !effectiveDate.isEmpty()) ? LocalDate.parse(effectiveDate) : LocalDate.now();

        if ("TwelvePay".equalsIgnoreCase(paymentPlan) || "Monthly".equalsIgnoreCase(paymentPlan)) {
            // 10% Downpayment at bind + 11 monthly installments
            double downpayment = totalCost * 0.10;
            double remaining = totalCost - downpayment;
            double monthly = remaining / 11.0;

            schedule.add(new Installment(1, eff.toString(), downpayment, "Binder Deposit / Downpayment (10%)"));
            for (int i = 1; i <= 11; i++) {
                schedule.add(new Installment(i + 1, eff.plusMonths(i).toString(), monthly, "Monthly Installment #" + i));
            }
        } else if ("FourPay".equalsIgnoreCase(paymentPlan) || "Quarterly".equalsIgnoreCase(paymentPlan)) {
            // 25% Downpayment at bind + 3 quarterly installments
            double quarterly = totalCost / 4.0;
            schedule.add(new Installment(1, eff.toString(), quarterly, "Binder Deposit / 1st Quarterly Payment (25%)"));
            schedule.add(new Installment(2, eff.plusMonths(3).toString(), quarterly, "2nd Quarterly Installment"));
            schedule.add(new Installment(3, eff.plusMonths(6).toString(), quarterly, "3rd Quarterly Installment"));
            schedule.add(new Installment(4, eff.plusMonths(9).toString(), quarterly, "4th Quarterly Installment"));
        } else {
            // Full Pay (100% due at inception)
            schedule.add(new Installment(1, eff.toString(), totalCost, "Full Pay Annual Premium (100%)"));
        }

        return schedule;
    }

    public static double calculateProducerCommission(double totalCost, double commissionRate) {
        double rate = commissionRate > 0 ? commissionRate : 0.15; // default 15% broker commission
        return round(totalCost * rate);
    }

    private static double round(double val) {
        return Math.round(val * 100.0) / 100.0;
    }
}
