// Guidewire PolicyCenter - IoT Telematics & Commercial Fleet Live Risk Streamer UI

async function renderTelematicsScreen(container) {
    container.innerHTML = `<div style="padding:20px; color:#64748B;">Loading IoT Commercial Fleet Telematics Data...</div>`;
    loadTelematicsDashboard(container);
}

async function loadTelematicsDashboard(container) {
    try {
        const res = await fetch(`${API_BASE}/telematics/fleet`);
        if (!res.ok) throw new Error("Failed to fetch fleet analytics");

        const fleet = await res.json();
        const readings = fleet.latestReadings || [];

        let cardsHtml = `
            <div style="display:grid; grid-template-columns: repeat(4, 1fr); gap:16px; margin-bottom:20px;">
                <div style="background:#FFFFFF; border:1px solid #CBD5E1; border-radius:8px; padding:16px; border-left:4px solid #2563EB;">
                    <span style="font-size:11px; font-weight:700; color:#64748B;">MONITORED FLEET VEHICLES</span>
                    <div style="font-size:26px; font-weight:800; color:#0F172A; margin-top:4px;">${fleet.monitoredVehiclesCount} Active Trucks</div>
                    <span style="font-size:11px; color:#2563EB;">📡 100% Real-Time GPS Signal</span>
                </div>
                <div style="background:#FFFFFF; border:1px solid #CBD5E1; border-radius:8px; padding:16px; border-left:4px solid ${fleet.averageFleetSafetyScore >= 85 ? '#16A34A' : '#EAB308'};">
                    <span style="font-size:11px; font-weight:700; color:#64748B;">AVERAGE FLEET SAFETY SCORE</span>
                    <div style="font-size:26px; font-weight:800; color:${fleet.averageFleetSafetyScore >= 85 ? '#16A34A' : '#D97706'}; margin-top:4px;">${fleet.averageFleetSafetyScore} / 100</div>
                    <span class="gw-badge ${fleet.averageFleetSafetyScore >= 85 ? 'gw-badge-bound' : 'gw-badge-quote'}">${fleet.statusRecommendation}</span>
                </div>
                <div style="background:#FFFFFF; border:1px solid #CBD5E1; border-radius:8px; padding:16px; border-left:4px solid #DC2626;">
                    <span style="font-size:11px; font-weight:700; color:#64748B;">HARD BRAKE EVENTS (24H)</span>
                    <div style="font-size:26px; font-weight:800; color:#DC2626; margin-top:4px;">${fleet.totalHardBrakeEvents} Incidents</div>
                    <span style="font-size:11px; color:#475569;">Rapid Deceleration Threshold >0.4g</span>
                </div>
                <div style="background:#FFFFFF; border:1px solid #CBD5E1; border-radius:8px; padding:16px; border-left:4px solid #7C3AED;">
                    <span style="font-size:11px; font-weight:700; color:#64748B;">RAPID ACCELERATION PINGS</span>
                    <div style="font-size:26px; font-weight:800; color:#7C3AED; margin-top:4px;">${fleet.totalRapidAccelerationEvents} Incidents</div>
                    <span style="font-size:11px; color:#64748B;">Aggressive Acceleration >0.35g</span>
                </div>
            </div>
        `;

        let rowsHtml = readings.map(r => {
            let gradeBadge = r.riskGrade === 'EXCELLENT' ? 'background:#DCFCE7; color:#15803D;' : (r.riskGrade === 'LOW_RISK' ? 'background:#DBEAFE; color:#1E40AF;' : 'background:#FEE2E2; color:#B91C1C;');
            let factorStr = r.premiumAdjustmentFactor < 0 ? `${(r.premiumAdjustmentFactor * 100).toFixed(0)}% CREDIT` : (r.premiumAdjustmentFactor > 0 ? `+${(r.premiumAdjustmentFactor * 100).toFixed(0)}% SURCHARGE` : '0% DEFAULT');

            return `
                <tr style="border-bottom:1px solid #F1F5F9;">
                    <td style="padding:10px;"><strong style="font-size:12px; color:#0F172A;">${r.vehicleName}</strong><br><span style="font-size:10px; color:#64748B; font-family:monospace;">VIN: ${r.vehicleVin}</span></td>
                    <td style="padding:10px; font-size:12px; font-weight:700; color:#334155;">👤 ${r.driverName}</td>
                    <td style="padding:10px; font-size:12px; font-family:monospace; color:#2563EB;">${r.speedMph} MPH</td>
                    <td style="padding:10px; font-size:12px; font-weight:700; color:${r.hardBrakeEvents > 0 ? '#DC2626' : '#16A34A'};">${r.hardBrakeEvents} Stops</td>
                    <td style="padding:10px; font-size:12px; font-weight:700; color:${r.rapidAccelerationEvents > 0 ? '#D97706' : '#16A34A'};">${r.rapidAccelerationEvents} Acceleration</td>
                    <td style="padding:10px; font-size:13px; font-weight:800; color:${r.safetyScore >= 85 ? '#16A34A' : '#DC2626'};">${r.safetyScore}/100</td>
                    <td style="padding:10px;"><span class="gw-badge" style="${gradeBadge}">${r.riskGrade}</span></td>
                    <td style="padding:10px; font-size:12px; font-weight:800; color:${r.premiumAdjustmentFactor <= 0 ? '#15803D' : '#DC2626'};">${factorStr}</td>
                </tr>
            `;
        }).join('');

        container.innerHTML = `
            <div style="margin-bottom:20px; display:flex; justify-content:space-between; align-items:center;">
                <div>
                    <h2 style="margin:0; font-size:22px; color:#0f172a; font-weight:700;">📡 IoT Telematics & Commercial Fleet Live Risk Streamer</h2>
                    <p style="margin:4px 0 0 0; color:#64748b; font-size:13px;">Real-Time Vehicle Telemetry Ingestion, Driver Safety Scorecards & Monthly Premium Adjustments.</p>
                </div>
                <button class="gw-btn gw-btn-primary" onclick="toggleTelemetryModal(true)">📡 Simulate Telemetry Ping</button>
            </div>

            ${cardsHtml}

            <!-- Simulation Modal -->
            <div id="telemetry-modal" class="gw-panel" style="margin-bottom:20px; border-left:4px solid #7C3AED; display:none;">
                <div class="gw-panel-header" style="display:flex; justify-content:space-between; align-items:center;">
                    <span>📡 Simulate Connected Vehicle IoT Telemetry Sensor Ping</span>
                    <button class="gw-btn" style="padding:2px 8px; font-size:10px;" onclick="toggleTelemetryModal(false)">✕ Close</button>
                </div>
                <div class="gw-panel-body" style="background:#F8FAFC;">
                    <form onsubmit="handleTelemetrySubmit(event)">
                        <div style="display:grid; grid-template-columns: 1fr 1fr 1fr; gap:12px; margin-bottom:12px;">
                            <div>
                                <label style="font-size:11px; font-weight:700; color:#475569; display:block; margin-bottom:4px;">VEHICLE VIN</label>
                                <select id="tel-vin" style="width:100%; padding:6px; font-size:12px; border:1px solid #CBD5E1; border-radius:4px;">
                                    <option value="1FTFW1E84MKD90182">Ford F-350 Heavy Hauler #1 (1FTFW1E84MKD90182)</option>
                                    <option value="2C4RC1BG4KR612948">Freightliner Cascadia #4 (2C4RC1BG4KR612948)</option>
                                    <option value="3FA6P0H78HR394821">Kenworth T680 Sleeper #2 (3FA6P0H78HR394821)</option>
                                </select>
                            </div>
                            <div>
                                <label style="font-size:11px; font-weight:700; color:#475569; display:block; margin-bottom:4px;">DRIVER NAME</label>
                                <input type="text" id="tel-driver" value="Marcus Vance" style="width:100%; padding:6px; font-size:12px; border:1px solid #CBD5E1; border-radius:4px;" required>
                            </div>
                            <div>
                                <label style="font-size:11px; font-weight:700; color:#475569; display:block; margin-bottom:4px;">SPEED (MPH)</label>
                                <input type="number" step="0.1" id="tel-speed" value="65.0" style="width:100%; padding:6px; font-size:12px; border:1px solid #CBD5E1; border-radius:4px;" required>
                            </div>
                        </div>
                        <div style="display:grid; grid-template-columns: 1fr 1fr 1fr; gap:12px; margin-bottom:12px;">
                            <div>
                                <label style="font-size:11px; font-weight:700; color:#475569; display:block; margin-bottom:4px;">HARD BRAKING EVENTS</label>
                                <input type="number" id="tel-brakes" value="0" style="width:100%; padding:6px; font-size:12px; border:1px solid #CBD5E1; border-radius:4px;" required>
                            </div>
                            <div>
                                <label style="font-size:11px; font-weight:700; color:#475569; display:block; margin-bottom:4px;">RAPID ACCELERATION PINGS</label>
                                <input type="number" id="tel-accel" value="0" style="width:100%; padding:6px; font-size:12px; border:1px solid #CBD5E1; border-radius:4px;" required>
                            </div>
                            <div>
                                <label style="font-size:11px; font-weight:700; color:#475569; display:block; margin-bottom:4px;">NIGHT DRIVING (MINS)</label>
                                <input type="number" id="tel-night" value="10" style="width:100%; padding:6px; font-size:12px; border:1px solid #CBD5E1; border-radius:4px;" required>
                            </div>
                        </div>
                        <div style="text-align:right;">
                            <button type="submit" class="gw-btn gw-btn-primary" style="font-size:12px; padding:6px 16px;">📡 Stream Ping & Recalculate Fleet Score</button>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Vehicle Telematics Table -->
            <div class="gw-panel">
                <div class="gw-panel-header">
                    <span>Active Commercial Fleet Vehicle Telematics Stream (pc_policyvehicle / pc_policydriver)</span>
                    <span class="gw-badge gw-badge-bound">SPRING BOOT TELEMATICS ENGINE</span>
                </div>
                <div class="gw-panel-body">
                    <table class="gw-table" style="width:100%;">
                        <thead>
                            <tr style="background:#F1F5F9; font-size:11px;">
                                <th>Vehicle & VIN</th>
                                <th>Assigned Driver</th>
                                <th>Speed (MPH)</th>
                                <th>Hard Brakes</th>
                                <th>Rapid Accel</th>
                                <th>Safety Score</th>
                                <th>Risk Grade</th>
                                <th>Premium Adjustment</th>
                            </tr>
                        </thead>
                        <tbody>${rowsHtml}</tbody>
                    </table>
                </div>
            </div>
        `;
    } catch (e) {
        container.innerHTML = `<div style="color:#DC2626; padding:20px;">Failed to load telematics data: ${e}</div>`;
    }
}

function toggleTelemetryModal(show) {
    const modal = document.getElementById('telemetry-modal');
    if (modal) modal.style.display = show ? 'block' : 'none';
}

async function handleTelemetrySubmit(event) {
    event.preventDefault();
    const vin = document.getElementById('tel-vin').value;
    const driver = document.getElementById('tel-driver').value;
    const speed = parseFloat(document.getElementById('tel-speed').value);
    const brakes = parseInt(document.getElementById('tel-brakes').value);
    const accel = parseInt(document.getElementById('tel-accel').value);
    const night = parseInt(document.getElementById('tel-night').value);

    let vehicleName = vin.includes('1FTFW1E84') ? 'Ford F-350 Heavy Hauler #1' : (vin.includes('2C4RC1BG4') ? 'Freightliner Cascadia #4' : 'Kenworth T680 Sleeper #2');

    try {
        const res = await fetch(`${API_BASE}/telematics/stream`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                vehicleVin: vin,
                vehicleName: vehicleName,
                driverName: driver,
                speedMph: speed,
                hardBrakeEvents: brakes,
                rapidAccelerationEvents: accel,
                nightDrivingMinutes: night
            })
        });
        if (res.ok) {
            const data = await res.json();
            alert('📡 Telemetry Sensor Ping Ingested!\nVehicle: ' + data.vehicleName + '\nSafety Score: ' + data.safetyScore + '/100 (' + data.riskGrade + ')\nAdjustment Factor: ' + (data.premiumAdjustmentFactor * 100).toFixed(0) + '%');
            toggleTelemetryModal(false);
            const mainContainer = document.querySelector('.gw-main');
            renderTelematicsScreen(mainContainer);
        }
    } catch (err) {
        alert('Error: ' + err);
    }
}
