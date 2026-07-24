// Guidewire PolicyCenter - Policy Cancellation Transaction Wizard UI

async function showCancellationScreen(jobNumber, policyNumber) {
    const workArea = document.getElementById('work-area');
    const heading = document.getElementById('page-heading');
    const badge = document.getElementById('job-status-badge');
    const subheaderBanner = document.getElementById('subheader-banner');

    if (subheaderBanner) subheaderBanner.style.display = 'none';
    const jobNum = jobNumber || (currentJob ? currentJob.jobNumber : 'SUB-5001');

    if (heading) heading.innerText = `Policy Cancellation - ${jobNum}`;
    if (badge) {
        badge.innerText = 'CANCELLATION DRAFT';
        badge.className = 'gw-badge gw-badge-draft';
        badge.style.display = 'inline-block';
    }

    const today = new Date().toISOString().split('T')[0];

    workArea.innerHTML = `
        <!-- Policy Cancellation Header Banner -->
        <div style="background: linear-gradient(135deg, #7F1D1D 0%, #991B1B 100%); color: #FFFFFF; padding: 20px 24px; border-radius: 8px; margin-bottom: 20px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1);">
            <div style="display:flex; justify-content:space-between; align-items:center; flex-wrap:wrap; gap:16px;">
                <div>
                    <div style="font-size:12px; text-transform:uppercase; letter-spacing:1px; color:#FCA5A5; font-weight:700;">Guidewire PolicyCenter • Policy Cancellation Transaction</div>
                    <h2 style="margin:4px 0 0 0; font-size:20px; font-weight:700; color:#FFFFFF;">Initiate Cancellation for Job #${jobNum}</h2>
                    <div style="font-size:13px; color:#FEE2E2; margin-top:4px;">Policy Number: <strong>${policyNumber || 'CP-8472910'}</strong></div>
                </div>
                <div>
                    <span class="gw-badge" style="background:#EF4444; color:#fff; font-size:12px; padding:6px 14px;">CANCELLATION DRAFT</span>
                </div>
            </div>
        </div>

        <div class="gw-panel">
            <div class="gw-panel-header">Cancellation Parameters & Refund Calculation</div>
            <div class="gw-panel-body">
                <div class="gw-form-grid">
                    <div class="gw-field">
                        <label>Policy Number</label>
                        <input type="text" value="${policyNumber || 'CP-8472910'}" readonly style="background:#F8FAFC;">
                    </div>
                    <div class="gw-field">
                        <label>Cancellation Calculation Method *</label>
                        <select id="cnc-method">
                            <option value="ProRata">Pro-Rata (Proportional refund for unused days)</option>
                            <option value="Flat">Flat (100% full refund from inception date)</option>
                            <option value="ShortRate">Short-Rate (Pro-rata refund minus short-rate penalty)</option>
                        </select>
                    </div>
                    <div class="gw-field">
                        <label>Effective Date of Cancellation *</label>
                        <input type="date" id="cnc-date" value="${today}">
                    </div>
                    <div class="gw-field">
                        <label>Reason Code *</label>
                        <select id="cnc-reason">
                            <option value="Customer Request - Sold Premises">Customer Request - Sold Premises</option>
                            <option value="Non-Payment of Premium">Non-Payment of Premium</option>
                            <option value="Underwriting Risk Increase">Underwriting Risk Increase</option>
                            <option value="Rewritten with another carrier">Rewritten with another carrier</option>
                        </select>
                    </div>
                </div>

                <div style="margin-top:20px; padding:20px; background:#FEF2F2; border:1px solid #FCA5A5; border-radius:8px;">
                    <h4 style="margin:0 0 8px 0; color:#991B1B; font-size:15px;">Cancellation Return Premium Calculation Preview</h4>
                    <div style="display:grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap:16px; margin-top:12px;">
                        <div>
                            <div style="font-size:11px; text-transform:uppercase; color:#991B1B;">Original Written Premium</div>
                            <div style="font-size:18px; font-weight:700; color:#451A1A;">$2,400.00</div>
                        </div>
                        <div>
                            <div style="font-size:11px; text-transform:uppercase; color:#991B1B;">Unearned Term Remaining</div>
                            <div style="font-size:18px; font-weight:700; color:#B91C1C;">182 / 365 days</div>
                        </div>
                        <div>
                            <div style="font-size:11px; text-transform:uppercase; color:#991B1B;">Estimated Return Premium</div>
                            <div style="font-size:18px; font-weight:800; color:#DC2626;">- $1,200.00</div>
                        </div>
                    </div>
                </div>

                <div style="margin-top:24px; display:flex; gap:12px;">
                    <button class="gw-btn" style="background:#DC2626; color:#fff; font-size:14px; font-weight:700; padding:8px 20px;" onclick="executeCancelPolicy('${jobNum}')">⚠️ Confirm & Execute Cancellation</button>
                    <button class="gw-btn" onclick="switchMainTab('desktop')">Cancel</button>
                </div>
            </div>
        </div>
    `;
}

async function executeCancelPolicy(jobNumber) {
    const method = document.getElementById('cnc-method') ? document.getElementById('cnc-method').value : 'ProRata';
    const reason = document.getElementById('cnc-reason') ? document.getElementById('cnc-reason').value : 'Customer Request';

    try {
        const res = await fetch(`${API_BASE}/jobs/cancel`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ jobNumber: jobNumber, cancelType: method, reason: reason })
        });
        if (res.ok) {
            alert(`⚠️ POLICY CANCELLED!\n\nJob ${jobNumber} has been cancelled and unearned return premium posted to BillingCenter!`);
            if (typeof loadJobs === 'function') await loadJobs();
            if (typeof loadPolicies === 'function') await loadPolicies();
            switchMainTab('desktop');
        } else {
            alert('Failed to cancel policy');
        }
    } catch (e) {
        alert('Network error cancelling policy: ' + e);
    }
}
