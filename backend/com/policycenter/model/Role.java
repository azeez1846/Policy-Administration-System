package com.policycenter.model;

/**
 * Guidewire PolicyCenter OOTB Entity: Role
 *
 * Defines security roles that control access to screens, actions, and authority
 * limits. Each User is assigned one or more Roles. Roles are used by the
 * permission system to determine what a user can view, edit, approve, or bind.
 */
public class Role {
    private String publicId;
    private String roleName;
    private String description;
    private String roleType;          // System, Custom
    private String permissions;       // Comma-separated permission keys
    private boolean canApproveUWIssues;
    private boolean canBindPolicies;
    private boolean canCancelPolicies;
    private boolean canViewFinancials;

    public Role() {}

    public Role(String publicId, String roleName, String description, String roleType) {
        this.publicId = publicId;
        this.roleName = roleName;
        this.description = description;
        this.roleType = roleType;
    }

    // --- Getters & Setters ---
    public String getPublicId() { return publicId; }
    public void setPublicId(String publicId) { this.publicId = publicId; }
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRoleType() { return roleType; }
    public void setRoleType(String roleType) { this.roleType = roleType; }
    public String getPermissions() { return permissions; }
    public void setPermissions(String permissions) { this.permissions = permissions; }
    public boolean isCanApproveUWIssues() { return canApproveUWIssues; }
    public void setCanApproveUWIssues(boolean canApproveUWIssues) { this.canApproveUWIssues = canApproveUWIssues; }
    public boolean isCanBindPolicies() { return canBindPolicies; }
    public void setCanBindPolicies(boolean canBindPolicies) { this.canBindPolicies = canBindPolicies; }
    public boolean isCanCancelPolicies() { return canCancelPolicies; }
    public void setCanCancelPolicies(boolean canCancelPolicies) { this.canCancelPolicies = canCancelPolicies; }
    public boolean isCanViewFinancials() { return canViewFinancials; }
    public void setCanViewFinancials(boolean canViewFinancials) { this.canViewFinancials = canViewFinancials; }
}
