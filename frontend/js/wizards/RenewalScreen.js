// Guidewire PolicyCenter - Policy Renewal Transaction Wizard UI

async function showRenewalScreen(jobNumber, policyNumber) {
    const workArea = document.getElementById('work-area');
    const heading = document.getElementById('page-heading');
    const badge = document.getElementById('job-status-badge');
    const subheaderBanner = document.getElementById('subheader-banner');

    if (subheaderBanner) subheaderBanner.style.display = 'none';
    const jobNum = jobNumber || (currentJob ? currentJob.jobNumber : 'SUB-5001');

    if (heading) heading.innerText = `Policy Renewal - ${jobNum}`;
    if (badge) {
        badge.innerText = 'RENEWAL QUOTING';
        badge.className = 'gw-badge gw-badge-quoted';
        badge.style.display = 'inline-block';
    }

    const nextExp = new Date();
    nextExp.setFullYear(nextExp.getFullYear() + 1);

    workArea.innerHTML = `
        <!-- Policy Renewal Header Banner -->
        <div style="background: linear-gradient(135deg, #1E3A8A 0%, #1E40AF 100%); color: #FFFFFF; padding: 20px 24px; border-radius: 8px; margin-bottom: 20px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1);">
            <div style="display:flex; justify-content:space-between; align-items:center; flex-wrap:wrap; gap:16px;">
                <div>
                    <div style="font-size:12px; text-transform:uppercase; letter-spacing:1px; color:#93C5FD; font-weight:700;">Guidewire PolicyCenter • Policy Renewal Transaction</div>
                    <h2 style="margin:4px 0 0 0; font-size:20px; font-weight:700; color:#FFFFFF;">Renewal Term for Job #${jobNum}</h2>
                    <div style="font-size:13px; color:#DBEAFE; margin-top:4px;">Policy Number: <strong>${policyNumber || 'CP-8472910'}</strong> | Line: <strong>Commercial Property</strong></div>
                </div>
                <div>
                    <span class="gw-badge gw-badge-quoted" style="font-size:12px; padding:6px 14px;">RENEWAL QUOTE</span>
                </div>
            </div>
        </div>

        <div class="gw-panel">
            <div class="gw-panel-header">Renewal Term Parameters (RenewalScreen.pcf)</div>
            <div class="gw-panel-body">
                <div class="gw-form-grid">
                    <div class="gw-field">
                        <label>Current Policy Number</label>
                        <input type="text" value="${policyNumber || 'CP-8472910'}" readonly style="background:#F8FAFC;">
                    </div>
                    <div class="gw-field">
                        <label>Renewal Term Number</label>
                        <input type="text" value="2" readonly style="background:#F8FAFC;">
                    </div>
                    <div class="gw-field">
                        <label>Renewal Effective Date *</label>
                        <input type="date" id="ren-eff-date" value="${new Date().toISOString().split('T')[0]}">
                    </div>
                    <div class="gw-field">
                        <label>Renewal Expiration Date *</label>
                        <input type="date" id="ren-exp-date" value="${nextExp.toISOString().split('T')[0]}">
                    </div>
                </div>

                <div style="margin-top:20px; padding:20px; background:#EFF6FF; border:1px solid #BFDBFE; border-radius:8px;">
                    <h4 style="margin:0 0 8px 0; color:#1E40AF; font-size:15px;">Renewal Rating Quote Breakdown</h4>
                    <div style="display:grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap:16px; margin-top:12px;">
                        <div>
                            <div style="font-size:11px; text-transform:uppercase; color:#64748B;">Prior Term Premium</div>
                            <div style="font-size:18px; font-weight:700; color:#334155;">$2,400.00</div>
                        </div>
                        <div>
                            <div style="font-size:11px; text-transform:uppercase; color:#1E40AF;">Inflation & Trend Adjustment</div>
                            <div style="font-size:18px; font-weight:700; color:#2563EB;">+ 5.0%%</div>
                        </div>
                        <div>
                            <div style="font-size:11px; text-transform:uppercase; color:#047857;">New Renewal Annual Premium</div>
                            <div style="font-size:18px; font-weight:800; color:#059669;">$2,520.00</div>
                        </div>
                    </div>
                </div>

                <div style="margin-top:24px; display:flex; gap:12px;">
                    <button class="gw-btn gw-btn-primary" style="background:#1E40AF; font-size:14px; font-weight:700; padding:8px 20px;" onclick="executeBindRenewal('${jobNum}')">⚡ Issue Renewal Policy Term</button>
                    <button class="gw-btn" onclick="switchMainTab('desktop')">Cancel</button>
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
            alert(`✅ RENEWAL POLICY ISSUED!\n\nJob Number ${jobNumber} has been renewed and issued for the next annual policy term!`);
            if (typeof loadJobs === 'function') await loadJobs();
            if (typeof loadPolicies === 'function') await loadPolicies();
            switchMainTab('desktop');
        } else {
            alert('Failed to issue renewal policy');
        }
    } catch (e) {
        alert('Network error renewing policy: ' + e);
    }
}
