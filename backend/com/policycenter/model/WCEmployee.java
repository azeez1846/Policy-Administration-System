package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_wcemployee")
public class WCEmployee {

    @Id
    @Column(name = "public_id")
    private String publicID;

    @Column(name = "policy_period_id")
    private String policyPeriodID;

    @Column(name = "class_code")
    private String classCode;

    @Column(name = "state")
    private String state;

    @Column(name = "num_employees")
    private int numEmployees;

    @Column(name = "estimated_payroll")
    private double estimatedPayroll;

    public WCEmployee() {}

    public WCEmployee(String publicID, String policyPeriodID, String classCode, String state, int numEmployees, double estimatedPayroll) {
        this.publicID = publicID;
        this.policyPeriodID = policyPeriodID;
        this.classCode = classCode;
        this.state = state;
        this.numEmployees = numEmployees;
        this.estimatedPayroll = estimatedPayroll;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getPolicyPeriodID() { return policyPeriodID; }
    public void setPolicyPeriodID(String policyPeriodID) { this.policyPeriodID = policyPeriodID; }

    public String getClassCode() { return classCode; }
    public void setClassCode(String classCode) { this.classCode = classCode; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public int getNumEmployees() { return numEmployees; }
    public void setNumEmployees(int numEmployees) { this.numEmployees = numEmployees; }

    public double getEstimatedPayroll() { return estimatedPayroll; }
    public void setEstimatedPayroll(double estimatedPayroll) { this.estimatedPayroll = estimatedPayroll; }
}
