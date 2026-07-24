# Guidewire Marketplace Accelerator: DocuSign E-Signature Integration

## Overview
This accelerator integrates **DocuSign E-Signature API** into Guidewire PolicyCenter document generation and binding workflows.

## Key Features
- **Envelope Dispatch**: Dispatches ACORD applications, binders, and endorsements for e-signature.
- **Status Lifecycle Tracking**: Monitors status transitions (`Sent`, `Delivered`, `Completed`, `Declined`).
- **Webhook Listener Simulation**: Receives asynchronous signature completion callbacks and automatically generates and attaches signed PDF records directly to PolicyCenter's `pc_document` repository.

## REST API Endpoints
- `POST /api/esignature/send`: Dispatches new e-signature envelope.
- `GET /api/esignature/status/{envelopeId}`: Retrieves envelope status.
- `POST /api/esignature/webhook/complete/{envelopeId}`: Simulates DocuSign webhook callback.
- `GET /api/esignature/envelopes`: Retrieves all tracked e-signature envelopes.

## UI Integration
Integrated into the **`📄 ACORD Docs`** screen (`frontend/js/ACORDDocumentScreen.js`).
