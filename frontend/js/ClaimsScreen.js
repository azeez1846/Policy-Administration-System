// Guidewire PolicyCenter - ClaimCenter Loss Runs & Reserves Integration UI

async function renderClaimsScreen(container) {
    container.innerHTML = `<div style="padding:20px; color:#64748B;">Connecting to Guidewire ClaimCenter Loss Database...</div>`;

    try {
        const res = await fetch(`${API_BASE}/claims/loss-history`);
        const claims = res.ok ? await res.json() : [];

        let rows = claims.map(c => `
            <tr>
                <td style="color:#2563EB; font-weight:700;">${c.claimNumber}</td>
                <td>${c.policyNumber}</td>
                <td>${c.lossDate}</td>
                <td>${c.lossCause}</td>
                <td><span class="gw-badge ${c.status === 'Closed' ? 'gw-badge-bound' : 'gw-badge-draft'}">${c.status}</span></td>
                <td style="font-weight:700;">$${c.paidAmount.toLocaleString()}</td>
                <td style="font-weight:700; color:#EA580C;">$${c.reserveAmount.toLocaleString()}</td>
            </tr>
        `).join('');

        container.innerHTML = `
            <div class="gw-panel">
                <div class="gw-panel-header" style="display:flex; justify-content:space-between; align-items:center;">
                    <span>ClaimCenter Loss History & Open Reserves</span>
                    <button class="gw-btn gw-btn-primary" style="font-size:11px;" onclick="window.open('${API_BASE}/documents/dec-page', '_blank')">Export Loss Run PDF</button>
                </div>
                <div class="gw-panel-body" style="padding:0;">
                    <table class="gw-table">
                        <thead>
                            <tr>
                                <th>Claim #</th>
                                <th>Policy #</th>
                                <th>Loss Date</th>
                                <th>Loss Cause</th>
                                <th>Status</th>
                                <th>Paid Amount</th>
                                <th>Open Reserve</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${rows}
                        </tbody>
                    </table>
                </div>
            </div>
        `;
    } catch (e) {
        container.innerHTML = `<div style="color:red; padding:20px;">Error loading claims: ${e}</div>`;
    }
}
