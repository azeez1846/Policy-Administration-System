# Module 03: GIS Geospatial Risk Heatmap & Catastrophe Exposure

## Overview
Provides GIS map visualization for commercial property concentration risk, Total Insured Value (TIV) exposure heatmaps, and catastrophe vulnerability zone overlays (Hurricane, Flood Zone A, Earthquake Fault Lines).

## Files Included
- `GISRiskController.java`: Spring Boot REST Controller (`/api/gis/exposures`).
- `GISRiskMapScreen.js`: Leaflet.js interactive map component displaying property pins and catastrophe layer toggles.

## Endpoints
- `GET /api/gis/exposures`: Returns list of insured commercial property locations, coordinates, TIV limits, and risk categories.
