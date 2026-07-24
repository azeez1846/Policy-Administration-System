# Guidewire PolicyCenter Sandbox — Technical Handover Document

**Version**: 1.0.0  
**Date**: July 24, 2026  
**Environment**: Spring Boot 3.2.3 • Java 21 • SQLite 3.43 • Docker • GitHub Actions  
**Repository**: `/Users/azeezmohiuddin/Downloads/PolicyCenter`  

---

## 1. Executive Summary

The **Guidewire PolicyCenter Sandbox** is an enterprise-grade emulation of Guidewire PolicyCenter. It provides full policy lifecycle management—from submission intake and underwriting rule evaluation to mid-term endorsements, prorated delta rating, bulk WorkQueue batch processing, and ACORD document generation.

### Key Highlights
- **Full Policy Lifecycle**: Submissions, Mid-Term Policy Changes (Endorsements), Renewals, Cancellations, Reinstatements, and Premium Audits.
- **Guidewire WorkQueue Batch Processing Engine**: Emulates `BatchProcessLV.pcf` and `AutoPolicyChangeBatchProcess.gs` to automatically scan bound policies, apply coverage inflation factors (+ $250,000 building limits), rate prorated delta premiums, and issue endorsements in bulk.
- **59 Out-Of-The-Box (OOTB) Entity Tables**: Fully populated SQLite database schema matching Guidewire PolicyCenter data specifications.
- **13 Automated Test Modules**: Comprehensive JUnit 5 test suite (`PolicyCenterFeatureTestSuite.java`) verifying 100% of core engines.
- **Containerization & CI/CD**: Multi-stage `Dockerfile`, `docker-compose.yml` (mapped to port `9090`), and GitHub Actions workflow (`.github/workflows/ci-cd.yml`).

---

## 2. Technical Architecture & Core Components

```
+-----------------------------------------------------------------------------------+
|                        Frontend UI (Vanilla JS & Modern CSS)                      |
| index.html | app.js | PolicyChangeScreen.js | BatchSchedulerScreen.js | ...          |
+-----------------------------------------------------------------------------------+
                                        |  REST HTTP / JSON
                                        v
+-----------------------------------------------------------------------------------+
|                           Spring Boot REST Controllers                            |
| JobController | PolicyChangeController | BatchController | AIUWController | ...   |
+-----------------------------------------------------------------------------------+
                                        |
+-----------------------------------------------------------------------------------+
|                    Guidewire Business Logic & Rating Engine                       |
| SubmissionProcess.java | PolicyChangeProcess.java | RatingEngine.java                 |
| AutoPolicyChangeBatchProcess.java | UWRulesEngine.java                             |
+-----------------------------------------------------------------------------------+
                                        |  JDBC / Hibernate ORM
                                        v
+-----------------------------------------------------------------------------------+
|                         SQLite Database (policycenter.db)                         |
| 59 OOTB PolicyCenter Entity Tables (pc_account, pc_job, pc_policyperiod, etc.)   |
+-----------------------------------------------------------------------------------+
```

### Key Package Breakdown (`com.policycenter`)

| Package / File Path | Purpose & Responsibilities |
| :--- | :--- |
| `com.policycenter.model.*` | JPA Entity classes mapped to SQLite tables (Account, Job, PolicyPeriod, Building, Coverage, Cost, Activity, etc.). |
| `com.policycenter.repository.PolicyCenterSqliteRepository` | Data Access Object managing SQLite connection pooling, initial table creation, sample data seeding, and transactional commits. |
| `com.policycenter.gs.classes.job.SubmissionProcess` | State machine manager for submission jobs (`Draft` -> `Quoted` -> `Bound`). |
| `com.policycenter.gs.classes.job.PolicyChangeProcess` | Engine managing mid-term endorsement transactions, term proration factor calculation, annualized delta rating, and OOS checks. |
| `com.policycenter.gs.classes.batch.AutoPolicyChangeBatchProcess` | Guidewire WorkQueue batch processor scanning bound policies and executing bulk endorsements. |
| `com.policycenter.gs.classes.rating.RatingEngine` | Commercial property rating algorithm computing base rates, construction factors, protection class multipliers, deductibles, and taxes. |
| `com.policycenter.gs.classes.rules.UWRulesEngine` | Underwriting authority rules evaluator checking building TIV limits and premium delta thresholds. |
| `com.policycenter.controller.*` | Spring `@RestController` classes exposing RESTful APIs. |

---

## 3. Data Model: 59 OOTB PolicyCenter Entity Tables

The SQLite database (`policycenter.db`) initializes and persists **59 entity tables**:

### Core Policy Administration Entities
1. `pc_account`: Accounts and account numbers.
2. `pc_contact`: Account contacts, insureds, and producers.
3. `pc_accountlocation`: Primary & secondary risk addresses.
4. `pc_accountcontact`: Account contact role assignments.
5. `pc_policyterm`: Policy terms and annual numbers.
6. `pc_policyperiod`: Policy periods, status, term dates, and effective dates.
7. `pc_job`: Job instances (`Submission`, `PolicyChange`, `Renewal`, `Cancellation`, `Reinstatement`).
8. `pc_policyline`: Commercial property, commercial auto, and liability lines.
9. `pc_policylocation`: Property location details and protection classes.
10. `pc_building`: Buildings, construction types, and square footage.
11. `pc_policyvehicle`: Commercial vehicles, VIN numbers, and usage classifications.
12. `pc_policydriver`: Authorized drivers, license numbers, and violation history.

### Coverages, Financials & Transactions
13. `pc_coverage`: Building, contents, liability, and collision coverages.
14. `pc_exclusion`: Policy exclusions.
15. `pc_policycondition`: Policy conditions and endorsements.
16. `pc_policyaddlinsured`: Additional insured entities and certificates.
17. `pc_cost`: Rated line item costs, base premiums, and tax charges.
18. `pc_policytransaction`: Transaction ledger records.
19. `pc_transaction`: General financial ledger items.
20. `pc_paymentplan`: Payment schedules (Full-Pay, 4-Pay, 12-Pay).
21. `pc_modifier`: Schedule rating IRPM modification factors.
22. `pc_taxsurcharge`: State taxes, fire marshal fees, and surcharges.
23. `pc_policycommission`: Producer commission rates and split percentages.

### Underwriting, Rules & WorkQueue Batching
24. `pc_uwissue`: Underwriting referral issues and blocking levels.
25. `pc_uwauthorityprofile`: Underwriter authority levels ($1M, $2.5M, $5M).
26. `pc_uwauthoritygrant`: Underwriter grant limits.
27. `pc_uwcompany`: Admitted writing companies (e.g. Guidewire Mutual Insurance Co.).
28. `pc_activity`: Underwriting tasks and assignment queues.
29. `pc_activitypattern`: Activity templates and SLA target days.
30. `pc_batchjob`: WorkQueue batch process registry (`AutoPolicyChangeBatch`, `RenewalNoticeBatch`, etc.).
31. `pc_contingency`: Policy binding contingencies.
32. `pc_document`: ACORD documents, binders, and attachments.
33. `pc_formpattern`: Policy form patterns and endorsement rules.
34. `pc_policyform`: Issued policy forms (e.g., CP 00 10, IL 00 17).

### Class Codes, Reinsurance, Product Model & Setup
35. `pc_wcclasscode`: Workers' comp class codes and hazard rates.
36. `pc_wcemployee`: Workers' comp employee exposure records.
37. `pc_glclasscode`: General liability class codes.
38. `pc_glexposure`: General liability gross payroll / revenue exposures.
39. `pc_ririsk`: Reinsurance risk items.
40. `pc_riprogram`: Reinsurance treaty programs (Excess of Loss, Quota Share).
41. `pc_riattachment`: Reinsurance treaty attachments.
42. `pc_ratebook`: Rating books and effective dates.
43. `pc_rateroutine`: Rating routine scripts.
44. `pc_ratetablefactor`: Base rate factors and territory multipliers.
45. `pc_role`: User security roles (Underwriter, Producer, UW Manager).
46. `pc_user`: System users and credentials.
47. `pc_group`: User organization groups and regions.
48. `pc_organization`: Producer agencies and brokerages.
49. `pc_producercode`: Producer codes and NPN numbers.
50. `pc_producercodeassignment`: Agency territory assignments.
51. `pc_region`: Geographic operating regions.
52. `pc_jurisdiction`: State underwriting jurisdictions.
53. `pc_productmodel`: Product line definitions.
54. `pc_coveragepattern`: Standard coverage patterns.
55. `pc_claimdetail`: ClaimCenter loss history records.
56. `pc_auditschedule`: Premium audit schedules.
57. `pc_auditinformation`: Audited payroll/sales exposure records.
58. `pc_policyhold`: Geographic catastrophe moratoriums.
59. `pc_history`: Policy audit trail and version history.

---

## 4. REST API Endpoint Catalog

| HTTP Method | Endpoint Path | Description |
| :--- | :--- | :--- |
| **GET** | `/api/entities` | Returns database record counts across all 59 OOTB entity tables. |
| **POST** | `/api/jobs/submission` | Creates a new Submission job. |
| **POST** | `/api/jobs/quote` | Evaluates UW rules and computes rating quote for a submission. |
| **POST** | `/api/jobs/bind` | Binds and issues a submission policy period. |
| **POST** | `/api/jobs/renew` | Generates a renewal quote and issues next annual term policy. |
| **POST** | `/api/jobs/cancel` | Initiates policy cancellation (Pro-Rata, Flat, Short-Rate). |
| **POST** | `/api/jobs/reinstate` | Reinstates a cancelled policy period. |
| **POST** | `/api/policy-change/start` | Initiates a Mid-Term Policy Change job (`CHG-xxxxx`). |
| **POST** | `/api/policy-change/quote` | Calculates prorated delta premium and flags UW referral issues. |
| **POST** | `/api/policy-change/bind` | Binds and issues Policy Change endorsement. |
| **GET** | `/api/policy-change/document/{jobNumber}` | Generates printable ACORD Policy Change Endorsement HTML document. |
| **GET** | `/api/batch/jobs` | Returns list of all registered WorkQueue batch jobs. |
| **POST** | `/api/batch/auto-policy-change/run` | Triggers automated bulk Policy Change batch processor for bound policies. |
| **GET** | `/api/batch/auto-policy-change/logs` | Returns statistics and audit logs for the last batch run. |
| **POST** | `/api/ai/assess-risk` | Returns AI Risk Scorecard and straight-through processing recommendation. |
| **POST** | `/api/ai/ocr-parse` | OCR extracts building limits and FEINs from raw ACORD text. |
| **POST** | `/api/gosu/compile` | Compiles and executes Gosu business rule scripts. |
| **GET** | `/api/gis/locations` | Returns GIS risk heatmap coordinates and catastrophe vulnerability flags. |
| **GET** | `/api/billing/schedules` | Returns 12-Pay, 4-Pay, and Full-Pay installment payment schedules. |
| **GET** | `/api/claims/history` | Returns ClaimCenter loss run history and paid claims. |

---

## 5. Development, Containerization & Operational Commands

### Automated Test Suite Execution
Run all 13 unit test modules:
```bash
mvn clean test
```

### Local Application Execution
Start Spring Boot application server natively on port 8080:
```bash
mvn spring-boot:run
```
Access UI: **`http://localhost:8080`**

### Package Standalone Executable JAR
```bash
mvn clean package -DskipTests
java -jar target/policycenter-sandbox-1.0.0.jar
```

### Docker Container Operations

#### Build Container Image
```bash
docker build -t policycenter-sandbox:latest .
```

#### Run Container on Port 9090
```bash
docker run -d -p 9090:8080 --name policycenter-sandbox policycenter-sandbox:latest
```
Access UI: **`http://localhost:9090`**

#### Run via Docker Compose
```bash
docker compose up -d --build
```

#### Docker Operations & Logs
```bash
# Inspect container status
docker ps

# View container logs
docker logs -f policycenter-sandbox

# Stop & remove container
docker stop policycenter-sandbox && docker rm policycenter-sandbox
```

---

## 6. GitHub Actions CI/CD Pipeline (`.github/workflows/ci-cd.yml`)

The automated CI/CD pipeline triggers on every push or pull request to `main`/`master`:
- **Job 1 (`test-and-build`)**: Sets up JDK 21, runs `mvn clean test` across all 13 unit test modules, and uploads the built JAR artifact.
- **Job 2 (`docker-build-and-push`)**: Sets up Docker Buildx, authenticates to GitHub Container Registry (`ghcr.io`), and publishes the container image (`ghcr.io/YOUR_GITHUB_USERNAME/policycenter-sandbox:latest`).

---

**End of Technical Handover Document**
