package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_auditinformation")
public class AuditInformation {

    @Id
    @Column(name = "public_id")
    private String publicID;

    @Column(name = "policy_period_id")
    private String policyPeriodID;

    @Column(name = "audit_type")
    private String auditType;

    @Column(name = "audit_schedule_type")
    private String auditScheduleType;

    @Column(name = "due_date")
    private String dueDate;

    @Column(name = "actual_audit_method")
    private String actualAuditMethod;

    @Column(name = "status")
    private String status;

    public AuditInformation() {}

    public AuditInformation(String publicID, String policyPeriodID, String auditType, String auditScheduleType, String dueDate, String actualAuditMethod, String status) {
        this.publicID = publicID;
        this.policyPeriodID = policyPeriodID;
        this.auditType = auditType;
        this.auditScheduleType = auditScheduleType;
        this.dueDate = dueDate;
        this.actualAuditMethod = actualAuditMethod;
        this.status = status;
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getPolicyPeriodID() { return policyPeriodID; }
    public void setPolicyPeriodID(String policyPeriodID) { this.policyPeriodID = policyPeriodID; }

    public String getAuditType() { return auditType; }
    public void setAuditType(String auditType) { this.auditType = auditType; }

    public String getAuditScheduleType() { return auditScheduleType; }
    public void setAuditScheduleType(String auditScheduleType) { this.auditScheduleType = auditScheduleType; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public String getActualAuditMethod() { return actualAuditMethod; }
    public void setActualAuditMethod(String actualAuditMethod) { this.actualAuditMethod = actualAuditMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
