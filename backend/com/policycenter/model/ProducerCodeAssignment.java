package com.policycenter.model;

/**
 * Guidewire PolicyCenter OOTB Entity: ProducerCodeAssignment
 *
 * Links Users to ProducerCodes in a many-to-many relationship.
 * A single user can be assigned multiple producer codes (e.g., an agency
 * principal operating under multiple sub-codes), and a single producer code
 * can have multiple users assigned to it.
 */
public class ProducerCodeAssignment {
    private String publicId;
    private String userId;
    private String producerCodeId;
    private String assignmentRole;    // Primary, Secondary, Backup
    private String effectiveDate;
    private String expirationDate;
    private boolean active;

    public ProducerCodeAssignment() {}

    public ProducerCodeAssignment(String publicId, String userId, String producerCodeId,
                                   String assignmentRole, boolean active) {
        this.publicId = publicId;
        this.userId = userId;
        this.producerCodeId = producerCodeId;
        this.assignmentRole = assignmentRole;
        this.active = active;
    }

    // --- Getters & Setters ---
    public String getPublicId() { return publicId; }
    public void setPublicId(String publicId) { this.publicId = publicId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getProducerCodeId() { return producerCodeId; }
    public void setProducerCodeId(String producerCodeId) { this.producerCodeId = producerCodeId; }
    public String getAssignmentRole() { return assignmentRole; }
    public void setAssignmentRole(String assignmentRole) { this.assignmentRole = assignmentRole; }
    public String getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(String effectiveDate) { this.effectiveDate = effectiveDate; }
    public String getExpirationDate() { return expirationDate; }
    public void setExpirationDate(String expirationDate) { this.expirationDate = expirationDate; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
