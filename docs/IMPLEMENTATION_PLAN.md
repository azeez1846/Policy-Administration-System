# Guidewire PolicyCenter Sandbox - Architecture & Feature Plan

## Project Overview
This repository contains a full-stack Java (Spring Boot) and Vanilla JS Guidewire PolicyCenter enterprise sandbox supporting all 44 Out-of-the-Box (OOTB) Guidewire database entities, policy lifecycle transactions, rating worksheets, reinsurance cessions, and 5 advanced enterprise modules.

## Architecture Blueprint

```
/Users/azeezmohiuddin/Downloads/PolicyCenter/
├── backend/
│   └── com/policycenter/
│       ├── controller/        # REST APIs (Account, Job, Rating, Reinsurance, AI, Gosu, GIS, Billing, Portal)
│       ├── model/             # Guidewire Domain Entities (Account, PolicyPeriod, Building, PolicyLine, etc.)
│       ├── repository/        # PolicyCenterSqliteRepository (44 OOTB SQLite Tables)
│       ├── service/           # AIRiskAssessmentService, RatingEngine, ReinsuranceEngine
│       └── gs/                # Gosu Business Rules Engine & Process Controllers
├── frontend/
│   ├── css/
│   │   └── guidewire-theme.css # Authentic Guidewire PolicyCenter CSS Design Tokens & Styles
│   ├── js/                    # UI Component Screens & Sub-views
│   └── index.html             # Top Navigation Header & Work Area Layout
├── policycenter.db            # SQLite Relational Database File
└── docs/                      # Saved Walkthrough & Implementation Documentation
```

## Core Modules & Capabilities

1. **Policy Lifecycle Engine**: Submissions, Policy Changes (Endorsements), Renewals, Cancellations, Reinstatements, Audits, and Issuance.
2. **SQLite Relational Persistence**: Direct SQL table population across all 44 OOTB PolicyCenter tables.
3. **Rating Studio Engine**: Factor Overrides, Rate Books, Rate Routines, and Rating Worksheets.
4. **Reinsurance Management**: Treaty & Facultative cessions, Excess of Loss (XOL), Quota Share.
5. **AI Underwriting Assistant**: AI Risk Scorecard (0–100), Fraud Probability Index, and ACORD/Loss Run OCR Parser.
6. **No-Code Product Designer & Gosu Studio**: Visual LOB designer and interactive Gosu rule compiler/debugger.
7. **GIS Risk Map**: Interactive Leaflet geospatial exposure pins, TIV concentration heatmaps, and Catastrophe overlays.
8. **Billing & ClaimCenter Link**: 12-Pay installment ledger and real-time ClaimCenter loss runs/reserves.
9. **Producer & Insured Portals**: 60-second quick quote generator, ACORD 25 COI PDF/HTML generator, and customer portal.
