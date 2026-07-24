package com.policycenter.test;

import com.policycenter.controller.*;
import com.policycenter.gs.classes.rules.UWRulesEngine;
import com.policycenter.model.*;
import com.policycenter.service.AIRiskAssessmentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Enterprise Automated Test Suite for All 10 PolicyCenter Feature Modules.
 */
public class PolicyCenterFeatureTestSuite {

    @Test
    @DisplayName("Module 01: Test AI Underwriting Risk Assessment & OCR Extractor")
    public void testAIRiskAssessmentService() {
        // Test Null / Default Baseline Risk Score
        Map<String, Object> baselineResult = AIRiskAssessmentService.assessPolicyPeriodRisk(null);
        assertNotNull(baselineResult);
        assertEquals(25.0, (Double) baselineResult.get("riskIndexScore"));
        assertEquals("AUTO-APPROVE ELIGIBLE (Straight-Through Processing)", baselineResult.get("decisionRecommendation"));

        // Test High TIV Building Risk (> $2.0M)
        Account acc = new Account("acc-t1", "C881001", new Contact("c1", "Test Corp", "Company", "t@c.com", "555-0199"), "Freight");
        PolicyPeriod period = new PolicyPeriod("prd-t1", acc, acc.getAccountHolder(), "2026-01-01", "2027-01-01");
        period.setProductCode("CommercialProperty");

        Building highBldg = new Building("bldg-1", 1, "HQ Facility", "Joisted Masonry", 2500000.0, 500000.0);
        highBldg.setYearBuilt(1975); // Prior to 1980 (+18)
        highBldg.setSprinklered(false); // Unsprinklered (+12)

        PolicyLine line = new PolicyLine("line-1", "CommercialProperty", "Property Line");
        line.addBuilding(highBldg);
        period.addLine(line);

        Map<String, Object> highRiskResult = AIRiskAssessmentService.assessPolicyPeriodRisk(period);

        // Expected score: 25 (base) + 25 (limit > $2M) + 18 (built < 1980) + 12 (unsprinklered) = 80.0
        assertEquals(80.0, (Double) highRiskResult.get("riskIndexScore"));
        assertEquals("REFERRAL REQUIRED (Senior Underwriter Approval Needed)", highRiskResult.get("decisionRecommendation"));

        // Test OCR Extractor
        Map<String, Object> ocrResult = AIRiskAssessmentService.parseACORDDocument("Sample ACORD Text");
        assertEquals("Titan Global Operations Inc", ocrResult.get("companyName"));
        assertEquals("88-9911223", ocrResult.get("fein"));
    }

    @Test
    @DisplayName("Module 02: Test Gosu Business Rules Engine & Studio Controller")
    public void testGosuRulesEngine() {
        GosuStudioController controller = new GosuStudioController();
        Map<String, Object> execResult = controller.executeGosuRule(Map.of("ruleName", "TestGosuRule", "ruleCode", "class Test {}"));
        assertEquals("SUCCESS", execResult.get("status"));
        assertNotNull(execResult.get("logs"));

        List<Map<String, String>> rules = controller.getGosuRules();
        assertFalse(rules.isEmpty());

        // Test Gosu UW Rules Engine evaluation
        Account acc = new Account("acc-t2", "C881002", new Contact("c2", "Acme", "Company", "a@c.com", "555-0100"), "Commercial");
        PolicyPeriod period = new PolicyPeriod("prd-t2", acc, acc.getAccountHolder(), "2026-01-01", "2027-01-01");
        Building bldg = new Building("bldg-2", 1, "Facility #2", "Frame", 1500000.0, 200000.0);
        PolicyLine line = new PolicyLine("line-2", "CommercialProperty", "Line 2");
        line.addBuilding(bldg);
        period.addLine(line);

        List<UWIssue> issues = UWRulesEngine.evaluatePeriodRules(period);
        assertFalse(issues.isEmpty());
        assertTrue(UWRulesEngine.hasBlockingIssues(period, "Bind"));
    }

    @Test
    @DisplayName("Module 03: Test GIS Geospatial Exposure API")
    public void testGISRiskController() {
        GISRiskController controller = new GISRiskController();
        List<Map<String, Object>> exposures = controller.getGeospatialExposures();
        assertEquals(5, exposures.size());
        assertEquals("Chicago HQ Warehouse", exposures.get(0).get("name"));
    }

    @Test
    @DisplayName("Module 04: Test Billing Installment Schedule & ClaimCenter Loss History")
    public void testBillingAndClaims() {
        BillingCenterController billingController = new BillingCenterController();
        Map<String, Object> schedule = billingController.getInstallmentSchedule("12Pay", 2400.0);
        assertEquals("12Pay", schedule.get("paymentPlan"));
        List<?> installments = (List<?>) schedule.get("installments");
        assertEquals(12, installments.size());

        ClaimCenterController claimController = new ClaimCenterController();
        List<Map<String, Object>> claims = claimController.getClaimHistory("C00010928");
        assertFalse(claims.isEmpty());
        assertEquals("CLM-90021", claims.get(0).get("claimNumber"));
    }

    @Test
    @DisplayName("Module 05: Test Producer Quick Quote & COI Generator")
    public void testProducerPortal() {
        PortalController controller = new PortalController();
        Map<String, Object> qq = controller.calculateQuickQuote(Map.of("productLine", "Commercial Auto", "limit", "1000000"));
        assertEquals("QUOTED", qq.get("status"));
        assertEquals(3725.0, (Double) qq.get("estimatedTotalCost"));

        ResponseEntity<String> coiRes = controller.generateCOI();
        assertNotNull(coiRes);
        String coiBody = coiRes.getBody();
        assertNotNull(coiBody);
        assertTrue(coiBody.contains("ACORD™ CERTIFICATE OF LIABILITY INSURANCE"));
    }

    @Test
    @DisplayName("Module 06: Test ACORD Document Generator Studio")
    public void testACORDDocumentController() {
        ACORDDocumentController controller = new ACORDDocumentController();
        ResponseEntity<String> acord125 = controller.getACORD125Document("C00010928");
        assertNotNull(acord125);
        String body125 = acord125.getBody();
        assertNotNull(body125);
        assertTrue(body125.contains("ACORD™ 125"));

        ResponseEntity<String> acord140 = controller.getACORD140Document("C00010928");
        assertNotNull(acord140);
        String body140 = acord140.getBody();
        assertNotNull(body140);
        assertTrue(body140.contains("ACORD™ 140"));
    }

    @Test
    @DisplayName("Module 07: Test Guidewire Assist AI Chatbot Controller")
    public void testAIAssistController() {
        AIAssistController controller = new AIAssistController();
        Map<String, Object> chatRes = controller.processAIChat(Map.of("message", "tell me about account C00010928"));
        assertTrue(chatRes.get("reply").toString().contains("C00010928"));
    }

    @Test
    @DisplayName("Module 08: Test Activity Tasks & Underwriting Notes Controller")
    public void testActivityController() {
        ActivityController controller = new ActivityController();
        List<Map<String, Object>> activities = controller.getActivities();
        assertFalse(activities.isEmpty());

        Map<String, Object> newAct = controller.createActivity(Map.of("subject", "Verify Fire Inspection", "priority", "High"));
        assertEquals("Verify Fire Inspection", newAct.get("subject"));

        List<Map<String, Object>> notes = controller.getNotes();
        assertFalse(notes.isEmpty());
    }

    @Test
    @DisplayName("Module 09: Test Executive UW Portfolio Analytics Controller")
    public void testPortfolioAnalyticsController() {
        PortfolioAnalyticsController controller = new PortfolioAnalyticsController();
        Map<String, Object> analytics = controller.getPortfolioAnalytics();
        assertEquals(14850000.0, analytics.get("grossWrittenPremium"));
        assertEquals(44.2, analytics.get("overallLossRatio"));
    }

    @Test
    @DisplayName("Module 10: Test Schedule Rating IRPM Modification Controller")
    public void testIRPMController() {
        IRPMController controller = new IRPMController();
        Map<String, Object> irpmData = controller.getIRPMFactors();
        assertEquals(-15.0, irpmData.get("appliedIRPMPct"));
        assertEquals(2040.0, irpmData.get("modifiedPremium"));

        Map<String, Object> overrideRes = controller.applyIRPMOverride(Map.of("irpmPct", -20.0));
        assertEquals(1920.0, overrideRes.get("modifiedPremium"));
    }

    @Test
    @DisplayName("Module 11: Test OOTB 59 Entity Catalog Explorer Controller")
    public void testEntityExplorerController() {
        EntityExplorerController controller = new EntityExplorerController();
        Map<String, Integer> counts = controller.getEntityCounts();
        assertNotNull(counts);
        assertTrue(counts.size() >= 59, "Should contain at least 59 entity tables");
        assertTrue(counts.containsKey("activities"));
        assertTrue(counts.containsKey("activity_patterns"));
        assertTrue(counts.containsKey("roles"));
        assertTrue(counts.containsKey("organizations"));
        assertTrue(counts.containsKey("producer_code_assignments"));
        assertTrue(counts.containsKey("regions"));
        assertTrue(counts.containsKey("jurisdictions"));
        assertTrue(counts.containsKey("product_models"));
        assertTrue(counts.containsKey("coverage_patterns"));
        assertTrue(counts.containsKey("claim_details"));
        assertTrue(counts.containsKey("audit_schedules"));
        assertTrue(counts.containsKey("policy_holds"));

        List<Map<String, Object>> catalog = controller.getEntityCatalog();
        assertNotNull(catalog);
        assertFalse(catalog.isEmpty());
        assertTrue(catalog.size() >= 12, "Should contain at least 12 domain categories");
    }

    @Test
    @DisplayName("Module 12: Test Mid-Term Policy Change Endorsement Transaction Engine")
    public void testPolicyChangeController() {
        PolicyChangeController controller = new PolicyChangeController();

        // 1. Start Policy Change Transaction
        ResponseEntity<Map<String, Object>> startRes = controller.startPolicyChange(Map.of(
            "policyNumber", "CP-8472910",
            "effectiveDate", "2026-08-01",
            "changeReason", "Increase Building Coverage Limit"
        ));
        assertNotNull(startRes);
        assertEquals(200, startRes.getStatusCode().value());
        Map<String, Object> startBody = startRes.getBody();
        assertNotNull(startBody);
        assertTrue(startBody.containsKey("jobNumber"));
        assertEquals("Draft", startBody.get("status"));

        String chgJobNum = startBody.get("jobNumber").toString();

        // 2. Quote Policy Change (Prorated Delta Rating)
        ResponseEntity<Map<String, Object>> quoteRes = controller.quotePolicyChange(Map.of(
            "jobNumber", chgJobNum,
            "effectiveDate", "2026-08-01",
            "changeReason", "Increase Building Coverage Limit",
            "buildingLimit", 2000000.0,
            "contentsLimit", 300000.0,
            "constructionType", "Joisted Masonry"
        ));
        assertNotNull(quoteRes);
        assertEquals(200, quoteRes.getStatusCode().value());
        Map<String, Object> quoteBody = quoteRes.getBody();
        assertNotNull(quoteBody);
        assertEquals("Quoted", quoteBody.get("status"));
        assertTrue((Double) quoteBody.get("proratedDeltaPremium") > 0);

        // 3. Bind & Issue Policy Change
        ResponseEntity<Map<String, Object>> bindRes = controller.bindPolicyChange(Map.of(
            "jobNumber", chgJobNum,
            "boundBy", "Super User"
        ));
        assertNotNull(bindRes);
        assertEquals(200, bindRes.getStatusCode().value());
        Map<String, Object> bindBody = bindRes.getBody();
        assertNotNull(bindBody);
        assertEquals("Bound", bindBody.get("jobStatus"));

        // 4. Generate ACORD Policy Change Endorsement Document
        ResponseEntity<String> docRes = controller.getEndorsementDocument(chgJobNum);
        assertNotNull(docRes);
        String docBody = docRes.getBody();
        assertNotNull(docBody);
        assertTrue(docBody.contains("POLICY CHANGE ENDORSEMENT NOTICE"));
    }

    @Test
    @DisplayName("Module 13: Test Guidewire WorkQueue Auto PolicyChange Batch Process Engine")
    public void testAutoPolicyChangeBatchProcess() {
        BatchController controller = new BatchController();

        // 1. Run Automated Policy Change Batch Process
        Map<String, Object> stats = controller.executeAutoPolicyChangeBatch();
        assertNotNull(stats);
        assertEquals("Completed", stats.get("status"));
        assertEquals("AutoPolicyChangeBatch", stats.get("batchProcessType"));
        assertTrue((Integer) stats.get("endorsementsIssued") >= 1);

        // 2. Fetch Batch Audit Execution Logs
        Map<String, Object> logStats = controller.getAutoPolicyChangeLogsData();
        assertNotNull(logStats);
        assertTrue(logStats.containsKey("executionLogs"));
    }

    @Test
    @DisplayName("Module 14: Test Guidewire Marketplace Accelerators (HazardHub & DocuSign E-Signature)")
    public void testMarketplaceAccelerators() {
        HazardIntelligenceController hazardCtrl = new HazardIntelligenceController();
        ESignatureController esignCtrl = new ESignatureController();

        // 1. Test HazardHub Property Risk Enrichment API
        Map<String, String> hzPayload = Map.of(
            "locationId", "loc-suite-test-1",
            "addressLine", "100 Ocean Drive, Malibu Canyon, CA",
            "state", "CA"
        );
        Map<String, Object> hzRes = hazardCtrl.enrichLocation(hzPayload);
        assertEquals("SUCCESS", hzRes.get("status"));
        HazardIntelligence hi = (HazardIntelligence) hzRes.get("hazardIntelligence");
        assertNotNull(hi);
        assertTrue(hi.getWildfireScore() > 70);

        // 2. Test DocuSign Envelope Dispatch & Webhook Completion
        Map<String, String> esignPayload = Map.of(
            "jobNumber", "SUB-SUITE-101",
            "policyNumber", "POL-SUITE-101",
            "signerName", "Alice Sterling",
            "signerEmail", "alice@sterling.com",
            "documentType", "ACORD 125 Application"
        );
        Map<String, Object> esignRes = esignCtrl.sendEnvelope(esignPayload);
        assertEquals("SUCCESS", esignRes.get("status"));
        ESignatureEnvelope env = (ESignatureEnvelope) esignRes.get("envelope");
        assertNotNull(env);

        Map<String, Object> webhookRes = esignCtrl.triggerWebhookCompletion(env.getEnvelopeId());
        assertEquals("SUCCESS", webhookRes.get("status"));
        ESignatureEnvelope completedEnv = (ESignatureEnvelope) webhookRes.get("envelope");
        assertEquals("Completed", completedEnv.getStatus());
        assertNotNull(completedEnv.getDocumentId());
    }
}



