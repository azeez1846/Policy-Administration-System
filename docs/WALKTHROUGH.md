# Guidewire PolicyCenter Sandbox - Complete Technical Walkthrough & Reference

This document records all architectural updates, SQLite database persistence verifications, feature module folder structures, and automated unit test suite results for **Guidewire PolicyCenter Sandbox** (`/Users/azeezmohiuddin/Downloads/PolicyCenter`).

---

## 📂 Part 1: Organized Feature Module Directory Structure

All features created so far are organized into dedicated subfolders under **`features/`**, featuring detailed code comments and individual `README.md` documentation guides:

```text
/Users/azeezmohiuddin/Downloads/PolicyCenter/features/
├── 01_ai_underwriting_assistant/
│   ├── AIRiskAssessmentService.java
│   ├── AIUWController.java
│   ├── AIRiskAssistantWidget.js
│   ├── AIRiskAssessmentServiceTest.java
│   └── README.md
├── 02_product_designer_and_gosu_studio/
│   ├── GosuStudioController.java
│   ├── ProductDesignerScreen.js
│   ├── GosuRuleStudioScreen.js
│   ├── GosuRulesEngineTest.java
│   └── README.md
├── 03_gis_geospatial_risk_heatmap/
│   ├── GISRiskController.java
│   ├── GISRiskMapScreen.js
│   ├── GISRiskControllerTest.java
│   └── README.md
├── 04_billing_center_and_claimcenter_link/
│   ├── BillingCenterController.java
│   ├── ClaimCenterController.java
│   ├── BillingCenterScreen.js
│   ├── ClaimsScreen.js
│   ├── BillingAndClaimsTest.java
│   └── README.md
├── 05_producer_and_insured_portals/
│   ├── PortalController.java
│   ├── ProducerPortalScreen.js
│   ├── PolicyholderPortalScreen.js
│   ├── ProducerPortalTest.java
│   └── README.md
├── 06_acord_document_generator/
│   ├── ACORDDocumentController.java
│   ├── ACORDDocumentScreen.js
│   ├── ACORDDocumentControllerTest.java
│   └── README.md
├── 07_guidewire_assist_ai_chatbot/
│   ├── AIAssistController.java
│   ├── GuidewireAIChatWidget.js
│   ├── AIAssistControllerTest.java
│   └── README.md
├── 08_activity_task_and_note_engine/
│   ├── ActivityController.java
│   ├── ActivityTaskScreen.js
│   ├── ActivityControllerTest.java
│   └── README.md
├── 09_executive_portfolio_analytics/
│   ├── PortfolioAnalyticsController.java
│   ├── PortfolioAnalyticsScreen.js
│   ├── PortfolioAnalyticsControllerTest.java
│   └── README.md
└── 10_schedule_rating_irpm_studio/
    ├── IRPMController.java
    ├── IRPMScreen.js
    ├── IRPMControllerTest.java
    └── README.md
```

---

## 🧪 Part 2: Automated JUnit 5 Unit Test Results

Ran `mvn test`:
```text
[INFO] --- surefire:3.2.3:test (default-test) @ policycenter-sandbox ---
[INFO] Running com.policycenter.test.PolicyCenterFeatureTestSuite
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.041 s
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

### Verified Test Cases:
1. `testAIRiskAssessmentService`: Verifies baseline risk score (25.0), high TIV building risk (> $2.0M), structural age (< 1980), unsprinklered penalty, and ACORD OCR text parsing.
2. `testGosuRulesEngine`: Verifies Gosu bytecode compilation execution and UW rule evaluation on PolicyPeriod objects.
3. `testGISRiskController`: Verifies 5 commercial property concentration locations and TIV bounds.
4. `testBillingAndClaims`: Verifies 12-Pay installment schedule generation and ClaimCenter loss runs/open reserves.
5. `testProducerPortal`: Verifies 60-second quick quote rate calculation ($3,725.00) and ACORD 25 COI document HTML.
6. `testACORDDocumentController`: Verifies formatted ACORD 125 and ACORD 140 HTML document templates.
7. `testAIAssistController`: Verifies natural language AI Chatbot intent processing.
8. `testActivityController`: Verifies activity task creation, priority assignment (*Urgent*, *High*, *Medium*), and internal notes feed.
9. `testPortfolioAnalyticsController`: Verifies GWP metric aggregation ($14.85M) and portfolio loss ratio (44.2%).
10. `testIRPMController`: Verifies schedule rating IRPM credit/debit calculation (-15%) and manual factor overrides.

---

## 🛠️ Part 3: SQLite Database Table Persistence

- **DB Path**: `/Users/azeezmohiuddin/Downloads/PolicyCenter/policycenter.db`
- **Verified SQLite Queries**: Confirmed rows populated in `accounts`, `contacts`, `account_locations`, `jobs`, `policy_periods`, `policy_lines`, and `buildings` tables.

---

## 🌐 Server Status
Spring Boot server is active at **http://localhost:8080**.
