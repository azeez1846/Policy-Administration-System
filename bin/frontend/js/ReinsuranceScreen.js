// Emulates Guidewire ReinsuranceLV.pcf / Reinsurance Treaty & Cession Engine

function renderReinsuranceScreen(container, grossPremium, buildingLimit) {
    const prem = grossPremium || 2400.00;
    const limit = buildingLimit || 1200000.00;

    container.innerHTML = `
        <div style="margin-bottom:20px; display:flex; justify-content:space-between; align-items:center;">
            <div>
                <h2 style="margin:0; font-size:22px; color:#0f172a; font-weight:700;">Reinsurance Management & Risk Cession (ReinsuranceLV.pcf)</h2>
                <p style="margin:4px 0 0 0; color:#64748b; font-size:13px;">Manage Quota Share (QS) & Excess of Loss (XOL) treaty attachments, ceded premium, and net retention.</p>
            </div>
            <button class="gw-btn gw-btn-primary" onclick="loadReinsuranceData(${prem}, ${limit})">🔄 Recalculate Risk Cession</button>
        </div>

        <div id="reinsurance-summary-cards">Loading cession calculations...</div>

        <div class="gw-panel" style="margin-top:20px;">
            <div class="gw-panel-header">
                <span>Active Reinsurance Treaty Attachments (ReinsuranceLV.pcf)</span>
                <span class="gw-badge gw-badge-bound">SPRING BOOT REINSURANCE ENGINE</span>
            </div>
            <div class="gw-panel-body">
                <div id="reinsurance-treaties-table">Loading treaties...</div>
            </div>
        </div>
    `;

    loadReinsuranceData(prem, limit);
}

async function loadReinsuranceData(prem, limit) {
    const cardsContainer = document.getElementById('reinsurance-summary-cards');
    const tableContainer = document.getElementById('reinsurance-treaties-table');
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
                        <span style="font-size:12px; color:#15803D; font-weight:600;">NET RETAINED CARRIER PREMIUM</span>
                        <div style="font-size:24px; font-weight:800; color:#166534; margin-top:4px;">$${data.netRetainedPremium.toLocaleString(undefined, {minimumFractionDigits:2})}</div>
                        <span class="gw-badge gw-badge-bound" style="background:#86EFAC; color:#14532D;">CARRIER NET EXPOSURE</span>
                    </div>
                </div>
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
