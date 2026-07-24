package com.policycenter.model;

/**
 * Guidewire PolicyCenter OOTB Entity: Activity
 *
 * Represents an underwriting task, approval workflow item, or system-generated
 * activity tied to a policy lifecycle event. Activities are the primary mechanism
 * for tracking work items in PolicyCenter's activity-based workflow engine.
 *
 * Key relationships:
 *   - Linked to a PolicyPeriod or Job via targetId
 *   - Assigned to a User (assigneeId)
 *   - Based on an ActivityPattern template
 */
public class Activity {
    private String publicId;
    private String subject;
    private String description;
    private String priority;         // Urgent, High, Normal, Low
    private String status;           // Open, Completed, Cancelled, Skipped
    private String activityClass;    // Task, Approval, Notification, Event
    private String activityPatternId;
    private String assigneeId;
    private String targetId;         // PolicyPeriod or Job publicId
    private String targetType;       // PolicyPeriod, Job, Account
    private String dueDate;
    private String completionDate;
    private String escalationDate;
    private boolean mandatory;
    private boolean recurring;

    public Activity() {}

    public Activity(String publicId, String subject, String priority, String status,
                    String activityClass, String assigneeId, String targetId, String dueDate) {
        this.publicId = publicId;
        this.subject = subject;
        this.priority = priority;
        this.status = status;
        this.activityClass = activityClass;
        this.assigneeId = assigneeId;
        this.targetId = targetId;
        this.dueDate = dueDate;
    }

    // --- Getters & Setters ---
    public String getPublicId() { return publicId; }
    public void setPublicId(String publicId) { this.publicId = publicId; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getActivityClass() { return activityClass; }
    public void setActivityClass(String activityClass) { this.activityClass = activityClass; }
    public String getActivityPatternId() { return activityPatternId; }
    public void setActivityPatternId(String activityPatternId) { this.activityPatternId = activityPatternId; }
    public String getAssigneeId() { return assigneeId; }
    public void setAssigneeId(String assigneeId) { this.assigneeId = assigneeId; }
    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }
    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }
    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public String getCompletionDate() { return completionDate; }
    public void setCompletionDate(String completionDate) { this.completionDate = completionDate; }
    public String getEscalationDate() { return escalationDate; }
    public void setEscalationDate(String escalationDate) { this.escalationDate = escalationDate; }
    public boolean isMandatory() { return mandatory; }
    public void setMandatory(boolean mandatory) { this.mandatory = mandatory; }
    public boolean isRecurring() { return recurring; }
    public void setRecurring(boolean recurring) { this.recurring = recurring; }
}
