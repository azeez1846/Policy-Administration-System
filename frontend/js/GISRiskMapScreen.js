// Guidewire PolicyCenter - GIS Geospatial Risk Heatmap & Catastrophe Exposure UI

async function renderGISRiskMapScreen(container) {
    container.innerHTML = `<div style="padding:20px; color:#64748B;">Loading GIS Geospatial Exposure Layers & Catastrophe Moratoriums...</div>`;

    try {
        const [expRes, hazardRes, morRes] = await Promise.all([
            fetch(`${API_BASE}/gis/exposures`),
            fetch(`${API_BASE}/marketplace/hazard/list`),
            fetch(`${API_BASE}/gis/moratoriums`)
        ]);

        const exposures = expRes.ok ? await expRes.json() : [];
        const hazardRecords = hazardRes.ok ? await hazardRes.json() : [];
        const moratoriums = morRes.ok ? await morRes.json() : [];

        // Helper function for Haversine distance calculation
        function getDistanceMiles(lat1, lon1, lat2, lon2) {
            const dLat = (lat2 - lat1) * Math.PI / 180;
            const dLon = (lon2 - lon1) * Math.PI / 180;
            const a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                      Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                      Math.sin(dLon/2) * Math.sin(dLon/2);
            return 3958.8 * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        }

        let activeMoratoriums = moratoriums.filter(m => m.status === 'ACTIVE');

        let pinsHtml = exposures.map(e => {
            // Check if exposure is within active moratorium
            let blockedBy = activeMoratoriums.find(m => getDistanceMiles(e.lat, e.lng, m.lat, m.lng) <= m.radiusMiles);
            let isBlocked = !!blockedBy;

            return `
                <div style="background:${isBlocked ? '#FEF2F2' : '#FFFFFF'}; border:1px solid ${isBlocked ? '#FCA5A5' : '#CBD5E1'}; border-radius:6px; padding:12px; margin-bottom:10px; border-left:4px solid ${isBlocked ? '#DC2626' : (e.tiv > 10000000 ? '#EA580C' : '#2563EB')};">
                    <div style="display:flex; justify-content:space-between; align-items:center;">
                        <strong style="color:#0F172A; font-size:13px;">${e.name}</strong>
                        <span class="gw-badge ${isBlocked ? 'gw-badge-draft' : (e.tiv > 10000000 ? 'gw-badge-quote' : 'gw-badge-bound')}">
                            ${isBlocked ? '🚫 MORATORIUM HOLD' : e.riskCategory}
                        </span>
                    </div>
                    <div style="font-size:11px; color:#64748B; margin-top:4px;">
                        Coordinates: ${e.lat}, ${e.lng} | State: ${e.state}
                    </div>
                    <div style="font-size:13px; font-weight:800; color:#1E40AF; margin-top:6px; display:flex; justify-content:space-between; align-items:center;">
                        <span>TIV Limit: $${e.tiv.toLocaleString()}</span>
                        ${isBlocked ? `<span style="font-size:10px; background:#DC2626; color:#FFFFFF; padding:2px 6px; border-radius:4px;">${blockedBy.catastropheType.toUpperCase()} HOLD (${blockedBy.radiusMiles} mi radius)</span>` : ''}
                    </div>
                </div>
            `;
        }).join('');

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

        let moratoriumsHtml = moratoriums.map(m => {
            let icon = m.catastropheType === 'Hurricane' ? '🌀' : (m.catastropheType === 'Wildfire' ? '🔥' : (m.catastropheType === 'Flood' ? '🌊' : '🌋'));
            let isActive = m.status === 'ACTIVE';
            return `
                <div style="background:#FFFFFF; border:1px solid ${isActive ? '#F59E0B' : '#E2E8F0'}; border-radius:6px; padding:12px; margin-bottom:10px; border-left:4px solid ${isActive ? '#D97706' : '#94A3B8'};">
                    <div style="display:flex; justify-content:space-between; align-items:center;">
                        <strong style="color:#0F172A; font-size:13px;">${icon} ${m.name}</strong>
                        <span class="gw-badge ${isActive ? 'gw-badge-draft' : 'gw-badge-bound'}">${m.status}</span>
                    </div>
                    <div style="font-size:11px; color:#475569; margin-top:4px;">
                        Center: <strong>${m.lat}, ${m.lng}</strong> | Radius: <strong style="color:#D97706;">${m.radiusMiles} Miles</strong>
                    </div>
                    <div style="font-size:11px; color:#64748B; margin-top:4px; display:flex; gap:12px;">
                        <span>Effective: ${m.effectiveDate}</span>
                        <span>Placed By: ${m.createdBy}</span>
                    </div>
                    <div style="margin-top:8px; display:flex; justify-content:space-between; align-items:center; pt-2; border-top:1px solid #F1F5F9;">
                        <span style="font-size:10px; color:#DC2626; font-weight:700;">
                            ${m.blocksBind ? '🔒 BLOCKS BINDING' : ''} ${m.blocksQuote ? '⚡ BLOCKS QUOTING' : ''}
                        </span>
                        ${isActive ? `<button class="gw-btn" style="font-size:10px; padding:2px 8px; color:#DC2626; border-color:#FCA5A5;" onclick="liftCatastropheMoratorium('${m.id}')">🔓 Lift Moratorium</button>` : '<span style="font-size:10px; color:#94A3B8;">LIFTED</span>'}
                    </div>
                </div>
            `;
        }).join('');

        container.innerHTML = `
            <div style="display:grid; grid-template-columns: 2fr 1fr; gap:16px;">
                
                <!-- GIS Map Workspace -->
                <div>
                    <div class="gw-panel">
                        <div class="gw-panel-header" style="display:flex; justify-content:space-between; align-items:center;">
                            <span>🗺️ GIS Property Concentration & Catastrophe Risk Map</span>
                            <div class="gw-btn-group">
                                <button class="gw-btn gw-btn-primary" style="padding:2px 10px; font-size:11px;" onclick="toggleMoratoriumModal(true)">⛈️ Declare Moratorium Zone</button>
                            </div>
                        </div>
                        <div class="gw-panel-body" style="padding:0;">
                            <!-- Simulated Map Canvas with Moratorium Visual Overlay -->
                            <div id="gis-map-canvas" style="height:320px; background:#0F172A; border-radius:0 0 6px 6px; position:relative; overflow:hidden; display:flex; align-items:center; justify-content:center; color:#94A3B8;">
                                <div style="position:absolute; top:15px; left:15px; background:rgba(15,23,42,0.95); padding:10px 14px; border-radius:6px; color:#FFFFFF; font-size:12px; border:1px solid #334155; z-index:5;">
                                    <div><strong>National Exposure & Moratorium Summary</strong></div>
                                    <div style="font-size:11px; color:#CBD5E1;">Total In-Force TIV: <strong style="color:#60A5FA;">$63,400,000</strong></div>
                                    <div style="font-size:11px; color:#CBD5E1;">Active Moratorium Holds: <strong style="color:#EF4444;">${activeMoratoriums.length} Active Zones</strong></div>
                                </div>

                                <!-- Simulated Radar / Radar Ring Overlay -->
                                <div style="position:absolute; width:220px; height:220px; border:2px dashed #EF4444; border-radius:50%; background:rgba(239,68,68,0.15); display:flex; align-items:center; justify-content:center; animation: pulse 3s infinite;">
                                    <span style="font-size:11px; color:#FCA5A5; font-weight:700; background:rgba(15,23,42,0.8); padding:2px 6px; border-radius:4px;">🌀 Hurricane Moratorium Buffer Zone</span>
                                </div>

                                <div style="text-align:center; z-index:2;">
                                    <div style="font-size:40px; margin-bottom:4px;">🗺️</div>
                                    <div style="font-size:13px; font-weight:700; color:#F8FAFC;">Interactive GIS Spatial Moratorium Engine</div>
                                    <div style="font-size:11px; color:#94A3B8;">Evaluating 5 Commercial Locations against ${activeMoratoriums.length} active catastrophe zones</div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Moratorium Declaration Drawer / Form Modal -->
                    <div id="moratorium-modal" class="gw-panel" style="margin-top:16px; border-left:4px solid #D97706; display:none;">
                        <div class="gw-panel-header" style="display:flex; justify-content:space-between; align-items:center;">
                            <span>⛈️ Declare New Geographic Catastrophe Moratorium (PolicyHold)</span>
                            <button class="gw-btn" style="padding:2px 8px; font-size:10px;" onclick="toggleMoratoriumModal(false)">✕ Close</button>
                        </div>
                        <div class="gw-panel-body" style="background:#FFFBEB;">
                            <form onsubmit="handleMoratoriumSubmit(event)">
                                <div style="display:grid; grid-template-columns:1fr 1fr; gap:12px; margin-bottom:10px;">
                                    <div>
                                        <label style="font-size:11px; font-weight:700; color:#475569; display:block;">MORATORIUM ZONE NAME</label>
                                        <input type="text" id="moratorium-name" placeholder="e.g. Hurricane Helene FL Gulf Coast" style="width:100%; padding:6px; font-size:12px; border:1px solid #CBD5E1; border-radius:4px;" required>
                                    </div>
                                    <div>
                                        <label style="font-size:11px; font-weight:700; color:#475569; display:block;">CATASTROPHE TYPE</label>
                                        <select id="moratorium-type" style="width:100%; padding:6px; font-size:12px; border:1px solid #CBD5E1; border-radius:4px;">
                                            <option value="Hurricane">🌀 Hurricane / Tropical Storm</option>
                                            <option value="Wildfire">🔥 Wildfire / Brush Fire</option>
                                            <option value="Flood">🌊 Severe Coastal / Riverine Flood</option>
                                            <option value="Tornado">🌪️ Tornado / Convective Storm</option>
                                            <option value="Earthquake">🌋 Earthquake Fault Movement</option>
                                        </select>
                                    </div>
                                </div>
                                <div style="display:grid; grid-template-columns:1fr 1fr 1fr; gap:12px; margin-bottom:12px;">
                                    <div>
                                        <label style="font-size:11px; font-weight:700; color:#475569; display:block;">CENTER LATITUDE</label>
                                        <input type="number" step="0.0001" id="moratorium-lat" value="25.7617" style="width:100%; padding:6px; font-size:12px; border:1px solid #CBD5E1; border-radius:4px;" required>
                                    </div>
                                    <div>
                                        <label style="font-size:11px; font-weight:700; color:#475569; display:block;">CENTER LONGITUDE</label>
                                        <input type="number" step="0.0001" id="moratorium-lng" value="-80.1918" style="width:100%; padding:6px; font-size:12px; border:1px solid #CBD5E1; border-radius:4px;" required>
                                    </div>
                                    <div>
                                        <label style="font-size:11px; font-weight:700; color:#475569; display:block;">RADIUS (MILES)</label>
                                        <input type="number" id="moratorium-radius" value="75" style="width:100%; padding:6px; font-size:12px; border:1px solid #CBD5E1; border-radius:4px;" required>
                                    </div>
                                </div>
                                <div style="display:flex; justify-content:space-between; align-items:center;">
                                    <div style="display:flex; gap:16px; font-size:11px; font-weight:600; color:#1E293B;">
                                        <label><input type="checkbox" id="moratorium-blocks-bind" checked> 🔒 Block Policy Binding</label>
                                        <label><input type="checkbox" id="moratorium-blocks-quote" checked> ⚡ Block Quoting</label>
                                    </div>
                                    <button type="submit" class="gw-btn gw-btn-primary" style="font-size:11px; padding:6px 16px;">⚡ Issue & Enforce PolicyHold</button>
                                </div>
                            </form>
                        </div>
                    </div>

                    <!-- Active Catastrophe Moratorium List -->
                    <div class="gw-panel" style="margin-top:16px; border-left:4px solid #D97706;">
                        <div class="gw-panel-header">
                            <span>⛈️ Declared PolicyHold Catastrophe Moratoriums (${moratoriums.length})</span>
                        </div>
                        <div class="gw-panel-body">
                            ${moratoriumsHtml}
                        </div>
                    </div>

                    <!-- Marketplace Accelerator Card -->
                    <div class="gw-panel" style="margin-top:16px; border-left:4px solid #0284C7;">
                        <div class="gw-panel-header" style="display:flex; justify-content:space-between; align-items:center;">
                            <span>⚡ Guidewire Marketplace Accelerator: HazardHub Risk Intelligence</span>
                            <button class="gw-btn gw-btn-primary" style="font-size:11px; padding:3px 10px;" onclick="runGlobalHazardEnrichment()">⚡ Enrich All Locations</button>
                        </div>
                        <div class="gw-panel-body">
                            <div id="hazardhub-records-list">
                                ${hazardHtml}
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Insured Property Risk Pins -->
                <div>
                    <div class="gw-panel">
                        <div class="gw-panel-header">Insured Locations & Moratorium Status</div>
                        <div class="gw-panel-body" style="max-height:680px; overflow-y:auto;">
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

function toggleMoratoriumModal(show) {
    const modal = document.getElementById('moratorium-modal');
    if (modal) modal.style.display = show ? 'block' : 'none';
}

async function handleMoratoriumSubmit(event) {
    event.preventDefault();
    const name = document.getElementById('moratorium-name').value;
    const type = document.getElementById('moratorium-type').value;
    const lat = parseFloat(document.getElementById('moratorium-lat').value);
    const lng = parseFloat(document.getElementById('moratorium-lng').value);
    const radius = parseFloat(document.getElementById('moratorium-radius').value);
    const blocksBind = document.getElementById('moratorium-blocks-bind').checked;
    const blocksQuote = document.getElementById('moratorium-blocks-quote').checked;

    try {
        const res = await fetch(`${API_BASE}/gis/moratoriums`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                name: name,
                catastropheType: type,
                lat: lat,
                lng: lng,
                radiusMiles: radius,
                blocksBind: blocksBind,
                blocksQuote: blocksQuote,
                createdBy: 'su',
                status: 'ACTIVE'
            })
        });
        if (res.ok) {
            alert('⛈️ Catastrophe Moratorium Declared!\n' + name + '\nRadius: ' + radius + ' miles.\nUnderwriting rules updated to block binding within zone.');
            location.reload();
        } else {
            alert('Failed to declare moratorium.');
        }
    } catch (err) {
        alert('Error: ' + err);
    }
}

async function liftCatastropheMoratorium(id) {
    if (!confirm('Are you sure you want to lift this catastrophe moratorium? Binding restrictions will be removed.')) return;
    try {
        const res = await fetch(`${API_BASE}/gis/moratoriums/${id}/lift`, { method: 'POST' });
        if (res.ok) {
            alert('🔓 Catastrophe Moratorium Lifted. Underwriting restrictions deactivated.');
            location.reload();
        } else {
            alert('Failed to lift moratorium.');
        }
    } catch (err) {
        alert('Error: ' + err);
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
