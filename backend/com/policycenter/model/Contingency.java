package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_contingency")
public class Contingency {

    @Id
    @Column(name = "public_id")
    private String publicID;

    @Column(name = "policy_period_id")
    private String policyPeriodID;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "due_date")
    private String dueDate;

    @Column(name = "status")
    private String status;

    @Column(name = "action")
    private String action;

    public Contingency() {}

    public Contingency(String publicID, String policyPeriodID, String title, String description, String dueDate, String status, String action) {
        this.publicID = publicID;
        this.policyPeriodID = policyPeriodID;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.action = action;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getPolicyPeriodID() { return policyPeriodID; }
    public void setPolicyPeriodID(String policyPeriodID) { this.policyPeriodID = policyPeriodID; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
}
