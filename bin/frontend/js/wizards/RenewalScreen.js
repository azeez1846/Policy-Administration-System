// Emulates Guidewire RenewalScreen.pcf / RenewalWizard.pcf

function showRenewalScreen(jobNumber) {
    const job = jobsList.find(j => j.jobNumber === jobNumber);
    if (!job || !job.policyPeriod) return;

    const period = job.policyPeriod;
    const workArea = document.getElementById('work-area');
    const heading = document.getElementById('page-heading');
    const badge = document.getElementById('job-status-badge');

    heading.innerText = `Policy Renewal - ${job.jobNumber}`;
    badge.innerText = 'RENEWAL QUOTING';
    badge.className = 'gw-badge gw-badge-quoted';

    const nextExp = new Date();
    nextExp.setFullYear(nextExp.getFullYear() + 1);

    workArea.innerHTML = `
        <div class="gw-panel">
            <div class="gw-panel-header">Renewal Term Parameters (RenewalScreen.pcf)</div>
            <div class="gw-panel-body">
                <div class="gw-form-grid">
                    <div class="gw-field">
                        <label>Current Policy Number</label>
                        <input type="text" value="${period.policyNumber || 'CP-8472910'}" readonly>
                    </div>
                    <div class="gw-field">
                        <label>Renewal Term Number</label>
                        <input type="text" value="${(period.termNumber || 1) + 1}" readonly>
                    </div>
                    <div class="gw-field">
                        <label>Renewal Effective Date</label>
                        <input type="date" value="${period.expirationDate || new Date().toISOString().split('T')[0]}">
                    </div>
                    <div class="gw-field">
                        <label>Renewal Expiration Date</label>
                        <input type="date" value="${nextExp.toISOString().split('T')[0]}">
                    </div>
                </div>

                <div style="margin-top:20px; padding:16px; background:#EFF6FF; border-left:4px solid #3B82F6; border-radius:4px;">
                    <h4 style="margin:0 0 8px 0; color:#1E40AF;">Renewal Rating Quote Summary</h4>
                    <p style="margin:0; font-size:13px; color:#1E3A8A;">Prior Term Premium: <strong>$${(period.totalCost || 2400).toLocaleString()}</strong></p>
                    <p style="margin:4px 0 0 0; font-size:14px; color:#1D4ED8; font-weight:700;">Calculated Renewal Premium: <strong>$${((period.totalCost || 2400) * 1.05).toLocaleString(undefined, {minimumFractionDigits: 2})}</strong> (+5% Inflation Adjustment)</p>
                </div>

                <div style="margin-top:24px; display:flex; gap:12px;">
                    <button class="gw-btn gw-btn-primary" onclick="executeBindRenewal('${jobNumber}')">Issue Renewal Policy</button>
                    <button class="gw-btn" onclick="renderPoliciesTab()">Cancel</button>
                </div>
            </div>
        </div>
    `;
}

async function executeBindRenewal(jobNumber) {
    try {
        const res = await fetch(`${API_BASE}/jobs/renew`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ jobNumber: jobNumber })
        });
        if (res.ok) {
            alert('Renewal Policy Issued Successfully!');
            await loadJobs();
            await loadPolicies();
            renderPoliciesTab();
        } else {
            alert('Failed to issue renewal policy');
        }
    } catch (e) {
        alert('Network error renewing policy');
    }
}
