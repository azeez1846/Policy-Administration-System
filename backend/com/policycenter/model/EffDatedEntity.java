package com.policycenter.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * Base Mapped Superclass for Guidewire-style Effective-Dated (EffDated) Entities.
 * 
 * Guidewire 3-Tier Identity Triad:
 * 1. PrimaryID (`id`): Unique physical database row key for a specific time-slice snapshot.
 * 2. FixedID (`fixedID`): Logical identity of the entity across effective time-slices and policy branches.
 * 3. BranchID (`branchID`): PolicyPeriod branch identifier that owns this entity slice version.
 */
@MappedSuperclass
public abstract class EffDatedEntity {

    @Id
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    @Column(name = "fixed_id", nullable = false, length = 64)
    private String fixedID;

    @Column(name = "branch_id", nullable = false, length = 64)
    private String branchID;

    @Column(name = "effective_date", length = 32)
    private String effectiveDate;

    @Column(name = "expiration_date", length = 32)
    private String expirationDate;

    @Column(name = "change_type", length = 32)
    private String changeType; // ADDED, MODIFIED, UNCHANGED, REMOVED

    public EffDatedEntity() {
        this.id = UUID.randomUUID().toString();
        this.fixedID = UUID.randomUUID().toString();
        this.branchID = "BRANCH-DEFAULT";
        this.effectiveDate = "2026-01-01T00:00:00Z";
        this.expirationDate = "2027-01-01T00:00:00Z";
        this.changeType = "ADDED";
    }

    public EffDatedEntity(String id, String fixedID, String branchID, String effectiveDate, String expirationDate, String changeType) {
        this.id = id != null ? id : UUID.randomUUID().toString();
        this.fixedID = fixedID != null ? fixedID : UUID.randomUUID().toString();
        this.branchID = branchID != null ? branchID : "BRANCH-DEFAULT";
        this.effectiveDate = effectiveDate;
        this.expirationDate = expirationDate;
        this.changeType = changeType != null ? changeType : "UNCHANGED";
    }

    public String getId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        return id;
    }

    public void setId(String id) {
        this.id = id != null ? id : UUID.randomUUID().toString();
    }

    public String getFixedID() {
        if (fixedID == null) {
            fixedID = UUID.randomUUID().toString();
        }
        return fixedID;
    }

    public void setFixedID(String fixedID) {
        this.fixedID = fixedID != null ? fixedID : UUID.randomUUID().toString();
    }

    public String getBranchID() {
        if (branchID == null) {
            branchID = "BRANCH-DEFAULT";
        }
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID != null ? branchID : "BRANCH-DEFAULT";
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    /**
     * Checks if this slice is effective as of a given ISO date string.
     */
    public boolean isEffectiveAsOf(String asOfDate) {
        if (asOfDate == null) return true;
        boolean afterEffective = (effectiveDate == null || asOfDate.compareTo(effectiveDate) >= 0);
        boolean beforeExpiration = (expirationDate == null || asOfDate.compareTo(expirationDate) < 0);
        return afterEffective && beforeExpiration;
    }
}
