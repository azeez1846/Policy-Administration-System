package com.policycenter.test;

import com.policycenter.controller.OOSController;
import com.policycenter.gs.classes.job.OOSEngine;
import com.policycenter.model.PolicyVersion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PolicyVersionDiffTest {

    @Test
    @DisplayName("Feature 03: Test Visual Policy Period Diff & Version Comparison Engine")
    public void testPolicyVersionDiffEngine() {
        // Fetch versions
        List<PolicyVersion> versions = OOSEngine.getHistoryForPolicy("POL-88201");
        assertFalse(versions.isEmpty());

        // Test OOS endorsement creation
        PolicyVersion newVer = OOSEngine.executeOOSEndorsement("POL-88201", "2026-03-01", "Added Flood Endorsement");
        assertNotNull(newVer);
        assertTrue(newVer.isOOS());

        // Test Version Comparison Diff calculation
        Map<String, Object> diff = OOSEngine.compareVersions("POL-88201", 1, 2);
        assertNotNull(diff);
        assertEquals("POL-88201", diff.get("policyNumber"));

        List<?> diffItems = (List<?>) diff.get("diffItems");
        assertNotNull(diffItems);
        assertFalse(diffItems.isEmpty());

        Double netDelta = (Double) diff.get("netPremiumDelta");
        assertNotNull(netDelta);

        // Test REST Controller diff endpoint
        OOSController controller = new OOSController();
        Map<String, Object> restDiff = controller.comparePolicyVersions("POL-88201", 1, 2);
        assertNotNull(restDiff);
        assertNotNull(restDiff.get("diffItems"));
    }
}
