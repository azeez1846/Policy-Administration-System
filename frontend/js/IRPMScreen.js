// Guidewire PolicyCenter - Schedule Rating Modification (IRPM) & Factor Override Studio UI

async function renderIRPMScreen(container) {
    container.innerHTML = `<div style="padding:20px; color:#64748B;">Loading Schedule Rating Modification (IRPM) Worksheets...</div>`;

    try {
        const res = await fetch(`${API_BASE}/rating/irpm`);
        const data = res.ok ? await res.json() : {
            basePremium: 2400.0,
            appliedIRPMPct: -15.0,
            modifiedPremium: 2040.0,
            categories: []
        };

        let catRows = (data.categories || []).map(c => `
            <tr>
                <td style="font-weight:700; color:#0F172A;">${c.category}</td>
                <td>${c.maxCredit}% / +${c.maxDebit}%</td>
                <td style="font-weight:700; color:${c.applied < 0 ? '#166534' : '#DC2626'};">${c.applied}%</td>
                <td style="font-size:11px; color:#64748B;">${c.reason}</td>
            </tr>
        `).join('');

        container.innerHTML = `
            <div style="display:grid; grid-template-columns: 2fr 1fr; gap:16px;">
                
                <!-- Schedule Rating Matrix -->
                <div>
                    <div class="gw-panel">
                        <div class="gw-panel-header">Schedule Rating Modification Categories (IRPM)</div>
                        <div class="gw-panel-body" style="padding:0;">
                            <table class="gw-table">
                                <thead>
                                    <tr>
                                        <th>Risk Category</th>
                                        <th>Credit / Debit Range</th>
                                        <th>Applied Factor</th>
                                        <th>Underwriting Justification</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    ${catRows}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <!-- Premium Modification Calculation Card -->
                <div>
                    <div class="gw-panel">
                        <div class="gw-panel-header">IRPM Premium Impact Summary</div>
                        <div class="gw-panel-body" style="text-align:center;">
                            <div style="font-size:12px; color:#64748B;">Unmodified Base Manual Premium</div>
                            <div style="font-size:20px; font-weight:700; color:#475569;">$${data.basePremium.toLocaleString()}</div>

                            <div style="margin:16px 0; padding:12px; background:#F8FAFC; border:1px solid #CBD5E1; border-radius:6px;">
                                <div style="font-size:11px; font-weight:700; color:#64748B;">Total Schedule IRPM Factor</div>
                                <div style="font-size:28px; font-weight:800; color:#166534;" id="irpm-factor-display">${data.appliedIRPMPct}%</div>
                            </div>

                            <div style="font-size:12px; color:#64748B;">Final Schedule Modified Premium</div>
                            <div style="font-size:26px; font-weight:800; color:#1E40AF;" id="irpm-modified-premium">$${data.modifiedPremium.toLocaleString()}</div>

                            <div style="margin-top:20px;">
                                <button class="gw-btn gw-btn-primary" style="width:100%; padding:8px;" onclick="promptIRPMOverride()">✏️ Override Schedule Factor</button>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        `;
    } catch (e) {
        container.innerHTML = `<div style="color:red; padding:20px;">Error loading IRPM worksheets: ${e}</div>`;
    }
}

async function promptIRPMOverride() {
    const val = prompt("Enter total Schedule IRPM modification percentage (-25% to +25%):", "-15.0");
    if (val !== null) {
        const num = parseFloat(val);
        if (!isNaN(num)) {
            const res = await fetch(`${API_BASE}/rating/irpm`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ irpmPct: num })
            });
            const data = await res.json();
            document.getElementById('irpm-factor-display').innerText = `${data.appliedIRPMPct}%`;
            document.getElementById('irpm-modified-premium').innerText = `$${data.modifiedPremium.toLocaleString()}`;
            alert(data.auditLog);
        }
    }
}
