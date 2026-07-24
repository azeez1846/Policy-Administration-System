// Guidewire PolicyCenter - GIS Geospatial Risk Heatmap & Catastrophe Exposure UI

async function renderGISRiskMapScreen(container) {
    container.innerHTML = `<div style="padding:20px; color:#64748B;">Loading GIS Geospatial Exposure Layers & Catastrophe Zones...</div>`;

    try {
        const res = await fetch(`${API_BASE}/gis/exposures`);
        const exposures = res.ok ? await res.json() : [];

        // Fetch HazardHub Marketplace Risk Records
        const hazardRes = await fetch(`${API_BASE}/marketplace/hazard/list`);
        const hazardRecords = hazardRes.ok ? await hazardRes.json() : [];

        let pinsHtml = exposures.map(e => `
            <div style="background:#FFFFFF; border:1px solid #CBD5E1; border-radius:6px; padding:12px; margin-bottom:10px; border-left:4px solid ${e.tiv > 10000000 ? '#DC2626' : '#2563EB'};">
                <div style="display:flex; justify-content:space-between; align-items:center;">
                    <strong style="color:#0F172A; font-size:13px;">${e.name}</strong>
                    <span class="gw-badge ${e.tiv > 10000000 ? 'gw-badge-draft' : 'gw-badge-bound'}">${e.riskCategory}</span>
                </div>
                <div style="font-size:11px; color:#64748B; margin-top:4px;">
                    Coordinates: ${e.lat}, ${e.lng} | State: ${e.state}
                </div>
                <div style="font-size:13px; font-weight:800; color:#1E40AF; margin-top:6px;">
                    TIV Limit: $${e.tiv.toLocaleString()}
                </div>
            </div>
        `).join('');

        let hazardHtml = hazardRecords.map(h => `
            <div style="background:#0F172A; border:1px solid #334155; border-radius:6px; padding:10px; margin-bottom:8px; color:#F8FAFC;">
                <div style="display:flex; justify-content:space-between; align-items:center;">
                    <span style="font-size:12px; font-weight:700; color:#38BDF8;">${h.addressLine}</span>
                    <span class="gw-badge ${h.wildfireScore > 70 ? 'gw-badge-draft' : 'gw-badge-bound'}">${h.riskCategory || 'Risk Enriched'}</span>
                </div>
                <div style="display:grid; grid-template-columns:1fr 1fr 1fr; gap:6px; margin-top:8px; font-size:11px; color:#94A3B8;">
                    <div>🔥 Wildfire: <strong style="color:${h.wildfireScore > 70 ? '#EF4444' : '#10B981'};">${h.wildfireScore}/100</strong></div>
                    <div>🌊 Flood Zone: <strong style="color:#F59E0B;">${h.floodZone}</strong></div>
                    <div>🏠 Roof Quality: <strong style="color:#60A5FA;">${h.roofConditionScore}/5.0</strong></div>
                </div>
            </div>
        `).join('');

        container.innerHTML = `
            <div style="display:grid; grid-template-columns: 2fr 1fr; gap:16px;">
                
                <!-- GIS Map Workspace -->
                <div>
                    <div class="gw-panel">
                        <div class="gw-panel-header" style="display:flex; justify-content:space-between; align-items:center;">
                            <span>🗺️ GIS Property Concentration & Catastrophe Risk Map</span>
                            <div class="gw-btn-group">
                                <button class="gw-btn" style="padding:2px 8px; font-size:11px;" onclick="alert('Hurricane Zone layer toggled')">🌀 Hurricane</button>
                                <button class="gw-btn" style="padding:2px 8px; font-size:11px;" onclick="alert('Flood Zone A layer toggled')">🌊 Flood Zone</button>
                                <button class="gw-btn" style="padding:2px 8px; font-size:11px;" onclick="alert('Earthquake Fault lines toggled')">🌋 Earthquake</button>
                            </div>
                        </div>
                        <div class="gw-panel-body" style="padding:0;">
                            <!-- Simulated Map Container -->
                            <div id="gis-map-canvas" style="height:320px; background:#1E293B; border-radius:0 0 6px 6px; position:relative; overflow:hidden; display:flex; align-items:center; justify-content:center; color:#94A3B8;">
                                <div style="position:absolute; top:15px; left:15px; background:rgba(15,23,42,0.9); padding:10px 14px; border-radius:6px; color:#FFFFFF; font-size:12px; border:1px solid #334155;">
                                    <div><strong>National Exposure Summary</strong></div>
                                    <div style="font-size:11px; color:#CBD5E1;">Total In-Force TIV: <strong style="color:#60A5FA;">$63,400,000</strong></div>
                                    <div style="font-size:11px; color:#CBD5E1;">Active Catastrophe Overlays: <strong style="color:#F59E0B;">3 Active Zones</strong></div>
                                </div>

                                <div style="text-align:center;">
                                    <div style="font-size:40px; margin-bottom:4px;">🗺️</div>
                                    <div style="font-size:13px; font-weight:700; color:#F8FAFC;">Interactive GIS Map Server (Leaflet.js Engine)</div>
                                    <div style="font-size:11px; color:#94A3B8;">Displaying 5 High-TIV Commercial Locations & Catastrophe Buffers</div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Marketplace Accelerator Card -->
                    <div class="gw-panel" style="margin-top:16px; border-left:4px solid #0284C7;">
                        <div class="gw-panel-header" style="display:flex; justify-content:space-between; align-items:center;">
                            <span>⚡ Guidewire Marketplace Accelerator: HazardHub Risk Intelligence</span>
                            <button class="gw-btn gw-btn-primary" style="font-size:11px; padding:3px 10px;" onclick="runGlobalHazardEnrichment()">⚡ Enrich All Locations</button>
                        </div>
                        <div class="gw-panel-body">
                            <div style="font-size:12px; color:#475569; margin-bottom:10px;">
                                HazardHub Geocoding API automatically calculates wildfire vulnerability ratings, flood zone classifications, aerial roof condition scores, and hail severity. High-risk locations (>75 wildfire score) trigger automatic <strong>Block Bind</strong> Underwriting Issues.
                            </div>
                            <div id="hazardhub-records-list">
                                ${hazardHtml}
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Insured Property Risk Pins -->
                <div>
                    <div class="gw-panel">
                        <div class="gw-panel-header">Insured Locations & TIV Limits</div>
                        <div class="gw-panel-body" style="max-height:580px; overflow-y:auto;">
                            ${pinsHtml}
                        </div>
                    </div>
                </div>

            </div>
        `;
    } catch (e) {
        container.innerHTML = `<div style="color:red; padding:20px;">Error loading GIS Map data: ${e}</div>`;
    }
}

async function runGlobalHazardEnrichment() {
    try {
        const res = await fetch(`${API_BASE}/marketplace/hazard/enrich`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                locationId: 'accloc-1',
                buildingId: 'bldg-1',
                addressLine: '742 Evergreen Terrace, Malibu Canyon, CA',
                state: 'CA'
            })
        });
        const data = await res.json();
        alert('⚡ HazardHub Enrichment Executed!\nLocation: Malibu Canyon, CA\nWildfire Score: ' + data.hazardIntelligence.wildfireScore + '/100 (Extreme Hazard)\nFlood Zone: ' + data.hazardIntelligence.floodZone + '\nUnderwriting Status: Block Bind Referral Triggered.');
        location.reload();
    } catch (err) {
        alert('Enrichment failed: ' + err);
    }
}
