# Guidewire Marketplace Accelerator: HazardHub Property Environmental Risk & Geocoding

## Overview
This accelerator integrates **HazardHub Geocoding & Environmental Risk Intelligence** directly into Guidewire PolicyCenter location risk evaluation.

## Key Features
- **Wildfire Risk Exposure Index**: Evaluates vulnerability (0-100 score). Properties exceeding a 75 threshold trigger automatic **Block Bind** Underwriting Issues.
- **FEMA Flood Zone Classification**: Identifies Zone A, AE, VE, and X classifications.
- **Aerial AI Roof Score**: Analyzes roof degradation and material condition (1.0 to 5.0).
- **Catastrophe Hazards**: Hail severity indices and coastal distance metrics.

## REST API Endpoints
- `POST /api/marketplace/hazard/enrich`: Geocodes and enriches location risk parameters.
- `GET /api/marketplace/hazard/location/{locationId}`: Fetches enriched hazard record by location ID.
- `GET /api/marketplace/hazard/list`: Retrieves all location hazard records.

## UI Integration
Integrated into the **`🗺️ GIS Risk Map`** screen (`frontend/js/GISRiskMapScreen.js`).
