// Emulates Guidewire PolicyChangeScreen.pcf / PolicyChangeWizard.pcf

function showPolicyChangeScreen(jobNumber) {
    const job = jobsList.find(j => j.jobNumber === jobNumber);
    if (!job || !job.policyPeriod) return;

    const period = job.policyPeriod;
    const workArea = document.getElementById('work-area');
    const heading = document.getElementById('page-heading');
    const badge = document.getElementById('job-status-badge');

    heading.innerText = `Policy Change (Endorsement) - ${job.jobNumber}`;
    badge.innerText = 'DRAFT ENDORSEMENT';
    badge.className = 'gw-badge gw-badge-draft';

    const bldg = (period.lines && period.lines.length > 0 && period.lines[0].buildings && period.lines[0].buildings.length > 0)
        ? period.lines[0].buildings[0]
        : { buildingLimit: 1500000, contentsLimit: 500000 };

    workArea.innerHTML = `
        <div class="gw-panel">
            <div class="gw-panel-header">Policy Change Parameters (PolicyChangeScreen.pcf)</div>
            <div class="gw-panel-body">
                <div class="gw-form-grid">
                    <div class="gw-field">
                        <label>Policy Number</label>
                        <input type="text" value="${period.policyNumber || 'CP-8472910'}" readonly>
                    </div>
                    <div class="gw-field">
                        <label>Effective Date of Change</label>
                        <input type="date" id="end-eff-date" value="${new Date().toISOString().split('T')[0]}">
                    </div>
                    <div class="gw-field">
                        <label>Reason for Change</label>
                        <select id="end-reason">
                            <option>Increase Building Coverage Limit</option>
                            <option>Add Secondary Building</option>
                            <option>Update Address / Contact Details</option>
                        </select>
                    </div>
                    <div class="gw-field">
                        <label>Updated Building Limit ($)</label>
                        <input type="number" id="end-bldg-limit" value="${bldg.buildingLimit || 1500000}">
                    </div>
                </div>

                <div style="margin-top:20px; padding:16px; background:#F1F5F9; border-radius:6px;">
                    <h4 style="margin:0 0 8px 0; color:#1E293B;">Pro-Rata Premium Adjustment Preview</h4>
                    <p style="margin:0; font-size:13px; color:#475569;">Current Term Premium: <strong>$${(period.totalCost || 2400).toLocaleString()}</strong></p>
                    <p style="margin:4px 0 0 0; font-size:13px; color:#059669;">Estimated Pro-Rated Delta: <strong>+$350.00</strong> (Proration Factor: 0.50)</p>
                </div>

                <div style="margin-top:24px; display:flex; gap:12px;">
                    <button class="gw-btn gw-btn-success" onclick="executeBindPolicyChange('${jobNumber}')">Bind Policy Change</button>
                    <button class="gw-btn" onclick="renderPoliciesTab()">Cancel</button>
                </div>
            </div>
        </div>
    `;
}

async function executeBindPolicyChange(jobNumber) {
    const newLimit = parseFloat(document.getElementById('end-bldg-limit').value);
    try {
        const res = await fetch(`${API_BASE}/jobs/endorse`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ jobNumber: jobNumber, buildingLimit: newLimit })
        });
        if (res.ok) {
            alert('Mid-Term Endorsement Bound & Recorded in Policy Ledger!');
            await loadJobs();
            await loadPolicies();
            renderPoliciesTab();
        } else {
            alert('Failed to process policy change');
        }
    } catch (e) {
        alert('Network error binding policy change');
    }
}
