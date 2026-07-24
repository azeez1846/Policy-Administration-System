package com.policycenter.model;

/**
 * Guidewire PolicyCenter OOTB Entity: AuditSchedule
 *
 * Defines premium audit scheduling for auditable policies (Workers' Comp,
 * General Liability). Tracks audit type (annual, final, interim), scheduling
 * dates, assigned auditor, and completion status.
 */
public class AuditSchedule {
    private String publicId;
    private String periodId;
    private String auditType;         // Annual, Final, Interim, Voluntary
    private String auditMethod;       // Physical, Phone, Mail, SelfAudit
    private String scheduledDate;
    private String completionDate;
    private String status;            // Scheduled, InProgress, Completed, Waived
    private String auditorName;
    private String auditorCompany;
    private double estimatedPremium;
    private double auditedPremium;
    private double premiumAdjustment; // Difference
    private String notes;

    public AuditSchedule() {}

    public AuditSchedule(String publicId, String periodId, String auditType,
                         String auditMethod, String scheduledDate, String status) {
        this.publicId = publicId;
        this.periodId = periodId;
        this.auditType = auditType;
        this.auditMethod = auditMethod;
        this.scheduledDate = scheduledDate;
        this.status = status;
    }

    // --- Getters & Setters ---
    public String getPublicId() { return publicId; }
    public void setPublicId(String publicId) { this.publicId = publicId; }
    public String getPeriodId() { return periodId; }
    public void setPeriodId(String periodId) { this.periodId = periodId; }
    public String getAuditType() { return auditType; }
    public void setAuditType(String auditType) { this.auditType = auditType; }
    public String getAuditMethod() { return auditMethod; }
    public void setAuditMethod(String auditMethod) { this.auditMethod = auditMethod; }
    public String getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(String scheduledDate) { this.scheduledDate = scheduledDate; }
    public String getCompletionDate() { return completionDate; }
    public void setCompletionDate(String completionDate) { this.completionDate = completionDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAuditorName() { return auditorName; }
    public void setAuditorName(String auditorName) { this.auditorName = auditorName; }
    public String getAuditorCompany() { return auditorCompany; }
    public void setAuditorCompany(String auditorCompany) { this.auditorCompany = auditorCompany; }
    public double getEstimatedPremium() { return estimatedPremium; }
    public void setEstimatedPremium(double estimatedPremium) { this.estimatedPremium = estimatedPremium; }
    public double getAuditedPremium() { return auditedPremium; }
    public void setAuditedPremium(double auditedPremium) { this.auditedPremium = auditedPremium; }
    public double getPremiumAdjustment() { return premiumAdjustment; }
    public void setPremiumAdjustment(double premiumAdjustment) { this.premiumAdjustment = premiumAdjustment; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
