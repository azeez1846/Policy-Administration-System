// Emulates Guidewire CancelPolicyScreen.pcf / CancellationWizard.pcf

function showCancellationScreen(jobNumber) {
    const job = jobsList.find(j => j.jobNumber === jobNumber);
    if (!job || !job.policyPeriod) return;

    const period = job.policyPeriod;
    const workArea = document.getElementById('work-area');
    const heading = document.getElementById('page-heading');
    const badge = document.getElementById('job-status-badge');

    heading.innerText = `Policy Cancellation - ${job.jobNumber}`;
    badge.innerText = 'CANCELLATION DRAFT';
    badge.className = 'gw-badge gw-badge-draft';

    workArea.innerHTML = `
        <div class="gw-panel">
            <div class="gw-panel-header">Cancellation Parameters (CancelPolicyScreen.pcf)</div>
            <div class="gw-panel-body">
                <div class="gw-form-grid">
                    <div class="gw-field">
                        <label>Policy Number</label>
                        <input type="text" value="${period.policyNumber || 'CP-8472910'}" readonly>
                    </div>
                    <div class="gw-field">
                        <label>Cancellation Calculation Method</label>
                        <select id="cnc-method">
                            <option value="ProRata">Pro-Rata (Proportional refund based on unearned term)</option>
                            <option value="Flat">Flat (100% full refund from inception)</option>
                            <option value="ShortRate">Short-Rate (Pro-rata minus short-rate penalty)</option>
                        </select>
                    </div>
                    <div class="gw-field">
                        <label>Effective Date of Cancellation</label>
                        <input type="date" id="cnc-date" value="${new Date().toISOString().split('T')[0]}">
                    </div>
                    <div class="gw-field">
                        <label>Reason Code</label>
                        <select id="cnc-reason">
                            <option value="Customer Request - Sold Premises">Customer Request - Sold Premises</option>
                            <option value="Non-Payment of Premium">Non-Payment of Premium</option>
                            <option value="Underwriting Risk Increase">Underwriting Risk Increase</option>
                            <option value="Rewritten with another carrier">Rewritten with another carrier</option>
                        </select>
                    </div>
                </div>

                <div style="margin-top:20px; padding:16px; background:#FEF2F2; border-left:4px solid #EF4444; border-radius:4px;">
                    <h4 style="margin:0 0 8px 0; color:#991B1B;">Cancellation Return Premium Summary</h4>
                    <p style="margin:0; font-size:13px; color:#7F1D1D;">Original Written Premium: <strong>$${(period.totalCost || 2400).toLocaleString()}</strong></p>
                    <p style="margin:4px 0 0 0; font-size:14px; color:#DC2626; font-weight:700;">Estimated Return Premium: <strong>-$${((period.totalCost || 2400) * 0.5).toLocaleString(undefined, {minimumFractionDigits: 2})}</strong></p>
                </div>

                <div style="margin-top:24px; display:flex; gap:12px;">
                    <button class="gw-btn" style="background:#EF4444; color:#fff;" onclick="executeCancelPolicy('${jobNumber}')">Confirm Cancellation</button>
                    <button class="gw-btn" onclick="renderPoliciesTab()">Cancel</button>
                </div>
            </div>
        </div>
    `;
}

async function executeCancelPolicy(jobNumber) {
    const method = document.getElementById('cnc-method').value;
    const reason = document.getElementById('cnc-reason').value;

    try {
        const res = await fetch(`${API_BASE}/jobs/cancel`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ jobNumber: jobNumber, cancelType: method, reason: reason })
        });
        if (res.ok) {
            alert('Policy Cancelled & Return Premium Recorded!');
            await loadJobs();
            await loadPolicies();
            renderPoliciesTab();
        } else {
            alert('Failed to cancel policy');
        }
    } catch (e) {
        alert('Network error cancelling policy');
    }
}
