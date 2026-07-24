package com.policycenter.test;

import com.policycenter.controller.JobController;
import com.policycenter.gs.classes.job.RenewalProcess;
import com.policycenter.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PolicyRenewalEngineTest {

    @Test
    @DisplayName("Feature 05: Test Automated Policy Renewal & YoY Impact Analyzer Engine")
    public void testPolicyRenewalEngine() {
        Account acc = new Account("acc-r1", "C771001", new Contact("c1", "Global Transport Corp", "Company", "g@t.com", "555-9000"), "Transport");
        PolicyPeriod period = new PolicyPeriod("prd-r1", acc, acc.getAccountHolder(), "2025-01-01", "2026-01-01");
        period.setPolicyNumber("POL-771001");
        period.setTotalPremium(2500.00);

        Building bldg = new Building("bldg-r1", 1, "Logistics Center", "Joisted Masonry", 1000000.0, 200000.0);
        PolicyLine line = new PolicyLine("line-r1", "CommercialProperty", "Property Line");
        line.addBuilding(bldg);
        period.addLine(line);

        Job job = new Job("JOB-771001", "Renewal", "Draft", period);

        RenewalProcess process = new RenewalProcess(job);
        Map<String, Object> packet = process.generateRenewalPacket();

        assertNotNull(packet);
        assertEquals("POL-771001", packet.get("policyNumber"));
        assertEquals(2, packet.get("termNumber"));
        assertNotNull(packet.get("expiringPremium"));
        assertNotNull(packet.get("renewalPremium"));
        assertNotNull(packet.get("premiumDelta"));
        assertEquals("READY_FOR_DISPATCH", packet.get("acordRenewalNoticeStatus"));

        // Test JobController endpoint
        JobController controller = new JobController();
        Map<String, Object> restPacket = controller.getRenewalPacket(Map.of("jobNumber", "JOB-001"));
        assertNotNull(restPacket);
        assertNotNull(restPacket.get("renewalPremium"));
    }
}
