// Emulates Guidewire PolicyHistoryLV.pcf / Out-of-Sequence Revision Tree Visualizer

function renderPolicyHistoryScreen(container, policyNumber) {
    const polNum = policyNumber || 'POL-88201';

    container.innerHTML = `
        <div style="margin-bottom:20px; display:flex; justify-content:space-between; align-items:center;">
            <div>
                <h2 style="margin:0; font-size:22px; color:#0f172a; font-weight:700;">Policy Revision Audit History & OOS Slice Tree (PolicyHistoryLV.pcf)</h2>
                <p style="margin:4px 0 0 0; color:#64748b; font-size:13px;">Policy File: <strong>${polNum}</strong> — Visualizes retroactive term slice versions and Out-of-Sequence endorsement conflicts.</p>
            </div>
            <button class="gw-btn gw-btn-primary" onclick="handleNewOOSEndorsement('${polNum}')">+ Retroactive OOS Endorsement</button>
        </div>

        <div class="gw-panel">
            <div class="gw-panel-header">
                <span>Policy Revision Timeline (PolicyHistoryLV.pcf)</span>
                <span class="gw-badge gw-badge-bound">SPRING BOOT LIVE HISTORY</span>
            </div>
            <div class="gw-panel-body">
                <div id="oos-timeline-container">Loading revision tree...</div>
            </div>
        </div>
    `;

    loadPolicyHistoryData(polNum);
}

async function loadPolicyHistoryData(polNum) {
    const container = document.getElementById('oos-timeline-container');
    if (!container) return;

    try {
        const res = await fetch(`${API_BASE}/policies/history?policyNumber=${polNum}`);
        if (res.ok) {
            const versions = await res.json();
            let nodes = versions.map(v => `
                <div style="position:relative; padding-left:28px; margin-bottom:20px; border-left:3px solid ${v.oos ? '#eab308' : '#3b82f6'};">
                    <div style="position:absolute; left:-9px; top:0; width:15px; height:15px; border-radius:50%; background:${v.oos ? '#eab308' : '#3b82f6'}; border:3px solid #fff;"></div>
                    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:4px;">
                        <div>
                            <strong style="font-size:15px; color:#0f172a;">Seq #${v.sequenceNumber} — ${v.jobType}</strong>
                            <span class="gw-badge ${v.oos ? 'gw-badge-draft' : 'gw-badge-bound'}" style="${v.oos ? 'background:#FEF08A; color:#854D0E;' : ''}">
                                ${v.oos ? '⚠️ OUT-OF-SEQUENCE (OOS)' : 'IN-SEQUENCE'}
                            </span>
                        </div>
                        <span style="font-size:13px; color:#64748b; font-weight:600;">Effective: ${v.effectiveDate}</span>
                    </div>
                    <p style="margin:0; font-size:13px; color:#334155;">${v.description}</p>
                </div>
            `).join('');

            container.innerHTML = `
                <div style="padding:10px 5px;">
                    ${nodes}
                </div>
            `;
        }
    } catch (e) {
        container.innerHTML = `<div style="color:#dc2626; padding:12px;">Failed to load policy revision history.</div>`;
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
        }
    } catch (e) {
        alert("Failed to submit OOS endorsement.");
    }
}
