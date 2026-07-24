package com.policycenter.test;

import com.policycenter.model.Building;
import com.policycenter.service.EffDatedSliceManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EffDatedEngineTest {

    private final EffDatedSliceManager sliceManager = new EffDatedSliceManager();

    @Test
    @DisplayName("EffDated ORM: Test PrimaryID, FixedID, BranchID Triad Identity")
    public void testEffDatedIdentityTriad() {
        Building b = new Building("bldg-fixed-101", 1, "Main Warehouse", "Joisted Masonry", 1000000.0, 200000.0);
        
        assertNotNull(b.getId(), "PrimaryID (id) must be non-null");
        assertNotNull(b.getFixedID(), "FixedID must be non-null");
        assertEquals("BRANCH-DEFAULT", b.getBranchID());
        assertEquals("2026-01-01T00:00:00Z", b.getEffectiveDate());
        assertEquals("2027-01-01T00:00:00Z", b.getExpirationDate());
        assertEquals("ADDED", b.getChangeType());
        assertEquals(b.getFixedID(), b.getPublicID(), "Legacy publicID should resolve to FixedID");
    }

    @Test
    @DisplayName("EffDated ORM: Test Slice Splitting for Mid-Term Edits")
    public void testSliceSplitting() {
        Building originalSlice = new Building("bldg-fixed-202", 1, "HQ Office", "Frame", 500000.0, 100000.0);
        originalSlice.setBranchID("BRANCH-PERIOD-001");
        originalSlice.setEffectiveDate("2026-01-01T00:00:00Z");
        originalSlice.setExpirationDate("2027-01-01T00:00:00Z");

        String splitDate = "2026-06-01T00:00:00Z";
        Building newSlice = sliceManager.splitBuildingSlice(originalSlice, splitDate, 1200000.0, 250000.0);

        // Verify original slice was truncated
        assertEquals("2026-06-01T00:00:00Z", originalSlice.getExpirationDate());

        // Verify new slice attributes
        assertNotEquals(originalSlice.getId(), newSlice.getId(), "New slice must have a distinct PrimaryID");
        assertEquals(originalSlice.getFixedID(), newSlice.getFixedID(), "New slice must retain original FixedID");
        assertEquals(originalSlice.getBranchID(), newSlice.getBranchID(), "New slice must retain original BranchID");
        assertEquals(splitDate, newSlice.getEffectiveDate());
        assertEquals("2027-01-01T00:00:00Z", newSlice.getExpirationDate());
        assertEquals("MODIFIED", newSlice.getChangeType());
        assertEquals(1200000.0, newSlice.getBuildingLimit());
    }

    @Test
    @DisplayName("EffDated ORM: Test Branch Copy-on-Write Linking via FixedID")
    public void testBranching() {
        Building b1 = new Building("fixed-303", 1, "Distribution Center", "Masonry", 800000.0, 150000.0);
        b1.setBranchID("BRANCH-ORIGINAL");

        List<Building> cloned = sliceManager.branchBuildings(List.of(b1), "BRANCH-ENDORSEMENT-01");
        assertEquals(1, cloned.size());

        Building b1Branch = cloned.get(0);
        assertNotEquals(b1.getId(), b1Branch.getId(), "Branch clone must get new PrimaryID");
        assertEquals(b1.getFixedID(), b1Branch.getFixedID(), "Branch clone must keep original FixedID");
        assertEquals("BRANCH-ENDORSEMENT-01", b1Branch.getBranchID());
    }

    @Test
    @DisplayName("EffDated ORM: Test As-Of Date Slice Filtering")
    public void testAsOfSliceFiltering() {
        Building b1 = new Building("fixed-404", 1, "Facility", "Frame", 500000.0, 100000.0);
        b1.setEffectiveDate("2026-01-01T00:00:00Z");
        b1.setExpirationDate("2026-06-01T00:00:00Z");

        Building b2 = new Building("fixed-404", 1, "Facility", "Frame", 750000.0, 100000.0);
        b2.setEffectiveDate("2026-06-01T00:00:00Z");
        b2.setExpirationDate("2027-01-01T00:00:00Z");

        List<Building> allSlices = List.of(b1, b2);

        List<Building> marchSlices = sliceManager.getSlicesAsOf(allSlices, "2026-03-15T00:00:00Z");
        assertEquals(1, marchSlices.size());
        assertEquals(500000.0, marchSlices.get(0).getBuildingLimit());

        List<Building> augustSlices = sliceManager.getSlicesAsOf(allSlices, "2026-08-01T00:00:00Z");
        assertEquals(1, augustSlices.size());
        assertEquals(750000.0, augustSlices.get(0).getBuildingLimit());
    }
}
