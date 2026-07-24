// Emulates Guidewire ReinsuranceLV.pcf / Reinsurance Treaty & Cession Engine

function renderReinsuranceScreen(container, grossPremium, buildingLimit) {
    const prem = grossPremium || 2400.00;
    const limit = buildingLimit || 1200000.00;

    container.innerHTML = `
        <div style="margin-bottom:20px; display:flex; justify-content:space-between; align-items:center;">
            <div>
                <h2 style="margin:0; font-size:22px; color:#0f172a; font-weight:700;">Reinsurance Management & Risk Cession (ReinsuranceLV.pcf)</h2>
                <p style="margin:4px 0 0 0; color:#64748b; font-size:13px;">Manage Quota Share (QS), Excess of Loss (XOL), and Facultative treaty attachments, ceded premium, and carrier net retention.</p>
            </div>
            <div style="display:flex; gap:10px;">
                <button class="gw-btn gw-btn-primary" onclick="toggleTreatyModal(true)">🛡️ Add Reinsurance Treaty</button>
                <button class="gw-btn" onclick="loadReinsuranceData(${prem}, ${limit})">🔄 Recalculate Risk Cession</button>
            </div>
        </div>

        <!-- Reinsurance Modal -->
        <div id="treaty-modal" class="gw-panel" style="margin-bottom:20px; border-left:4px solid #2563EB; display:none;">
            <div class="gw-panel-header" style="display:flex; justify-content:space-between; align-items:center;">
                <span>🛡️ Register New Reinsurance Treaty / Facultative Agreement</span>
                <button class="gw-btn" style="padding:2px 8px; font-size:10px;" onclick="toggleTreatyModal(false)">✕ Close</button>
            </div>
            <div class="gw-panel-body" style="background:#F8FAFC;">
                <form onsubmit="handleTreatySubmit(event, ${prem}, ${limit})">
                    <div style="display:grid; grid-template-columns: 1fr 1fr 1fr; gap:12px; margin-bottom:12px;">
                        <div>
                            <label style="font-size:11px; font-weight:700; color:#475569; display:block; margin-bottom:4px;">TREATY PROGRAM NAME</label>
                            <input type="text" id="treaty-name" placeholder="e.g. 2026 Commercial Auto Quota Share" style="width:100%; padding:6px; font-size:12px; border:1px solid #CBD5E1; border-radius:4px;" required>
                        </div>
                        <div>
                            <label style="font-size:11px; font-weight:700; color:#475569; display:block; margin-bottom:4px;">TREATY TYPE</label>
                            <select id="treaty-type" style="width:100%; padding:6px; font-size:12px; border:1px solid #CBD5E1; border-radius:4px;">
                                <option value="QuotaShare">Quota Share (Proportional)</option>
                                <option value="ExcessOfLoss">Excess of Loss (XOL Non-Proportional)</option>
                                <option value="Facultative">Facultative Risk Specific</option>
                            </select>
                        </div>
                        <div>
                            <label style="font-size:11px; font-weight:700; color:#475569; display:block; margin-bottom:4px;">REINSURER / SYNDICATE</label>
                            <input type="text" id="treaty-reinsurer" placeholder="e.g. Swiss Re / Munich Re / Lloyd's" style="width:100%; padding:6px; font-size:12px; border:1px solid #CBD5E1; border-radius:4px;" required>
                        </div>
                    </div>
                    <div style="display:grid; grid-template-columns: 1fr 1fr; gap:12px; margin-bottom:12px;">
                        <div>
                            <label style="font-size:11px; font-weight:700; color:#475569; display:block; margin-bottom:4px;">CEDED CESSION SHARE (%)</label>
                            <input type="number" step="0.1" id="treaty-share" placeholder="e.g. 25.0" style="width:100%; padding:6px; font-size:12px; border:1px solid #CBD5E1; border-radius:4px;" required>
                        </div>
                        <div>
                            <label style="font-size:11px; font-weight:700; color:#475569; display:block; margin-bottom:4px;">ATTACHMENT POINT ($ THRESHOLD)</label>
                            <input type="number" id="treaty-attachment" placeholder="e.g. 1000000" style="width:100%; padding:6px; font-size:12px; border:1px solid #CBD5E1; border-radius:4px;" required>
                        </div>
                    </div>
                    <div style="text-align:right;">
                        <button type="submit" class="gw-btn gw-btn-primary" style="font-size:12px; padding:6px 16px;">⚡ Attach Treaty & Re-rate Portfolio</button>
                    </div>
                </form>
            </div>
        </div>

        <div id="reinsurance-summary-cards">Loading cession calculations...</div>

        <!-- Layer Stack Visualizer -->
        <div class="gw-panel" style="margin-top:20px;">
            <div class="gw-panel-header">
                <span>📊 Reinsurance Layer Attachment Stack & Retention Breakdown</span>
            </div>
            <div class="gw-panel-body" id="reinsurance-layer-stack">
                Loading layer breakdown...
            </div>
        </div>

        <div class="gw-panel" style="margin-top:20px;">
            <div class="gw-panel-header">
                <span>Active Reinsurance Treaty Attachments (pc_reinsurancetreaty)</span>
                <span class="gw-badge gw-badge-bound">SPRING BOOT REINSURANCE ENGINE</span>
            </div>
            <div class="gw-panel-body">
                <div id="reinsurance-treaties-table">Loading treaties...</div>
            </div>
        </div>
    `;

    loadReinsuranceData(prem, limit);
}

function toggleTreatyModal(show) {
    const modal = document.getElementById('treaty-modal');
    if (modal) modal.style.display = show ? 'block' : 'none';
}

async function handleTreatySubmit(event, prem, limit) {
    event.preventDefault();
    const name = document.getElementById('treaty-name').value;
    const type = document.getElementById('treaty-type').value;
    const reinsurer = document.getElementById('treaty-reinsurer').value;
    const share = parseFloat(document.getElementById('treaty-share').value);
    const attachment = parseFloat(document.getElementById('treaty-attachment').value);

    try {
        const res = await fetch(`${API_BASE}/reinsurance/treaties`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                treatyName: name,
                treatyType: type,
                reinsurerName: reinsurer,
                cededPercentage: share,
                attachmentPoint: attachment
            })
        });
        if (res.ok) {
            alert('🛡️ Reinsurance Treaty Attached Successfully!\n' + name + ' (' + share + '% Cession)');
            toggleTreatyModal(false);
            loadReinsuranceData(prem, limit);
        } else {
            alert('Failed to register treaty.');
        }
    } catch (err) {
        alert('Error: ' + err);
    }
}

async function loadReinsuranceData(prem, limit) {
    const cardsContainer = document.getElementById('reinsurance-summary-cards');
    const tableContainer = document.getElementById('reinsurance-treaties-table');
    const layerContainer = document.getElementById('reinsurance-layer-stack');
    if (!cardsContainer || !tableContainer) return;

    try {
        const res = await fetch(`${API_BASE}/reinsurance/calculate-cession`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ grossPremium: prem, buildingLimit: limit })
        });

        if (res.ok) {
            const data = await res.json();
            cardsContainer.innerHTML = `
                <div style="display:grid; grid-template-columns: repeat(3, 1fr); gap:16px;">
                    <div style="background:#F8FAFC; border:1px solid #E2E8F0; padding:16px; border-radius:8px;">
                        <span style="font-size:12px; color:#64748b; font-weight:600;">GROSS DIRECT PREMIUM</span>
                        <div style="font-size:24px; font-weight:800; color:#0f172a; margin-top:4px;">$${data.grossPremium.toLocaleString(undefined, {minimumFractionDigits:2})}</div>
                        <span style="font-size:11px; color:#475569;">Building Exposure: $${data.buildingLimit.toLocaleString()}</span>
                    </div>
                    <div style="background:#EFF6FF; border:1px solid #BFDBFE; padding:16px; border-radius:8px;">
                        <span style="font-size:12px; color:#1D4ED8; font-weight:600;">CEDED REINSURANCE PREMIUM (${data.totalCededPercentage}%)</span>
                        <div style="font-size:24px; font-weight:800; color:#1E40AF; margin-top:4px;">$${data.cededPremium.toLocaleString(undefined, {minimumFractionDigits:2})}</div>
                        <span class="gw-badge gw-badge-bound">CEDED TO REINSURERS</span>
                    </div>
                    <div style="background:#F0FDF4; border:1px solid #BBF7D0; padding:16px; border-radius:8px;">
                        <span style="font-size:12px; color:#15803D; font-weight:600;">NET RETAINED CARRIER PREMIUM (${data.netRetentionPercentage || (100 - data.totalCededPercentage)}%)</span>
                        <div style="font-size:24px; font-weight:800; color:#166534; margin-top:4px;">$${data.netRetainedPremium.toLocaleString(undefined, {minimumFractionDigits:2})}</div>
                        <span class="gw-badge gw-badge-bound" style="background:#86EFAC; color:#14532D;">CARRIER NET EXPOSURE</span>
                    </div>
                </div>
            `;

            // Layer stack visual progress bar
            let layersHtml = (data.layerStack || []).map(l => `
                <div style="display:flex; justify-content:space-between; align-items:center; background:#F1F5F9; padding:8px 12px; border-radius:6px; margin-bottom:6px; font-size:12px;">
                    <div>
                        <strong style="color:#1E293B;">${l.treatyName}</strong>
                        <span style="font-size:11px; color:#64748B; margin-left:8px;">(${l.treatyType} • ${l.reinsurerName})</span>
                    </div>
                    <div style="display:flex; align-items:center; gap:12px;">
                        <span style="color:#2563EB; font-weight:700;">${l.cededPercentage}% Ceded</span>
                        <span style="color:#0F172A; font-weight:800;">$${l.layerCededPremium.toLocaleString(undefined, {minimumFractionDigits:2})}</span>
                    </div>
                </div>
            `).join('');

            layerContainer.innerHTML = `
                <div style="margin-bottom:12px;">
                    <div style="display:flex; height:24px; border-radius:6px; overflow:hidden; border:1px solid #CBD5E1;">
                        <div style="width:${data.netRetentionPercentage || (100 - data.totalCededPercentage)}%; background:#166534; color:#FFFFFF; font-size:10px; font-weight:700; display:flex; align-items:center; justify-content:center;">
                            Net Retained (${data.netRetentionPercentage || (100 - data.totalCededPercentage)}%)
                        </div>
                        <div style="width:${data.totalCededPercentage}%; background:#2563EB; color:#FFFFFF; font-size:10px; font-weight:700; display:flex; align-items:center; justify-content:center;">
                            Ceded Reinsurance (${data.totalCededPercentage}%)
                        </div>
                    </div>
                </div>
                ${layersHtml}
            `;

            let rows = (data.treaties || []).map(t => `
                <tr>
                    <td><strong>${t.treatyName}</strong></td>
                    <td><span class="gw-badge gw-badge-draft">${t.treatyType}</span></td>
                    <td><strong>${t.reinsurerName}</strong></td>
                    <td><strong style="color:#2563eb;">${t.cededPercentage}%</strong></td>
                    <td>$${t.attachmentPoint.toLocaleString()}</td>
                </tr>
            `).join('');

            tableContainer.innerHTML = `
                <table class="gw-table">
                    <thead>
                        <tr>
                            <th>Treaty Name</th>
                            <th>Treaty Type</th>
                            <th>Reinsurer / Syndicate Name</th>
                            <th>Ceded Cession %</th>
                            <th>Attachment Point ($)</th>
                        </tr>
                    </thead>
                    <tbody>${rows}</tbody>
                </table>
            `;
        }
    } catch (e) {
        cardsContainer.innerHTML = `<div style="color:#dc2626; padding:12px;">Failed to calculate reinsurance cessions.</div>`;
    }
}
