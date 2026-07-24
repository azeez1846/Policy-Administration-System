# Module 01: AI Underwriting Assistant & Risk Analytics

## Overview
This feature module integrates AI Risk Scoring, Fraud Analytics, and ACORD/Loss Run OCR document text extraction into Guidewire PolicyCenter.

## Files Included
- `AIRiskAssessmentService.java`: Java service computing AI Risk Index (0–100), Fraud %, and UW recommendations.
- `AIUWController.java`: Spring Boot REST Controller exposing `/api/ai/assess-risk` and `/api/ai/ocr-parse`.
- `AIRiskAssistantWidget.js`: Frontend component rendering the AI Scorecard UI and OCR Text Parser modal.

## Endpoints
- `POST /api/ai/assess-risk`: Evaluates submission risk factors.
- `POST /api/ai/ocr-parse`: Parses raw document text to extract company and building parameters.
