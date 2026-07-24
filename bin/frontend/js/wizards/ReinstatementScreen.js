// Emulates Guidewire ReinstatementScreen.pcf / ReinstatementWizard.pcf

function showReinstatementScreen(jobNumber) {
    const job = jobsList.find(j => j.jobNumber === jobNumber);
    if (!job || !job.policyPeriod) return;

    const period = job.policyPeriod;
    const workArea = document.getElementById('work-area');
    const heading = document.getElementById('page-heading');
    const badge = document.getElementById('job-status-badge');

    heading.innerText = `Policy Reinstatement - ${job.jobNumber}`;
    badge.innerText = 'REINSTATEMENT';
    badge.className = 'gw-badge gw-badge-draft';

    workArea.innerHTML = `
        <div class="gw-panel">
            <div class="gw-panel-header">Reinstatement Authorization (ReinstatementScreen.pcf)</div>
            <div class="gw-panel-body">
                <div class="gw-form-grid">
                    <div class="gw-field">
                        <label>Policy Number</label>
                        <input type="text" value="${period.policyNumber || 'CP-8472910'}" readonly>
                    </div>
                    <div class="gw-field">
                        <label>Current Status</label>
                        <input type="text" value="${period.status}" readonly>
                    </div>
                    <div class="gw-field">
                        <label>Reinstatement Reason</label>
                        <select id="rst-reason">
                            <option>Overdue Premium Payment Received</option>
                            <option>Underwriting Appeal Approved</option>
                            <option>Correction of Administrative Error</option>
                        </select>
                    </div>
                    <div class="gw-field">
                        <label>Underwriter Authorization</label>
                        <input type="text" value="su (Super User / Senior UW)" readonly>
                    </div>
                </div>

                <div style="margin-top:20px; padding:16px; background:#F0FDF4; border-left:4px solid #22C55E; border-radius:4px;">
                    <h4 style="margin:0 0 8px 0; color:#166534;">Reinstatement Effect</h4>
                    <p style="margin:0; font-size:13px; color:#14532D;">Reinstating this policy will restore in-force coverage retroactively without lapse in coverage.</p>
                </div>

                <div style="margin-top:24px; display:flex; gap:12px;">
                    <button class="gw-btn gw-btn-success" onclick="executeReinstatePolicy('${jobNumber}')">Reinstate Policy Coverage</button>
                    <button class="gw-btn" onclick="renderPoliciesTab()">Cancel</button>
                </div>
            </div>
        </div>
    `;
}

async function executeReinstatePolicy(jobNumber) {
    try {
        const res = await fetch(`${API_BASE}/jobs/reinstate`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ jobNumber: jobNumber })
        });
        if (res.ok) {
            alert('Policy Reinstated to Active Bound Status!');
            await loadJobs();
            await loadPolicies();
            renderPoliciesTab();
        } else {
            alert('Failed to reinstate policy');
        }
    } catch (e) {
        alert('Network error reinstating policy');
    }
}
