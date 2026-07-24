// Emulates Guidewire RatingMatrixLV.pcf / Product Model Rating Studio

function renderRatingStudioScreen(container) {
    container.innerHTML = `
        <div style="margin-bottom:20px; display:flex; justify-content:space-between; align-items:center;">
            <div>
                <h2 style="margin:0; font-size:22px; color:#0f172a; font-weight:700;">Product Model Rating Matrix Studio (RatingMatrixLV.pcf)</h2>
                <p style="margin:4px 0 0 0; color:#64748b; font-size:13px;">Manage live actuarial rating factors, territory multipliers, and construction risk multipliers.</p>
            </div>
            <button class="gw-btn gw-btn-primary" onclick="loadRatingStudioData()">🔄 Refresh Rating Factors</button>
        </div>

        <div class="gw-panel">
            <div class="gw-panel-header">
                <span>Actuarial Base Rate Multipliers (RatingMatrixLV.pcf)</span>
                <span class="gw-badge gw-badge-bound">SPRING BOOT LIVE SYNC</span>
            </div>
            <div class="gw-panel-body">
                <div id="rating-studio-table-container">Loading rate table factors...</div>
            </div>
        </div>
    `;

    loadRatingStudioData();
}

async function loadRatingStudioData() {
    const container = document.getElementById('rating-studio-table-container');
    if (!container) return;

    try {
        const res = await fetch(`${API_BASE}/rating/factors`);
        if (res.ok) {
            const factors = await res.json();
            let rows = factors.map(f => `
                <tr>
                    <td><span class="gw-badge gw-badge-draft">${f.lineCode}</span></td>
                    <td><strong>${f.tableCode}</strong></td>
                    <td>${f.paramKey}</td>
                    <td><span style="color:#64748b; font-size:12px;">${f.paramValue}</span></td>
                    <td><strong style="color:#0284c7; font-size:15px;">${f.factorValue.toFixed(2)}x</strong></td>
                    <td>
                        <button class="gw-btn" style="padding:3px 8px; font-size:11px;" onclick="handleEditFactor('${f.factorID}', '${f.paramKey}', ${f.factorValue})">✏️ Edit Multiplier</button>
                    </td>
                </tr>
            `).join('');

            container.innerHTML = `
                <table class="gw-table">
                    <thead>
                        <tr>
                            <th>Product Line</th>
                            <th>Rate Table Code</th>
                            <th>Parameter Key</th>
                            <th>Description</th>
                            <th>Current Factor Multiplier</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>${rows}</tbody>
                </table>
            `;
        }
    } catch (e) {
        container.innerHTML = `<div style="color:#dc2626; padding:12px;">Failed to load rating factors.</div>`;
    }
}

function handleEditFactor(factorID, paramKey, currentVal) {
    let overlay = document.getElementById('factor-edit-modal-overlay');
    if (!overlay) {
        overlay = document.createElement('div');
        overlay.id = 'factor-edit-modal-overlay';
        overlay.style.cssText = `
            position: fixed; top: 0; left: 0; width: 100vw; height: 100vh;
            background: rgba(15, 23, 42, 0.6); backdrop-filter: blur(4px);
            display: flex; justify-content: center; align-items: center; z-index: 9999;
        `;
        document.body.appendChild(overlay);
    }

    overlay.innerHTML = `
        <div style="background: #FFFFFF; border-radius: 12px; width: 440px; max-width: 90vw; overflow: hidden; box-shadow: 0 20px 25px -5px rgba(0,0,0,0.1);">
            <div style="background: #1E293B; color: #FFFFFF; padding: 16px 20px; display: flex; justify-content: space-between; align-items: center;">
                <h3 style="margin: 0; font-size: 16px; font-weight: 700;">Edit Actuarial Factor (${paramKey})</h3>
                <button onclick="document.getElementById('factor-edit-modal-overlay').style.display='none'" style="background: transparent; border: none; color: #94A3B8; font-size: 18px; cursor: pointer;">✕</button>
            </div>
            <form style="padding: 20px;" onsubmit="submitFactorOverride(event, '${factorID}')">
                <div style="margin-bottom: 16px;">
                    <label style="font-size: 12px; font-weight: 600; color: #334155; display: block; margin-bottom: 6px;">Target Parameter</label>
                    <input type="text" value="${paramKey}" readonly style="width: 100%; padding: 8px 12px; background: #F8FAFC; border: 1px solid #CBD5E1; border-radius: 6px;">
                </div>
                <div style="margin-bottom: 20px;">
                    <label style="font-size: 12px; font-weight: 600; color: #334155; display: block; margin-bottom: 6px;">New Rating Multiplier (x)</label>
                    <input type="number" step="0.05" id="factor-new-val" value="${currentVal.toFixed(2)}" required style="width: 100%; padding: 8px 12px; border: 1px solid #CBD5E1; border-radius: 6px;">
                </div>
                <div style="display: flex; justify-content: flex-end; gap: 10px; border-top: 1px solid #E2E8F0; padding-top: 14px;">
                    <button type="button" onclick="document.getElementById('factor-edit-modal-overlay').style.display='none'" class="gw-btn">Cancel</button>
                    <button type="submit" class="gw-btn gw-btn-primary">✓ Update Factor</button>
                </div>
            </form>
        </div>
    `;
    overlay.style.display = 'flex';
}

async function submitFactorOverride(e, factorID) {
    e.preventDefault();
    const newVal = parseFloat(document.getElementById('factor-new-val').value);
    if (isNaN(newVal)) return;

    try {
        const res = await fetch(`${API_BASE}/rating/factors`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ factorID: factorID, factorValue: newVal })
        });
        if (res.ok) {
            document.getElementById('factor-edit-modal-overlay').style.display = 'none';
            loadRatingStudioData();
        }
    } catch (e) {
        alert("Failed to update factor multiplier.");
    }
}
