package com.policycenter.test;

import com.policycenter.controller.ESignatureController;
import com.policycenter.controller.HazardIntelligenceController;
import com.policycenter.gs.classes.rules.UWRulesEngine;
import com.policycenter.model.*;
import com.policycenter.repository.PolicyCenterSqliteRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 Test Module verifying Guidewire Marketplace Accelerators:
 * 1. HazardHub Property Risk & Environmental Geocoding Accelerator
 * 2. DocuSign E-Signature Integration Accelerator
 */
public class VerifyHazardAndESignatureAccelerators {

    private static PolicyCenterSqliteRepository repo;
    private static HazardIntelligenceController hazardController;
    private static ESignatureController esignController;

    @BeforeAll
    public static void setUp() {
        repo = PolicyCenterSqliteRepository.getInstance();
        hazardController = new HazardIntelligenceController();
        esignController = new ESignatureController();
    }

    @Test
    @DisplayName("Verify HazardHub Property Risk Enrichment API & UW Rule Enforcement")
    public void testHazardHubEnrichmentAndUWRules() {
        Map<String, String> payload = new HashMap<>();
        payload.put("locationId", "loc-test-ca-101");
        payload.put("buildingId", "bldg-1");
        payload.put("addressLine", "742 Evergreen Terrace, Malibu Canyon, CA");
        payload.put("state", "CA");

        Map<String, Object> result = hazardController.enrichLocation(payload);
        assertEquals("SUCCESS", result.get("status"));
        assertNotNull(result.get("hazardIntelligence"));

        HazardIntelligence hi = (HazardIntelligence) result.get("hazardIntelligence");
        assertTrue(hi.getWildfireScore() > 70, "California canyon property should have high wildfire score");

        // Verify UW Rule Triggering
        PolicyPeriod period = new PolicyPeriod();
        period.setPeriodID("period-hazard-test");
        PolicyLocation loc = new PolicyLocation("loc-test-ca-101", 1, "Malibu Risk Hub", "742 Evergreen Terrace", "Malibu", "CA", "90265");
        period.getLocations().add(loc);

        List<UWIssue> issues = UWRulesEngine.evaluatePeriodRules(period);
        assertNotNull(issues);
        boolean hasWildfireIssue = issues.stream().anyMatch(i -> i.getIssueKey().contains("wildfire") || i.getShortDescription().contains("wildfire"));
        assertTrue(hasWildfireIssue, "Extreme wildfire hazard should trigger an underwriting referral issue with Bind blocking level");
    }

    @Test
    @DisplayName("Verify DocuSign E-Signature Envelope Dispatch, Webhook Callback & Auto-Attachment")
    public void testDocuSignEnvelopeWorkflow() {
        Map<String, String> sendPayload = new HashMap<>();
        sendPayload.put("jobNumber", "SUB-8841");
        sendPayload.put("policyNumber", "POL-9921001");
        sendPayload.put("signerName", "Robert Sterling");
        sendPayload.put("signerEmail", "rsterling@sterlinglogistics.com");
        sendPayload.put("documentType", "ACORD 125 Commercial Insurance Application");

        Map<String, Object> sendResult = esignController.sendEnvelope(sendPayload);
        assertEquals("SUCCESS", sendResult.get("status"));
        ESignatureEnvelope envelope = (ESignatureEnvelope) sendResult.get("envelope");
        assertNotNull(envelope);
        assertEquals("Sent", envelope.getStatus());

        // Trigger Webhook Completion Callback
        Map<String, Object> webhookResult = esignController.triggerWebhookCompletion(envelope.getEnvelopeId());
        assertEquals("SUCCESS", webhookResult.get("status"));

        ESignatureEnvelope completedEnv = repo.getESignatureEnvelopeById(envelope.getEnvelopeId());
        assertNotNull(completedEnv);
        assertEquals("Completed", completedEnv.getStatus());
        assertNotNull(completedEnv.getDocumentId());

        // Verify signed document exists in pc_document repository
        Document doc = repo.getDocumentById(completedEnv.getDocumentId());
        assertNotNull(doc, "Signed document must be auto-created in Guidewire pc_document entity table");
        assertEquals("FinalSigned", doc.getStatus());
    }
}
