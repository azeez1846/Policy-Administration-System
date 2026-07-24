package com.policycenter.model;

/**
 * Guidewire PolicyCenter OOTB Entity: ActivityPattern
 *
 * Defines the template/blueprint for creating activities. Each pattern specifies
 * the activity type, default priority, auto-assignment rules, and which policy
 * lifecycle events trigger it. Used by the Gosu rules engine and automated
 * workflow to generate activities consistently.
 */
public class ActivityPattern {
    private String publicId;
    private String code;
    private String subject;
    private String description;
    private String activityClass;     // Task, Approval, Notification, Event
    private String priority;          // Urgent, High, Normal, Low
    private String category;          // Underwriting, Policy, Renewal, Cancellation, Audit
    private int dueDaysFromTarget;    // Days after trigger event
    private int escalationDays;       // Days after due date before escalation
    private boolean mandatory;
    private boolean autoAssign;
    private boolean recurring;
    private String triggerType;       // Pre-Quote, Pre-Bind, Post-Issue, Renewal, Cancellation

    public ActivityPattern() {}

    public ActivityPattern(String publicId, String code, String subject, String activityClass,
                           String priority, String category, int dueDaysFromTarget) {
        this.publicId = publicId;
        this.code = code;
        this.subject = subject;
        this.activityClass = activityClass;
        this.priority = priority;
        this.category = category;
        this.dueDaysFromTarget = dueDaysFromTarget;
    }

    // --- Getters & Setters ---
    public String getPublicId() { return publicId; }
    public void setPublicId(String publicId) { this.publicId = publicId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getActivityClass() { return activityClass; }
    public void setActivityClass(String activityClass) { this.activityClass = activityClass; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getDueDaysFromTarget() { return dueDaysFromTarget; }
    public void setDueDaysFromTarget(int dueDaysFromTarget) { this.dueDaysFromTarget = dueDaysFromTarget; }
    public int getEscalationDays() { return escalationDays; }
    public void setEscalationDays(int escalationDays) { this.escalationDays = escalationDays; }
    public boolean isMandatory() { return mandatory; }
    public void setMandatory(boolean mandatory) { this.mandatory = mandatory; }
    public boolean isAutoAssign() { return autoAssign; }
    public void setAutoAssign(boolean autoAssign) { this.autoAssign = autoAssign; }
    public boolean isRecurring() { return recurring; }
    public void setRecurring(boolean recurring) { this.recurring = recurring; }
    public String getTriggerType() { return triggerType; }
    public void setTriggerType(String triggerType) { this.triggerType = triggerType; }
}
