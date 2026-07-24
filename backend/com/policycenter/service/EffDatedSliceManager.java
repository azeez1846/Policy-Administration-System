package com.policycenter.service;

import com.policycenter.model.Building;
import com.policycenter.model.EffDatedEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service managing Guidewire-style Effective-Dated (EffDated) slicing,
 * mid-term edit slice splitting, and branch revision cloning.
 */
@Service
public class EffDatedSliceManager {

    /**
     * Filters a list of EffDated entities to return active slices as of a specific date.
     */
    public <T extends EffDatedEntity> List<T> getSlicesAsOf(List<T> entities, String asOfDate) {
        List<T> activeSlices = new ArrayList<>();
        if (entities == null) return activeSlices;

        for (T entity : entities) {

            if (!"REMOVED".equalsIgnoreCase(entity.getChangeType()) && entity.isEffectiveAsOf(asOfDate)) {
                activeSlices.add(entity);
            }
        }
        return activeSlices;
    }

    /**
     * Splits a Building slice mid-term at splitDate for a modification.
     * 
     * 1. Truncates existing building slice expirationDate to splitDate.
     * 2. Creates new building slice starting at splitDate with identical fixedID & branchID,
     *    new unique PrimaryID (id), and updated building attributes.
     */
    public Building splitBuildingSlice(Building original, String splitDate, double newBuildingLimit, double newContentsLimit) {
        String originalExpiration = original.getExpirationDate();

        // 1. Truncate existing slice
        original.setExpirationDate(splitDate);

        // 2. Instantiate new slice with identical fixedID and branchID
        Building newSlice = new Building();
        newSlice.setId(UUID.randomUUID().toString()); // New PrimaryID (id)
        newSlice.setFixedID(original.getFixedID());    // Keep Logical Identity (FixedID)
        newSlice.setBranchID(original.getBranchID());  // Same PolicyPeriod Branch
        newSlice.setEffectiveDate(splitDate);
        newSlice.setExpirationDate(originalExpiration);
        newSlice.setChangeType("MODIFIED");

        // Copy / update attributes
        newSlice.setBuildingNum(original.getBuildingNum());
        newSlice.setDescription(original.getDescription());
        newSlice.setConstructionType(original.getConstructionType());
        newSlice.setYearBuilt(original.getYearBuilt());
        newSlice.setNumStories(original.getNumStories());
        newSlice.setSprinklered(original.isSprinklered());
        newSlice.setAlarmType(original.getAlarmType());
        newSlice.setFireProtectionClass(original.getFireProtectionClass());
        newSlice.setBuildingLimit(newBuildingLimit);
        newSlice.setContentsLimit(newContentsLimit);

        return newSlice;
    }

    /**
     * Branch Copy-on-Write: Clones building slices to a new PolicyPeriod branch.
     * Preserves FixedID so entities remain linked across branches.
     */
    public List<Building> branchBuildings(List<Building> sourceBuildings, String newBranchId) {
        List<Building> cloned = new ArrayList<>();
        if (sourceBuildings == null) return cloned;

        for (Building b : sourceBuildings) {
            Building clone = new Building();
            clone.setId(UUID.randomUUID().toString()); // New PrimaryID
            clone.setFixedID(b.getFixedID());          // Same FixedID
            clone.setBranchID(newBranchId);             // New BranchID
            clone.setEffectiveDate(b.getEffectiveDate());
            clone.setExpirationDate(b.getExpirationDate());
            clone.setChangeType("UNCHANGED");

            clone.setBuildingNum(b.getBuildingNum());
            clone.setDescription(b.getDescription());
            clone.setConstructionType(b.getConstructionType());
            clone.setYearBuilt(b.getYearBuilt());
            clone.setNumStories(b.getNumStories());
            clone.setSprinklered(b.isSprinklered());
            clone.setAlarmType(b.getAlarmType());
            clone.setFireProtectionClass(b.getFireProtectionClass());
            clone.setBuildingLimit(b.getBuildingLimit());
            clone.setContentsLimit(b.getContentsLimit());

            cloned.add(clone);
        }
        return cloned;
    }
}
