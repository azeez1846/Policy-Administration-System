// Emulates Guidewire PolicyHistoryLV.pcf / Out-of-Sequence Revision Tree & Version Diff Visualizer

function renderPolicyHistoryScreen(container, policyNumber) {
    const polNum = policyNumber || 'POL-88201';

    container.innerHTML = `
        <div style="margin-bottom:20px; display:flex; justify-content:space-between; align-items:center;">
            <div>
                <h2 style="margin:0; font-size:22px; color:#0f172a; font-weight:700;">Policy Revision Audit History & Version Diff Studio (PolicyHistoryLV.pcf)</h2>
                <p style="margin:4px 0 0 0; color:#64748b; font-size:13px;">Policy File: <strong>${polNum}</strong> — Compare term revisions, retroactive slice versions, and endorsement diffs.</p>
            </div>
            <div style="display:flex; gap:10px;">
                <button class="gw-btn gw-btn-primary" onclick="loadPolicyDiffData('${polNum}')">🔍 Compare Version Diff</button>
                <button class="gw-btn" onclick="handleNewOOSEndorsement('${polNum}')">+ Retroactive OOS Endorsement</button>
            </div>
        </div>

        <div style="display:grid; grid-template-columns: 1fr 1fr; gap:20px;">
            <!-- Revision Timeline -->
            <div class="gw-panel">
                <div class="gw-panel-header">
                    <span>Policy Revision Timeline (PolicyHistoryLV.pcf)</span>
                    <span class="gw-badge gw-badge-bound">SPRING BOOT LIVE HISTORY</span>
                </div>
                <div class="gw-panel-body">
                    <div id="oos-timeline-container">Loading revision tree...</div>
                </div>
            </div>

            <!-- Version Comparison Studio -->
            <div class="gw-panel">
                <div class="gw-panel-header" style="display:flex; justify-content:space-between; align-items:center;">
                    <span>🔍 Policy Revision Diff & Delta Inspector</span>
                    <div style="display:flex; gap:6px; align-items:center; font-size:11px;">
                        <span>Seq #</span>
                        <select id="diff-v1-select" style="padding:2px 4px; font-size:11px; border:1px solid #CBD5E1; border-radius:4px;">
                            <option value="1">v1 (Inception)</option>
                            <option value="2">v2 (Endorsement)</option>
                        </select>
                        <span>vs</span>
                        <select id="diff-v2-select" style="padding:2px 4px; font-size:11px; border:1px solid #CBD5E1; border-radius:4px;">
                            <option value="2" selected>v2 (Endorsement)</option>
                            <option value="3">v3 (OOS Revision)</option>
                        </select>
                        <button class="gw-btn gw-btn-primary" style="padding:2px 6px; font-size:10px;" onclick="triggerDiffRun('${polNum}')">Go</button>
                    </div>
                </div>
                <div class="gw-panel-body" id="version-diff-container">
                    Select versions above to inspect line-item coverage and rating diffs...
                </div>
            </div>
        </div>
    `;

    loadPolicyHistoryData(polNum);
    loadPolicyDiffData(polNum, 1, 2);
}

async function loadPolicyHistoryData(polNum) {
    const container = document.getElementById('oos-timeline-container');
    if (!container) return;

    try {
        const res = await fetch(`${API_BASE}/policies/history?policyNumber=${polNum}`);
        if (res.ok) {
            const versions = await res.json();
            let nodes = versions.map(v => `
                <div style="position:relative; padding-left:28px; margin-bottom:16px; border-left:3px solid ${v.oos ? '#eab308' : '#3b82f6'};">
                    <div style="position:absolute; left:-9px; top:0; width:15px; height:15px; border-radius:50%; background:${v.oos ? '#eab308' : '#3b82f6'}; border:3px solid #fff;"></div>
                    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:4px;">
                        <div>
                            <strong style="font-size:14px; color:#0f172a;">Seq #${v.sequenceNumber} — ${v.jobType}</strong>
                            <span class="gw-badge ${v.oos ? 'gw-badge-draft' : 'gw-badge-bound'}" style="${v.oos ? 'background:#FEF08A; color:#854D0E;' : ''}">
                                ${v.oos ? '⚠️ OOS' : 'IN-SEQUENCE'}
                            </span>
                        </div>
                        <span style="font-size:12px; color:#64748b; font-weight:600;">Eff: ${v.effectiveDate}</span>
                    </div>
                    <p style="margin:0; font-size:12px; color:#334155;">${v.description}</p>
                </div>
            `).join('');

            container.innerHTML = `
                <div style="padding:5px;">
                    ${nodes}
                </div>
            `;
        }
    } catch (e) {
        container.innerHTML = `<div style="color:#dc2626; padding:12px;">Failed to load policy revision history.</div>`;
    }
}

function triggerDiffRun(polNum) {
    const v1 = parseInt(document.getElementById('diff-v1-select').value || '1');
    const v2 = parseInt(document.getElementById('diff-v2-select').value || '2');
    loadPolicyDiffData(polNum, v1, v2);
}

async function loadPolicyDiffData(polNum, v1 = 1, v2 = 2) {
    const container = document.getElementById('version-diff-container');
    if (!container) return;

    container.innerHTML = `<div style="color:#64748B; padding:12px;">Calculating version diff tree for Seq #${v1} vs Seq #${v2}...</div>`;

    try {
        const res = await fetch(`${API_BASE}/policies/history/diff?policyNumber=${polNum}&v1=${v1}&v2=${v2}`);
        if (res.ok) {
            const data = await res.json();
            const items = data.diffItems || [];

            let rows = items.map(d => {
                let badgeClass = d.action === 'ADDED' ? 'gw-badge-bound' : (d.action === 'REMOVED' ? 'gw-badge-draft' : 'gw-badge-quote');
                let badgeStyle = d.action === 'ADDED' ? 'background:#DCFCE7; color:#15803D;' : (d.action === 'REMOVED' ? 'background:#FEE2E2; color:#B91C1C;' : 'background:#FEF08A; color:#854D0E;');
                let deltaStr = d.premiumDelta > 0 ? `+$${d.premiumDelta.toFixed(2)}` : (d.premiumDelta < 0 ? `-$${Math.abs(d.premiumDelta).toFixed(2)}` : '$0.00');

                return `
                    <tr style="border-bottom:1px solid #F1F5F9;">
                        <td style="padding:8px; font-size:11px; color:#64748B; font-weight:700;">${d.category}</td>
                        <td style="padding:8px; font-size:12px; font-weight:700; color:#0F172A;">${d.item}</td>
                        <td style="padding:8px;"><span class="gw-badge" style="${badgeStyle}">${d.action}</span></td>
                        <td style="padding:8px; font-size:11px; color:#475569; font-family:monospace;">${d.oldValue}</td>
                        <td style="padding:8px; font-size:11px; color:#0F172A; font-weight:700; font-family:monospace;">${d.newValue}</td>
                        <td style="padding:8px; font-size:12px; font-weight:800; color:${d.premiumDelta > 0 ? '#1D4ED8' : (d.premiumDelta < 0 ? '#15803D' : '#64748B')};">${deltaStr}</td>
                    </tr>
                `;
            }).join('');

            container.innerHTML = `
                <div style="background:#F8FAFC; border:1px solid #E2E8F0; padding:12px; border-radius:6px; margin-bottom:12px; display:flex; justify-content:space-between; align-items:center;">
                    <div>
                        <span style="font-size:11px; color:#64748B; font-weight:700;">NET REVISION PREMIUM DELTA</span>
                        <div style="font-size:20px; font-weight:800; color:${data.netPremiumDelta >= 0 ? '#1E40AF' : '#15803D'};">
                            ${data.netPremiumDelta >= 0 ? '+' : ''}$${data.netPremiumDelta.toLocaleString(undefined, {minimumFractionDigits:2})}
                        </div>
                    </div>
                    <div style="font-size:11px; color:#475569;">
                        Comparing <strong>Seq #${v1}</strong> ➔ <strong>Seq #${v2}</strong>
                    </div>
                </div>

                <table class="gw-table" style="width:100%;">
                    <thead>
                        <tr style="background:#F1F5F9; font-size:11px;">
                            <th>Category</th>
                            <th>Field / Entity</th>
                            <th>Action</th>
                            <th>Previous Value</th>
                            <th>New Revision Value</th>
                            <th>Delta ($)</th>
                        </tr>
                    </thead>
                    <tbody>${rows}</tbody>
                </table>
            `;
        }
    } catch (e) {
        container.innerHTML = `<div style="color:#dc2626; padding:12px;">Failed to compare policy versions.</div>`;
    }
}

function handleNewOOSEndorsement(polNum) {
    let overlay = document.getElementById('oos-modal-overlay');
    if (!overlay) {
        overlay = document.createElement('div');
        overlay.id = 'oos-modal-overlay';
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
                <h3 style="margin: 0; font-size: 16px; font-weight: 700;">Retroactive Out-of-Sequence Endorsement (${polNum})</h3>
                <button onclick="document.getElementById('oos-modal-overlay').style.display='none'" style="background: transparent; border: none; color: #94A3B8; font-size: 18px; cursor: pointer;">✕</button>
            </div>
            <form style="padding: 20px;" onsubmit="submitOOSEndorsement(event, '${polNum}')">
                <div style="margin-bottom: 16px;">
                    <label style="font-size: 12px; font-weight: 600; color: #334155; display: block; margin-bottom: 6px;">Retroactive Effective Date *</label>
                    <input type="date" id="oos-eff-date" value="2026-03-01" required style="width: 100%; padding: 8px 12px; border: 1px solid #CBD5E1; border-radius: 6px;">
                </div>
                <div style="margin-bottom: 20px;">
                    <label style="font-size: 12px; font-weight: 600; color: #334155; display: block; margin-bottom: 6px;">Endorsement Description *</label>
                    <input type="text" id="oos-desc" value="Retroactive Exposure Adjustment & Equipment Schedule" required style="width: 100%; padding: 8px 12px; border: 1px solid #CBD5E1; border-radius: 6px;">
                </div>
                <div style="display: flex; justify-content: flex-end; gap: 10px; border-top: 1px solid #E2E8F0; padding-top: 14px;">
                    <button type="button" onclick="document.getElementById('oos-modal-overlay').style.display='none'" class="gw-btn">Cancel</button>
                    <button type="submit" class="gw-btn gw-btn-primary">⚡ Generate Term Revision</button>
                </div>
            </form>
        </div>
    `;
    overlay.style.display = 'flex';
}

async function submitOOSEndorsement(e, polNum) {
    e.preventDefault();
    const effDate = document.getElementById('oos-eff-date').value;
    const desc = document.getElementById('oos-desc').value;

    try {
        const res = await fetch(`${API_BASE}/policies/history/oos-endorse`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ policyNumber: polNum, effectiveDate: effDate, description: desc })
        });
        if (res.ok) {
            document.getElementById('oos-modal-overlay').style.display = 'none';
            loadPolicyHistoryData(polNum);
            loadPolicyDiffData(polNum, 1, 3);
        }
    } catch (e) {
        alert("Failed to submit OOS endorsement.");
    }
}
