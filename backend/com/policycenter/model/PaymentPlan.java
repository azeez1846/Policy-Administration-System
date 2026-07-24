package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_paymentplan")
public class PaymentPlan {

    @Id
    @Column(name = "public_id")
    private String publicID;

    @Column(name = "name")
    private String name;

    @Column(name = "billing_method")
    private String billingMethod;

    @Column(name = "down_payment_percent")
    private double downPaymentPercent;

    @Column(name = "installment_fee")
    private double installmentFee;

    @Column(name = "number_of_installments")
    private int numberOfInstallments;

    public PaymentPlan() {}

    public PaymentPlan(String publicID, String name, String billingMethod, double downPaymentPercent, double installmentFee, int numberOfInstallments) {
        this.publicID = publicID;
        this.name = name;
        this.billingMethod = billingMethod;
        this.downPaymentPercent = downPaymentPercent;
        this.installmentFee = installmentFee;
        this.numberOfInstallments = numberOfInstallments;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBillingMethod() { return billingMethod; }
    public void setBillingMethod(String billingMethod) { this.billingMethod = billingMethod; }

    public double getDownPaymentPercent() { return downPaymentPercent; }
    public void setDownPaymentPercent(double downPaymentPercent) { this.downPaymentPercent = downPaymentPercent; }

    public double getInstallmentFee() { return installmentFee; }
    public void setInstallmentFee(double installmentFee) { this.installmentFee = installmentFee; }

    public int getNumberOfInstallments() { return numberOfInstallments; }
    public void setNumberOfInstallments(int numberOfInstallments) { this.numberOfInstallments = numberOfInstallments; }
}
