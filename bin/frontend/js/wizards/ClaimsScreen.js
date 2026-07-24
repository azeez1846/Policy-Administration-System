// Emulates Guidewire ClaimHistoryDV.pcf / ClaimsLV.pcf

function renderClaimsStep(container, period) {
    const accNum = period.account ? period.account.accountNumber : 'ACC-1001';

    container.innerHTML = `
        <div id="claims-summary-banner">Loading prior claims & 3-year loss ratio...</div>

        <div class="gw-panel" style="margin-top:20px;">
            <div class="gw-panel-header">
                <span>Prior Claims & Loss History Schedule (ClaimsLV.pcf)</span>
                <button class="gw-btn gw-btn-primary" style="padding:4px 10px; font-size:11px;" onclick="handleReportClaimModal('${accNum}')">+ Report Prior Loss</button>
            </div>
            <div class="gw-panel-body">
                <div id="claims-table-container">Loading claims table...</div>
            </div>
        </div>
    `;

    fetchAccountClaims(accNum, period.totalCost || 2400.00);
}

async function fetchAccountClaims(accNum, totalCost) {
    try {
        const res = await fetch(`${API_BASE}/claims?accountNumber=${accNum}&earnedPremium=${totalCost}`);
        if (res.ok) {
            const data = await res.json();
            const ratio = data.lossRatioPercentage;
            const claims = data.claims || [];

            const isHigh = ratio > 65.0;
            const bannerBg = isHigh ? '#FEF2F2' : '#F0FDF4';
            const bannerBorder = isHigh ? '#EF4444' : '#22C55E';
            const bannerTitleColor = isHigh ? '#991B1B' : '#166534';

            document.getElementById('claims-summary-banner').innerHTML = `
                <div style="padding:16px; background:${bannerBg}; border-left:4px solid ${bannerBorder}; border-radius:6px; display:flex; justify-content:space-between; align-items:center;">
                    <div>
                        <h4 style="margin:0 0 4px 0; color:${bannerTitleColor}; font-size:16px; font-weight:700;">Account 3-Year Loss Ratio Summary</h4>
                        <p style="margin:0; font-size:13px; color:#334155;">3-Year Incurred Losses / Premium Earned ($${totalCost.toFixed(2)})</p>
                    </div>
                    <div style="text-align:right;">
                        <div style="font-size:28px; font-weight:800; color:${bannerTitleColor};">${ratio.toFixed(1)}%</div>
                        <span class="gw-badge" style="background:${isHigh ? '#FCA5A5' : '#86EFAC'}; color:${isHigh ? '#7F1D1D' : '#14532D'};">
                            ${isHigh ? '⚠️ HIGH LOSS RATIO (UW REFERRAL REQUIRED)' : '✓ ACCEPTABLE RISK LEVEL'}
                        </span>
                    </div>
                </div>
            `;

            let rows = claims.map(c => `
                <tr>
                    <td><strong>${c.claimNumber}</strong></td>
                    <td>${c.lossDate}</td>
                    <td>${c.causeOfLoss}</td>
                    <td>$${c.totalPaid.toLocaleString()}</td>
                    <td>$${c.reserveAmount.toLocaleString()}</td>
                    <td><span class="gw-badge ${c.claimStatus === 'Open' ? 'gw-badge-draft' : 'gw-badge-bound'}">${c.claimStatus.toUpperCase()}</span></td>
                </tr>
            `).join('');

            document.getElementById('claims-table-container').innerHTML = `
                <table class="gw-table">
                    <thead>
                        <tr>
                            <th>Claim #</th>
                            <th>Loss Date</th>
                            <th>Cause of Loss</th>
                            <th>Paid Amount ($)</th>
                            <th>Reserve ($)</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>${rows.length > 0 ? rows : '<tr><td colspan="6" style="color:#059669; font-weight:600;">✓ No prior claims reported for this account.</td></tr>'}</tbody>
                </table>
            `;
        }
    } catch (e) {
        console.error("Failed to load claims", e);
    }
}

function handleReportClaimModal(accNum) {
    let overlay = document.getElementById('report-claim-modal-overlay');
    if (!overlay) {
        overlay = document.createElement('div');
        overlay.id = 'report-claim-modal-overlay';
        overlay.style.cssText = `
            position: fixed; top: 0; left: 0; width: 100vw; height: 100vh;
            background: rgba(15, 23, 42, 0.6); backdrop-filter: blur(4px);
            display: flex; justify-content: center; align-items: center; z-index: 9999;
        `;
        document.body.appendChild(overlay);
    }

    overlay.innerHTML = `
        <div style="background: #FFFFFF; border-radius: 12px; width: 480px; max-width: 90vw; overflow: hidden; box-shadow: 0 20px 25px -5px rgba(0,0,0,0.1);">
            <div style="background: #1E293B; color: #FFFFFF; padding: 16px 20px; display: flex; justify-content: space-between; align-items: center;">
                <h3 style="margin: 0; font-size: 16px; font-weight: 700;">Report Prior Loss (ClaimsLV.pcf)</h3>
                <button onclick="document.getElementById('report-claim-modal-overlay').style.display='none'" style="background: transparent; border: none; color: #94A3B8; font-size: 18px; cursor: pointer;">✕</button>
            </div>
            <form style="padding: 20px;" onsubmit="submitPriorClaim(event, '${accNum}')">
                <div style="margin-bottom: 16px;">
                    <label style="font-size: 12px; font-weight: 600; color: #334155; display: block; margin-bottom: 6px;">Cause of Loss *</label>
                    <input type="text" id="claim-cause" value="Vehicle Collision - Rear End" required style="width: 100%; padding: 8px 12px; border: 1px solid #CBD5E1; border-radius: 6px;">
                </div>
                <div style="margin-bottom: 16px;">
                    <label style="font-size: 12px; font-weight: 600; color: #334155; display: block; margin-bottom: 6px;">Loss Date *</label>
                    <input type="date" id="claim-date" value="2025-06-15" required style="width: 100%; padding: 8px 12px; border: 1px solid #CBD5E1; border-radius: 6px;">
                </div>
                <div style="margin-bottom: 20px;">
                    <label style="font-size: 12px; font-weight: 600; color: #334155; display: block; margin-bottom: 6px;">Total Incurred Amount ($) *</label>
                    <input type="number" step="100" id="claim-paid" value="3500" required style="width: 100%; padding: 8px 12px; border: 1px solid #CBD5E1; border-radius: 6px;">
                </div>
                <div style="display: flex; justify-content: flex-end; gap: 10px; border-top: 1px solid #E2E8F0; padding-top: 14px;">
                    <button type="button" onclick="document.getElementById('report-claim-modal-overlay').style.display='none'" class="gw-btn">Cancel</button>
                    <button type="submit" class="gw-btn gw-btn-primary">✓ Save Claim</button>
                </div>
            </form>
        </div>
    `;
    overlay.style.display = 'flex';
}

async function submitPriorClaim(e, accNum) {
    e.preventDefault();
    const cause = document.getElementById('claim-cause').value;
    const lossDate = document.getElementById('claim-date').value;
    const paid = parseFloat(document.getElementById('claim-paid').value) || 0;

    try {
        const res = await fetch(`${API_BASE}/claims`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ accountNumber: accNum, lossDate: lossDate, causeOfLoss: cause, totalPaid: paid, reserveAmount: 0 })
        });
        if (res.ok) {
            document.getElementById('report-claim-modal-overlay').style.display = 'none';
            if (currentJob && currentJob.policyPeriod) {
                renderClaimsStep(document.getElementById('work-area'), currentJob.policyPeriod);
            }
        }
    } catch (e) {
        alert('Error saving claim');
    }
}
