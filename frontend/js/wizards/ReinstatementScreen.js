// Guidewire PolicyCenter - Policy Reinstatement Transaction Wizard UI

async function showReinstatementScreen(jobNumber, policyNumber) {
    const workArea = document.getElementById('work-area');
    const heading = document.getElementById('page-heading');
    const badge = document.getElementById('job-status-badge');
    const subheaderBanner = document.getElementById('subheader-banner');

    if (subheaderBanner) subheaderBanner.style.display = 'none';
    const jobNum = jobNumber || (currentJob ? currentJob.jobNumber : 'SUB-5001');

    if (heading) heading.innerText = `Policy Reinstatement - ${jobNum}`;
    if (badge) {
        badge.innerText = 'REINSTATEMENT DRAFT';
        badge.className = 'gw-badge gw-badge-draft';
        badge.style.display = 'inline-block';
    }

    workArea.innerHTML = `
        <!-- Policy Reinstatement Header Banner -->
        <div style="background: linear-gradient(135deg, #065F46 0%, #047857 100%); color: #FFFFFF; padding: 20px 24px; border-radius: 8px; margin-bottom: 20px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1);">
            <div style="display:flex; justify-content:space-between; align-items:center; flex-wrap:wrap; gap:16px;">
                <div>
                    <div style="font-size:12px; text-transform:uppercase; letter-spacing:1px; color:#A7F3D0; font-weight:700;">Guidewire PolicyCenter • Policy Reinstatement Transaction</div>
                    <h2 style="margin:4px 0 0 0; font-size:20px; font-weight:700; color:#FFFFFF;">Reinstate Policy #${policyNumber || 'CP-8472910'}</h2>
                    <div style="font-size:13px; color:#D1FAE5; margin-top:4px;">Job Number: <strong>${jobNum}</strong></div>
                </div>
                <div>
                    <span class="gw-badge gw-badge-bound" style="font-size:12px; padding:6px 14px;">REINSTATEMENT DRAFT</span>
                </div>
            </div>
        </div>

        <div class="gw-panel">
            <div class="gw-panel-header">Reinstatement Parameters & Underwriting Approval</div>
            <div class="gw-panel-body">
                <div class="gw-form-grid">
                    <div class="gw-field">
                        <label>Policy Number</label>
                        <input type="text" value="${policyNumber || 'CP-8472910'}" readonly style="background:#F8FAFC;">
                    </div>
                    <div class="gw-field">
                        <label>Reinstatement Reason *</label>
                        <select id="rst-reason">
                            <option value="Premium Paid in Full">Premium Paid in Full (Payment Received)</option>
                            <option value="Underwriting Compliance Satisfied">Underwriting Compliance Satisfied</option>
                            <option value="Reinstatement Request Approved">Reinstatement Request Approved by Manager</option>
                        </select>
                    </div>
                </div>

                <div style="margin-top:20px; padding:20px; background:#ECFDF5; border:1px solid #A7F3D0; border-radius:8px;">
                    <h4 style="margin:0 0 6px 0; color:#065F46; font-size:15px;">Policy Reinstatement Status</h4>
                    <p style="margin:0; font-size:13px; color:#047857;">
                        Reinstating this policy will restore coverage without a lapse in coverage from the cancellation effective date.
                    </p>
                </div>

                <div style="margin-top:24px; display:flex; gap:12px;">
                    <button class="gw-btn gw-btn-success" style="font-size:14px; font-weight:700; padding:8px 20px;" onclick="executeReinstatement('${jobNum}')">⚡ Reinstate Policy Coverage</button>
                    <button class="gw-btn" onclick="switchMainTab('desktop')">Cancel</button>
                </div>
            </div>
        </div>
    `;
}

async function executeReinstatement(jobNumber) {
    const reason = document.getElementById('rst-reason') ? document.getElementById('rst-reason').value : 'Underwriting Approval';

    try {
        const res = await fetch(`${API_BASE}/jobs/reinstate`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ jobNumber: jobNumber, reason: reason })
        });
        if (res.ok) {
            alert(`✅ POLICY REINSTATED!\n\nPolicy ${jobNumber} has been reinstated to active Bound status!`);
            if (typeof loadJobs === 'function') await loadJobs();
            if (typeof loadPolicies === 'function') await loadPolicies();
            switchMainTab('desktop');
        } else {
            alert('Failed to reinstate policy');
        }
    } catch (e) {
        alert('Network error reinstating policy: ' + e);
    }
}
