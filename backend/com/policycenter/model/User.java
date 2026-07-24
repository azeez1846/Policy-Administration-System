package com.policycenter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pc_user")
public class User {

    @Id
    @Column(name = "public_id")
    private String publicID;

    @Column(name = "username")
    private String username;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "user_role")
    private String userRole;

    @Column(name = "email")
    private String email;

    @Column(name = "department")
    private String department;

    @Column(name = "authority_profile_id")
    private String authorityProfileID;

    public User() {}

    public User(String publicID, String username, String passwordHash, String displayName, String userRole) {
        this.publicID = publicID;
        this.username = username;
        this.passwordHash = passwordHash;
        this.displayName = displayName;
        this.userRole = userRole;
        this.email = username + "@guidewire.com";
        this.department = "Underwriting";
    }

    public User(String publicID, String username, String passwordHash, String displayName, String userRole, String authorityProfileID) {
        this.publicID = publicID;
        this.username = username;
        this.passwordHash = passwordHash;
        this.displayName = displayName;
        this.userRole = userRole;
        this.authorityProfileID = authorityProfileID;
        this.email = username + "@guidewire.com";
        this.department = "Underwriting";
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getPassword() { return passwordHash; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getFullName() { return displayName; }

    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole; }
    public String getRole() { return userRole; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getAuthorityProfileID() { return authorityProfileID; }
    public void setAuthorityProfileID(String authorityProfileID) { this.authorityProfileID = authorityProfileID; }

    public String getProducerCode() { return "301-009281"; }
}
